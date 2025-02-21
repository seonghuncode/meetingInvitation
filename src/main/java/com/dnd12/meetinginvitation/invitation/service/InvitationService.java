package com.dnd12.meetinginvitation.invitation.service;


import com.dnd12.meetinginvitation.attendence.dto.AttendanceResponseDto;
import com.dnd12.meetinginvitation.attendence.entity.Attendance;
import com.dnd12.meetinginvitation.attendence.repository.AttendanceRepository;
import com.dnd12.meetinginvitation.invitation.dto.InvitationDto;
import com.dnd12.meetinginvitation.invitation.dto.ResponseDto;
import com.dnd12.meetinginvitation.invitation.entity.*;
import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import com.dnd12.meetinginvitation.invitation.repository.*;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private InvitationParticipantRepository invitationParticipantRepository;
    @Autowired
    private FontRepository fontRepository;
    @Autowired
    private StickerRepository stickerRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    //초대장 생성
    @Transactional
    public ResponseEntity<ResponseDto> makeInvitation(InvitationDto invitationDto) {

        try {
            // User 조회 (creator_id를 통해)
            User user = userRepository.findById(invitationDto.getCreatorId())
                    .orElseThrow(() -> new RuntimeException("Fail: user not found with id: " + invitationDto.getCreatorId()));

            //전달 받은 폰트 이름으로 폰트 조회
            Font font = fontRepository.findByFontName(invitationDto.getFontName())
                    .orElseThrow(() -> new RuntimeException("Fail: font not found with name: " + invitationDto.getFontName()));

            //전달 받은 스티커 이름으로 스티커 조회(1. 빈문자열 또는 null일 경우)
            if (invitationDto.getSticker() == null || invitationDto.getSticker().trim().isEmpty() && invitationDto.getSticker().trim().equals("")) {
                invitationDto.setSticker(null);
                Optional<Sticker> sticker = stickerRepository.findByStickerName(invitationDto.getSticker());
                if(sticker.isEmpty()){
                    makeSticker(null);
                }
            }
            //전달 받은 sticker이름 확인
            Sticker sticker = stickerRepository.findByStickerName(invitationDto.getSticker())
                    .orElseThrow(() -> new RuntimeException("Fail: sticker not found with name: " + invitationDto.getSticker()));



            //전달 받은 편지지 이름으로 편지지 종류 조회(1. 빈문자열 또는 null일 경우)
            if (invitationDto.getThemeName() == null || invitationDto.getThemeName().trim().isEmpty() && invitationDto.getThemeName().trim().equals("")) {
                invitationDto.setThemeName(null);
                Optional<Theme> theme = themeRepository.findByThemeName(invitationDto.getThemeName());
                if(theme.isEmpty()){
                    makeTheme(null);
                }
            }
            //전달 받은 편지지 종류 확인
            Theme theme = themeRepository.findByThemeName(invitationDto.getThemeName())
                    .orElseThrow(() -> new RuntimeException("Fail: themeName not found with name: " + invitationDto.getThemeName()));

            //전달 받은 배경(Base64로 인코딩된 이미지 파일을 디코딩 해서 특정 경로에 저장 후 해당 url저장)
            String fileUrl = "/getInvitationImage?fileName=" + fileStorageService.saveBase64File(invitationDto.getBackgroundImageData());

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatterDateTime = now.format(formatter);

            //초대장 생성
            Invitation invitation = Invitation.builder()
                    .user(user)
                    .createdAt(now)
                    .updatedAt(now)
                    .organizerName(invitationDto.getOrganizerName())
                    .place(invitationDto.getPlace())
                    .detailAddress(invitationDto.getDetailAddress())
                    .date(invitationDto.getDate())
                    .maxAttendences(invitationDto.getMaxAttendances())
                    .description(invitationDto.getDescription())
                    .state(invitationDto.getState())
                    .link(invitationDto.getLink())
                    .title(invitationDto.getTitle())
                    .font(font)
                    .sticker(sticker)
                    .theme(theme)
                    .basicBackgroundType(invitationDto.getBasicBackgroundType())
                    .backgroundUrl(fileUrl)
                    .build();


            //초대장 저장
            invitationRepository.save(invitation);


            //초대장 생성시 invitationType은 항상 CREATOR (초대장을 누군가에게 전송할 경우 INVITED로 변경해서 전송)
            InvitationParticipant creatorParticipant = InvitationParticipant.builder()
                    .invitation(invitation)
                    .user(user)
                    .invitationType(InvitationType.CREATOR)
                    .date(LocalDateTime.now())
                    .build();
            //초대장 타입 저장(CREATOR)
            invitationParticipantRepository.save(creatorParticipant);


            return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }

    }


    //폰트 생성
    @Transactional
    public ResponseEntity<ResponseDto> makeFont(String fontName) {
        Font font = new Font();
        font.setFontName(fontName);
        try {
            fontRepository.save(font);
            return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }
    }

    //스티커 생성
    @Transactional
    public ResponseEntity<ResponseDto> makeSticker(String stickerName) {
        try {
            Sticker sticker = new Sticker();
            sticker.setStickerName(stickerName);
            stickerRepository.save(sticker);
            return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));
        } catch (Exception e) {
            // 예외 발생 시 롤백 수행
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }
    }

    //편지지 종류 생성
    @Transactional
    public ResponseEntity<ResponseDto> makeTheme(String themeName) {
        try {
            Theme theme = new Theme();
            theme.setThemeName(themeName);
            themeRepository.save(theme);
            return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));
        } catch (Exception e) {
            // 예외 발생 시 롤백 수행
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }
    }


    //초대장 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto> getInvitationAllList(Long userId, int page, int size, String sort) {
        return getInvitationListByParam(userId, page, size, sort, "ALL");
    }


    //생성한 초대장 전체조회
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto> getCreatorInvitationAllList(Long userId, int page, int size, String sort) {
        return getInvitationListByParam(userId, page, size, sort, "CREATOR");
    }

    //생성한 초대장 전체조회
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto> getInvitedInvitationAllList(Long userId, int page, int size, String sort) {
        return getInvitationListByParam(userId, page, size, sort, "INVITED");
    }



    // 날자의 경우 초대장 갱신 날짜를 반환하기 때문에 응답 갱신 날짜를 반환하도록 수정 필요
    //특정 초대장 응답 리스트
    public ResponseEntity<ResponseDto> getInvitationResponseList(Long invitationId){
        Invitation invitation = invitationRepository.findInvitationById(invitationId);
        List<Attendance> attendances = attendanceRepository.findByInvitationId(invitationId);

        List<AttendanceResponseDto> dtoList = new ArrayList<>();
        for(Attendance attendace : attendances){
            AttendanceResponseDto dto = new AttendanceResponseDto();
            dto.setInvitationId(attendace.getInvitation().getId());
            dto.setUserId(attendace.getUser().getId());
            dto.setName(attendace.getName());
            dto.setState(attendace.getState());
            dto.setWriteDate(invitation.getUpdatedAt());
            dtoList.add(dto);
        }
        return ResponseEntity.ok(ResponseDto.success(dtoList));
    }


    //특정 초대장 조회
    public ResponseEntity<ResponseDto> getSpecificInvitation(Long invitationId){
        //Invitation invitation = invitationRepository.findInvitationById(invitationId);
        //Invitation invitation = invitationRepository.findById(invitationId)
        //        .orElseThrow(() -> new EntityNotFoundException("Invitation not found with id: " + invitationId));

        Invitation invitation = invitationRepository.findInvitationById(invitationId);
        List<InvitationDto> invitationList = new ArrayList<>();

        InvitationDto dto = new InvitationDto();
        dto.setCreatorId(invitation.getUser().getId());
        dto.setInvitationId(invitation.getId());
        dto.setCreatedAt(invitation.getCreatedAt());
        dto.setUpdatedAt(invitation.getUpdatedAt());
        dto.setPlace(invitation.getPlace());
        dto.setDetailAddress(invitation.getDetailAddress());
        dto.setDescription(invitation.getDescription());
        dto.setDate(invitation.getDate());
        dto.setMaxAttendances(invitation.getMaxAttendences());
        dto.setState(invitation.getState());
        dto.setLink(invitation.getLink());
        dto.setFontName(invitation.getLink());
        dto.setFontName(invitation.getFont().getFontName());
        dto.setSticker(invitation.getSticker().getStickerName());
        dto.setOrganizerName(invitation.getOrganizerName());
        dto.setTitle(invitation.getTitle());
        dto.setBackgroundImageData(invitation.getBackgroundUrl());
        dto.setThemeName(invitation.getTheme().getThemeName());
        invitationList.add(dto);

        return ResponseEntity.ok(ResponseDto.success(invitationList));
    }


    private ResponseEntity<ResponseDto> getInvitationListByParam(Long userId, int page, int size, String sort, String listType) {

        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail("Fail: user not found with id: " + userId));
        }


        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt")); // createdAt 기준 정렬

        Page<Invitation> invitationPage = null;
        List<InvitationDto> invitationList = new ArrayList<>();
        boolean result = false;
        if (listType.equals("ALL")) {
            invitationPage = invitationRepository.findByUserId(userId, pageable);
            result = true;
        } else if (listType.equals("CREATOR")) {
            invitationPage = invitationRepository.findByParticipantsUserIdAndParticipantsInvitationType(userId, InvitationType.CREATOR, pageable);
            result = true;
        } else if (listType.equals("INVITED")) {
            invitationPage = invitationRepository.findByParticipantsUserIdAndParticipantsInvitationType(userId, InvitationType.INVITED, pageable);
            result = true;
        }

        if (result) {

            for (Invitation invitation : invitationPage) {

                //특정사용자, 현재 초대장에 대한 invitationType조회
                InvitationParticipant invitationParticipant = invitationParticipantRepository.findByInvitationIdAndUserId(invitation.getId(), userId);

                InvitationDto dto = new InvitationDto();
                dto.setCreatorId(invitation.getUser().getId());
                dto.setInvitationId(invitation.getId());
                dto.setCreatedAt(invitation.getCreatedAt());
                dto.setUpdatedAt(invitation.getUpdatedAt());
                dto.setPlace(invitation.getPlace());
                dto.setDetailAddress(invitation.getDetailAddress());
                dto.setDescription(invitation.getDescription());
                dto.setDate(invitation.getDate());
                dto.setMaxAttendances(invitation.getMaxAttendences());
                dto.setState(invitation.getState());
                dto.setLink(invitation.getLink());
                dto.setFontName(invitation.getLink());
                dto.setFontName(invitation.getFont().getFontName());
                dto.setSticker(invitation.getSticker().getStickerName());
                dto.setOrganizerName(invitation.getOrganizerName());
                dto.setTitle(invitation.getTitle());
                dto.setBackgroundImageData(invitation.getBackgroundUrl());
                dto.setThemeName(invitation.getTheme().getThemeName());
                dto.setInvitationType(invitationParticipant.getInvitationType());
                dto.setThemeName( invitation.getTheme().getThemeName());
                invitationList.add(dto);

//                invitationList.add(new InvitationDto(
//                        invitation.getUser().getId(),
//                        invitation.getId(),
//                        invitation.getCreatedAt(),
//                        invitation.getUpdatedAt(),
//                        invitation.getPlace(),
//                        invitation.getDetailAddress(),
//                        invitation.getDate(),
//                        invitation.getMaxAttendences(),
//                        invitation.getDescription(),
//                        invitation.getState(),
//                        invitation.getLink(),
//                        invitationParticipant.getInvitationType(),
//                        invitation.getFont().getFontName(),
//                        invitation.getSticker().getStickerName(),
//                        invitation.getTitle(),
//                        invitation.getBackgroundUrl(),
//                        invitation.getOrganizerName(),
//                        invitation.getTheme().getThemeName()
//                ));

            }
            return ResponseEntity.ok(ResponseDto.success(invitationList));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.fail("Fail: invitationType is wrong."));
    }




