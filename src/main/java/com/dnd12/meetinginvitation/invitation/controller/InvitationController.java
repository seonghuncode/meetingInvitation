package com.dnd12.meetinginvitation.invitation.controller;

import com.dnd12.meetinginvitation.invitation.dto.InvitationDto;
import com.dnd12.meetinginvitation.invitation.dto.ResponseDto;
import com.dnd12.meetinginvitation.invitation.service.FileStorageService;
import com.dnd12.meetinginvitation.invitation.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "초대장생성", description = "1. fontName, Sticker이름의 경우 DB에 없을 경우 오류 발생 -> 폰트 생성, 스티커 생성  API를 이용해서 먼저 생성 후 다시 요청! \n2. backgroundImageData의 경우 data:image/png;base64,뒤에 인코딩한 문자열로 요청!")
    //@ApiResponse(responseCode = "200", description = "테스트 성공")
    @RequestMapping(value = "/invitation", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto> makeInvitation(@RequestBody InvitationDto invitationDto){
        return invitationService.makeInvitation(invitationDto);
    }

    //폰트 생성
    @Operation(summary = "폰트 생성", description = "")
    @RequestMapping(value = "/font", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto>  makeFont(@RequestParam("fontName") String fontName){

        return invitationService.makeFont(fontName);
    }

    //스티커 생성
    @Operation(summary = "스티커 생성", description = "")
    @RequestMapping(value = "/sticker", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto>  makeSticker(@RequestParam("stickerName") String stickerName){
        return invitationService.makeSticker(stickerName);
    }

    //초대장 전체 조회(페이징) (Form-Data)
    @Operation(summary = "초대장 전체 조회", description = "EX) page=0(첫 번째 페이지), size=10(한 페이지에 10개씩), sort=desc(최신순 정렬)")
    @RequestMapping(value = "/invitations", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> getInvitationAllList(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort) {
        return invitationService.getInvitationAllList(userId, page, size, sort);
    }

    //생성한 초대장 전체 조회(페이징)
    @Operation(summary = "생성한 초대장 초회", description = "EX) page=0(첫 번째 페이지), size=10(한 페이지에 10개씩), sort=desc(최신순 정렬)")
    @RequestMapping(value = "/creatorInvitations", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> getCreatorInvitations(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort) {
        return invitationService.getCreatorInvitationAllList(userId, page, size, sort);
    }


    //받은 초대장 전체 조회(페이징)
    @Operation(summary = "받은 초대장 초회", description = "EX) page=0(첫 번째 페이지), size=10(한 페이지에 10개씩), sort=desc(최신순 정렬)")
    @RequestMapping(value = "/invitedInvitations", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> getInvitedInvitationAllList(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort) {
        return invitationService.getInvitedInvitationAllList(userId, page, size, sort);
    }

    //특정 초대장 조회
    @Operation(summary = "특정 초대장 조회", description = "")
    @RequestMapping(value = "/specificInvitation", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> getSpecificInvitation(
            @RequestParam("invitationId") Long invitationId
    ){
        return invitationService.getSpecificInvitation(invitationId);
    }

    //초대장 이미지 요청(썸네일)
    @Operation(summary = "초대장 배경 이미지 조회", description = "오류 발생시 404로 예외처리 필요")
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
    @Operation(summary = "초대장 수정", description = "1. fontName, Sticker이름의 경우 DB에 없을 경우 오류 발생 -> 폰트 생성, 스티커 생성  API를 이용해서 먼저 생성 후 다시 요청! \n2. backgroundImageData의 경우 data:image/png;base64,뒤에 인코딩한 문자열로 요청!")
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
