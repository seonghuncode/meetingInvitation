package com.dnd12.meetinginvitation.invitation.service;


import com.dnd12.meetinginvitation.invitation.dto.InvitationDto;
import com.dnd12.meetinginvitation.invitation.dto.ResponseDto;
import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.invitation.repository.InvitationRepository;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

    //초대장 생성
    public ResponseEntity<ResponseDto> makeInvitation(InvitationDto invitationDto){
        log.info("서비스 진입");
        log.info("유저:{}", invitationDto.getCreator_id());
        log.info("장소:{}", invitationDto.getPlace());
        log.info("상태:{}", invitationDto.getState());
        log.info("DTO:{}", invitationDto);


        try {
            log.info("유저 조회");

            // User 조회 (creator_id를 통해)
            User user = userRepository.findById(invitationDto.getCreator_id())
                    .orElseThrow(() -> new RuntimeException("Fail: user not found with id: " + invitationDto.getCreator_id()));

            log.info("유저 조회 끝");

            //파일 저장 처리
            String fileUrl = null;
            MultipartFile file = invitationDto.getInvitationTemplate();
            if(file != null && !file.isEmpty()){
                //ip주소는 제외하고 저장 -> 도메인이나 고정IP를 사용하지 않기 때문
                //프론트에서 http://IP주소:PORT/fileUrl형태로 사용
                fileUrl = "/getInvitationImage?fileName=" +  fileStorageService.saveFile(file);
            }

            Invitation invitation = Invitation.builder()
                    .user(user)
                    .createdAt(invitationDto.getCreated_at())
                    .place(invitationDto.getPlace())
                    .detailAddress(invitationDto.getDetail_address())
                    .date(invitationDto.getDate())
                    .maxAttendences(invitationDto.getMax_attendances())
                    .description(invitationDto.getDescription())
                    .state(invitationDto.getState())
                    .link(invitationDto.getLink())
                    .invitationTemplate_url(fileUrl)
                    .invitationType(invitationDto.getInvitationType())
                    .build();

            invitationRepository.save(invitation);
            return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }

    }

    
    //초대장 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto> getInvitationAllList(Long userId){

        if(!userRepository.existsById(userId)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail("Fail: user not found with id: " + userId));
        }


        List<Invitation> list = invitationRepository.findByUserId(userId);
        List<InvitationDto> invitationList = new ArrayList<>();

        for (Invitation invitation : list) {

            // URL 경로로 MultipartFile을 로드하기 위해 FileStorageService 사용
            //MultipartFile invitationTemplate = fileStorageService.loadFileAsMultipartFile(invitation.getInvitationTemplate_url());
            
            invitationList.add(new InvitationDto(
                    invitation.getUser().getId(),
                    invitation.getCreatedAt(),
                    invitation.getUpdatedAt(),
                    invitation.getPlace(),
                    invitation.getDetailAddress(),
                    invitation.getDate(),
                    invitation.getMaxAttendences(),
                    invitation.getDescription(),
                    invitation.getState(),
                    invitation.getLink(),
                    //invitationTemplate,
                    null,
                    invitation.getInvitationTemplate_url(),
                    invitation.getInvitationType()
            ));

        }
        return ResponseEntity.ok(ResponseDto.success(invitationList));
    }

//초대장 수정
public ResponseEntity<ResponseDto> modifyInvitation(Long id, InvitationDto invitationDto){

    //초대장 조회
    Optional<Invitation> optionalInvitation = invitationRepository.findById(id);
    if (!optionalInvitation.isPresent()) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.fail("Fail: Invitation found with id " + id));
    }

    Invitation invitation = optionalInvitation.get();
    if(invitationDto.getPlace() != null){
        invitation.setPlace(invitationDto.getPlace());
    }
    if(invitationDto.getDetail_address() != null){
        invitation.setDetailAddress(invitationDto.getDetail_address());
    }
    if(invitationDto.getDate() != null){
        invitation.setDate(invitationDto.getDate());
    }
    //해당 값은 수정을 안해도 기존 값 넘겨주어야 한다
    invitation.setMaxAttendences(invitationDto.getMax_attendances());
    if(invitationDto.getDescription() != null){
        invitation.setDescription(invitationDto.getDescription());
    }
    //해당 값은 수정을 안해도 기존 값 넘겨주어야 한다.
    invitationDto.setState(invitationDto.getState());
    if(invitationDto.getLink() != null){
        invitation.setLink(invitationDto.getLink());
    }

    //파일 저장 처리
    String fileUrl = null;
    MultipartFile file = invitationDto.getInvitationTemplate();
    if(file != null && !file.isEmpty()){
        //ip주소는 제외하고 저장 -> 도메인이나 고정IP를 사용하지 않기 때문
        //프론트에서 http://IP주소:PORT/fileUrl형태로 사용
        try {
            fileUrl = "/getInvitationImage?fileName=" +  fileStorageService.saveFile(file);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.fail(e.getMessage()));
        }
    }

    if(invitationDto.getInvitationTemplate() != null){
        invitation.setInvitationTemplate_url(fileUrl);
    }

    invitation.setUpdatedAt(LocalDateTime.now());

    invitationRepository.save(invitation);
    return ResponseEntity.ok(ResponseDto.success(Collections.singletonList("")));

}

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






}