//초대장 수정
public ResponseEntity<ResponseDto> modifyInvitation(Long id, InvitationDto invitationDto) {

    //초대장 조회
    Optional<Invitation> optionalInvitation = invitationRepository.findById(id);
    if (!optionalInvitation.isPresent()) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.fail("Fail: Invitation found with id " + id));
    }

    Invitation invitation = optionalInvitation.get();
    if (invitationDto.getPlace() != null) {
        invitation.setPlace(invitationDto.getPlace());
    }
    if (invitationDto.getDetailAddress() != null) {
        invitation.setDetailAddress(invitationDto.getDetailAddress());
    }
    if (invitationDto.getDate() != null) {
        invitation.setDate(invitationDto.getDate());
    }
    //해당 값은 수정을 안해도 기존 값 넘겨주어야 한다
    invitation.setMaxAttendences(invitationDto.getMaxAttendances());

    if (invitationDto.getDescription() != null) {
        invitation.setDescription(invitationDto.getDescription());
    }

    //해당 값은 수정을 안해도 기존 값 넘겨주어야 한다.
    invitationDto.setState(invitationDto.getState());
    if (invitationDto.getLink() != null) {
        invitation.setLink(invitationDto.getLink());
    }

    //기본 배경 타입 수정
    invitation.setBasicBackgroundType(invitationDto.getBasicBackgroundType());

    String fileUrl = null;
    try {
        fileUrl = "/getInvitationImage?fileName=" + fileStorageService.saveBase64File(invitationDto.getBackgroundImageData());
        invitation.setBackgroundUrl(fileUrl);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    //전달 받은 폰트 이름으로 폰트 조회
    Font font = fontRepository.findByFontName(invitationDto.getFontName())
            .orElseThrow(() -> new RuntimeException("Fail: font not found with name: " + invitationDto.getFontName()));

    //전달 받은 스티커 이름으로 스티커 조회(1. 빈문자열 또는 null일 경우)
    if (invitationDto.getSticker() == null || invitationDto.getSticker().trim().isEmpty() && invitationDto.getSticker().trim().equals("")) {
        invitationDto.setSticker(null);
        Optional<Sticker> sticker = stickerRepository.findByStickerName(invitationDto.getSticker());
        if(sticker.isEmpty()){
            makeSticker(null);
        }
    }
    //전달 받은 sticker이름 확인
    Sticker sticker = stickerRepository.findByStickerName(invitationDto.getSticker())
            .orElseThrow(() -> new RuntimeException("Fail: sticker not found with name: " + invitationDto.getSticker()));

    /*//전달 받은 스티커 이름으로 스티커 조회
    Sticker sticker = stickerRepository.findByStickerName(invitationDto.getSticker())
            .orElseThrow(() -> new RuntimeException("Fail: sticker not found with name: " + invitationDto.getSticker()));
*/
    invitation.setFont(font);

    invitation.setSticker(sticker);


    if (invitationDto.getTitle() != null) {
        invitation.setTitle(invitationDto.getTitle());
    }
    //초대장 업데이트 날짜 갱신
    invitation.setUpdatedAt(LocalDateTime.now());

    invitationRepository.save(invitation);
    return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));

}

//초대장 삭제
public ResponseEntity<ResponseDto> deleteInvitation(Long invitationId) {
    boolean isDeleted = invitationRepository.existsById(invitationId);
    if (!isDeleted) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.fail("Fail: Invitation found with id " + invitationId));
    }
    invitationRepository.deleteById(invitationId);
    return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));
}

public String getFileExtension(String fileName) {
    int index = fileName.lastIndexOf('.');
    return index > 0 ? fileName.substring(index + 1) : "";
}

public MediaType getMediaTypeForFileExtension(String fileExtension) {
    switch (fileExtension) {
        case "png":
            return MediaType.IMAGE_PNG;
        case "jpg":
        case "jpeg":
            return MediaType.IMAGE_JPEG;
        case "gif":
            return MediaType.IMAGE_GIF;
        case "bmp":
            return MediaType.valueOf("image/bmp");
        case "webp":
            return MediaType.valueOf("image/webp");
        default:
            return MediaType.APPLICATION_OCTET_STREAM; // 기본값은 일반적인 바이너리 파일
    }
}




}
