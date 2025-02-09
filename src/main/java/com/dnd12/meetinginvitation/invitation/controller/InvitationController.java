package com.dnd12.meetinginvitation.invitation.controller;

import com.dnd12.meetinginvitation.invitation.dto.InvitationDto;
import com.dnd12.meetinginvitation.invitation.dto.ResponseDto;
import com.dnd12.meetinginvitation.invitation.service.FileStorageService;
import com.dnd12.meetinginvitation.invitation.service.InvitationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private FileStorageService fileStorageService;

    //초대장 생성 (JSON)
    @RequestMapping(value = "/invitation", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto> makeInvitation(@RequestBody InvitationDto invitationDto){
        return invitationService.makeInvitation(invitationDto);
    }

    //초대장 전체 조회 (Form-Data)
    @RequestMapping(value = "/invitations", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> getInvitationAllList(@RequestParam("userId") Long userId){
        return invitationService.getInvitationAllList(userId);
    }

    //초대장 이미지 요청(썸네일)
    @RequestMapping(value = "/getInvitationImage", method = RequestMethod.GET)
    public ResponseEntity<Resource> getInvitationImage(@RequestParam("fileName") String fileName){
        try {
            Path filePath =Path.of(Paths.get(fileStorageService.getUploadDir()) + "/" + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // 파일 확장자 추출
                String fileExtension = invitationService.getFileExtension(fileName).toLowerCase();
                MediaType mediaType = invitationService.getMediaTypeForFileExtension(fileExtension);

                return ResponseEntity.ok()
                        .contentType(mediaType) // 동적으로 설정된 MIME 타입
                        .body(resource);

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //초대장 수정 (JSON)
    @RequestMapping(value = "/invitation", method = RequestMethod.PUT)
    public ResponseEntity<ResponseDto> modifyInvitation(@RequestParam("invitationId") Long invitationId, @RequestBody InvitationDto invitationDto){
        return invitationService.modifyInvitation(invitationId, invitationDto);
    }

    //초대장 삭제 (Form-Data)
    @DeleteMapping(value = "/invitation/{invitationId}")
    public ResponseEntity<ResponseDto> deleteInvitation(@PathVariable("invitationId") Long invitationId){
        return invitationService.deleteInvitation(invitationId);
    }







}
