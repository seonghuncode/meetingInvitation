package com.dnd12.meetinginvitation.invitation.service;


import com.dnd12.meetinginvitation.invitation.dto.InvitationDto;
import com.dnd12.meetinginvitation.invitation.dto.ResponseDto;
import com.dnd12.meetinginvitation.invitation.entity.Font;
import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.invitation.entity.InvitationParticipant;
import com.dnd12.meetinginvitation.invitation.entity.Sticker;
import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import com.dnd12.meetinginvitation.invitation.repository.FontRepository;
import com.dnd12.meetinginvitation.invitation.repository.InvitationParticipantRepository;
import com.dnd12.meetinginvitation.invitation.repository.InvitationRepository;
import com.dnd12.meetinginvitation.invitation.repository.StickerRepository;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

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

    //초대장 생성
    public ResponseEntity<ResponseDto> makeInvitation(InvitationDto invitationDto){

        try {
            // User 조회 (creator_id를 통해)
            User user = userRepository.findById(invitationDto.getCreator_id())
                    .orElseThrow(() -> new RuntimeException("Fail: user not found with id: " + invitationDto.getCreator_id()));

            //전달 받은 폰트 이름으로 폰트 조회
            Font font = fontRepository.findByFontName(invitationDto.getFontName())
                    .orElseThrow(() -> new RuntimeException("Fail: font not found with name: " + invitationDto.getFontName()));

            //전달 받은 스티커 이름으로 스티커 조회
            Sticker sticker = stickerRepository.findByStickerName(invitationDto.getSticker())
                    .orElseThrow(() -> new RuntimeException("Fail: sticker not found with name: " + invitationDto.getSticker()));

            //전달 받은 배경(Base64로 인코딩된 이미지 파일을 디코딩 해서 특정 경로에 저장 후 해당 url저장)
            String fileUrl = "/getInvitationImage?fileName=" +  fileStorageService.saveBase64File(invitationDto.getBackgroundImageData());

            //초대장 생성
            Invitation invitation = Invitation.builder()
                    .user(user)
                    .createdAt(invitationDto.getCreated_at())
                    .updatedAt(invitationDto.getUpdated_at())
                    .place(invitationDto.getPlace())
                    .detailAddress(invitationDto.getDetail_address())
                    .date(invitationDto.getDate())
                    .maxAttendences(invitationDto.getMax_attendances())
                    .description(invitationDto.getDescription())
                    .state(invitationDto.getState())
                    .link(invitationDto.getLink())
                    .title(invitationDto.getTitle())
                    .font(font)
                    .sticker(sticker)
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

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }

    }


    //폰트 생성
    public ResponseEntity<ResponseDto> makeFont(Font font){

        try{
            fontRepository.save(font);
            return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }
    }

    //스티커 생성
    public ResponseEntity<ResponseDto> makeSticker(Sticker sticker){
        try {
            stickerRepository.save(sticker);
            return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }
    }

    
    //초대장 전체 조회
//    @Transactional(readOnly = true)
//    public ResponseEntity<ResponseDto> getInvitationAllList(Long userId){
//
//        if(!userRepository.existsById(userId)){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseDto.fail("Fail: user not found with id: " + userId));
//        }
//
//
//        List<Invitation> list = invitationRepository.findByUserId(userId);
//        List<InvitationDto> invitationList = new ArrayList<>();
//
//        for (Invitation invitation : list) {
//
//            // URL 경로로 MultipartFile을 로드하기 위해 FileStorageService 사용
//            //MultipartFile invitationTemplate = fileStorageService.loadFileAsMultipartFile(invitation.getInvitationTemplate_url());
//
//            invitationList.add(new InvitationDto(
//                    invitation.getUser().getId(),
//                    invitation.getId(),
//                    invitation.getCreatedAt(),
//                    invitation.getUpdatedAt(),
//                    invitation.getPlace(),
//                    invitation.getDetailAddress(),
//                    invitation.getDate(),
//                    invitation.getMaxAttendences(),
//                    invitation.getDescription(),
//                    invitation.getState(),
//                    invitation.getLink(),
//                    //invitationTemplate,
//                    null,
//                    invitation.getInvitationTemplate_url()
//            ));
//
//        }
//        return ResponseEntity.ok(ResponseDto.success(invitationList));
//    }

//초대장 수정
//public ResponseEntity<ResponseDto> modifyInvitation(Long id, InvitationDto invitationDto){
//
//    //초대장 조회
//    Optional<Invitation> optionalInvitation = invitationRepository.findById(id);
//    if (!optionalInvitation.isPresent()) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ResponseDto.fail("Fail: Invitation found with id " + id));
//    }
//
//    Invitation invitation = optionalInvitation.get();
//    if(invitationDto.getPlace() != null){
//        invitation.setPlace(invitationDto.getPlace());
//    }
//    if(invitationDto.getDetail_address() != null){
//        invitation.setDetailAddress(invitationDto.getDetail_address());
//    }
//    if(invitationDto.getDate() != null){
//        invitation.setDate(invitationDto.getDate());
//    }
//    //해당 값은 수정을 안해도 기존 값 넘겨주어야 한다
//    invitation.setMaxAttendences(invitationDto.getMax_attendances());
//    if(invitationDto.getDescription() != null){
//        invitation.setDescription(invitationDto.getDescription());
//    }
//    //해당 값은 수정을 안해도 기존 값 넘겨주어야 한다.
//    invitationDto.setState(invitationDto.getState());
//    if(invitationDto.getLink() != null){
//        invitation.setLink(invitationDto.getLink());
//    }
//
//    String fileUrl = null;
//    try {
//        fileUrl = "/getInvitationImage?fileName=" +  fileStorageService.saveBase64File(invitationDto.getImageData());
//        invitation.setInvitationTemplate_url(fileUrl);
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }
//
//    invitation.setUpdatedAt(LocalDateTime.now());
//
//    invitationRepository.save(invitation);
//    return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));
//
//}

//초대장 삭제
public ResponseEntity<ResponseDto> deleteInvitation(Long invitationId){
    boolean isDeleted = invitationRepository.existsById(invitationId);
    if(!isDeleted){
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
