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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @RequestMapping(value = "/makeInvitation", method = RequestMethod.PUT)
    public ResponseEntity<ResponseDto> makeInvitation(@ModelAttribute InvitationDto invitationDto, @AuthenticationPrincipal String email){
        log.info("유저:{}", invitationDto.getCreator_id());
        log.info("장소:{}", invitationDto.getPlace());
        log.info("상태:{}", invitationDto.getState());
        return invitationService.makeInvitation(invitationDto);
    }

    @RequestMapping(value = "/getInvitationAllList", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> getInvitationAllList(@RequestParam("userId") Long userId){
        return invitationService.getInvitationAllList(userId);
    }

    @RequestMapping(value = "/getInvitationImage", method = RequestMethod.GET)
    public ResponseEntity<Resource> getInvitationImage(@RequestParam("fileName") String fileName){
        try {
            Path filePath =Path.of(Paths.get(fileStorageService.getUploadDir()) + "/" + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // 이미지 MIME 타입 지정
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @RequestMapping(value = "/modifyInvitation", method = RequestMethod.PUT)
    public ResponseEntity<ResponseDto> modifyInvitation(@RequestParam("invitationId") Long invitationId, @ModelAttribute InvitationDto invitationDto){
        return invitationService.modifyInvitation(invitationId, invitationDto);
    }

    @DeleteMapping(value = "/deleteInvitation/{invitationId}")
    public ResponseEntity<ResponseDto> deleteInvitation(@PathVariable("invitationId") Long invitationId){
        return invitationService.deleteInvitation(invitationId);
    }







}
