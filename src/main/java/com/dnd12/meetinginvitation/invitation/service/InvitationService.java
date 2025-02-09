package com.dnd12.meetinginvitation.invitation.service;


import com.dnd12.meetinginvitation.invitation.dto.InvitationDto;
import com.dnd12.meetinginvitation.invitation.dto.ResponseDto;
import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.invitation.entity.InvitationParticipant;
import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import com.dnd12.meetinginvitation.invitation.repository.InvitationParticipantRepository;
import com.dnd12.meetinginvitation.invitation.repository.InvitationRepository;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private InvitationParticipantRepository invitationParticipantRepository;

    //초대장 생성
    public ResponseEntity<ResponseDto> makeInvitation(InvitationDto invitationDto){

        try {
            log.info("유저 조회");

            // User 조회 (creator_id를 통해)
            User user = userRepository.findById(invitationDto.getCreator_id())
                    .orElseThrow(() -> new RuntimeException("Fail: user not found with id: " + invitationDto.getCreator_id()));

            log.info("유저 조회 끝");

            //파일 저장 처리
            // 이미지 Base64 디코딩 및 저장
            String fileUrl = "/getInvitationImage?fileName=" +  fileStorageService.saveBase64File(invitationDto.getImageData());

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
                    .build();

            //초대장 저장
            invitationRepository.save(invitation);

           /* InvitationType invitationType = InvitationType.ERROR;
            if(InvitationType.INVITED.equals("INVITED")){
                invitationType = InvitationType.INVITED;
            }else if(InvitationType.CREATOR.equals("CREATOR")){
                invitationType = InvitationType.INVITED;
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ResponseDto.fail("Fail: The invitationType must be CREATOR or INVITED."));
            }*/

            //초대장 생성시 invitationType은 항상 CREATOR (초대장을 누군가에게 전송할 경우 INVITED로 변경해서 전송)
            InvitationParticipant creatorParticipant = InvitationParticipant.builder()
                    .invitation(invitation)
                    .user(user)
                    .invitationType(InvitationType.CREATOR)
                    .build();
            //초대장 타입 저장
            invitationParticipantRepository.save(creatorParticipant);


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
                    invitation.getId(),
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
                    invitation.getInvitationTemplate_url()
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

    String fileUrl = null;
    try {
        fileUrl = "/getInvitationImage?fileName=" +  fileStorageService.saveBase64File(invitationDto.getImageData());
        invitation.setInvitationTemplate_url(fileUrl);
    } catch (IOException e) {
        throw new RuntimeException(e);
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
