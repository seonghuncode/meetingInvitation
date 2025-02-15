package com.dnd12.meetinginvitation.invitation.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR;

    public FileStorageService(){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){
            // 배포전 테스트 환경
            UPLOAD_DIR = "C:\\Users\\USER\\OneDrive\\바탕 화면\\DND12기\\테스트데이터\\uploads";
        }else{
            //실제 배포시 저장 경로
            UPLOAD_DIR = "/var/www/uploads";
        }
    }

    public String saveBase64File(String base64Data) throws IOException{
        if (base64Data == null || base64Data.isEmpty()) {
            return null;
        }

        // Base64 문자열에서 파일 확장자 추출 (예: data:image/png;base64,... 형태일 경우)
        String extension = ".jpg"; // 기본 확장자 설정
        if (base64Data.startsWith("data:image/png;base64,")) {
            extension = ".png";
            base64Data = base64Data.replace("data:image/png;base64,", "");
        } else if (base64Data.startsWith("data:image/jpeg;base64,")) {
            extension = ".jpg";
            base64Data = base64Data.replace("data:image/jpeg;base64,", "");
        } else if (base64Data.startsWith("data:image/gif;base64,")) {
            extension = ".gif";
            base64Data = base64Data.replace("data:image/gif;base64,", "");
        }

        // 폴더가 존재하지 않으면 생성
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 고유한 파일명 생성 (UUID 사용)
        String newFileName = UUID.randomUUID().toString() + extension;
        String filePath = Paths.get(UPLOAD_DIR, newFileName).toString();

        // Base64 디코딩 후 파일로 저장
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
        }

        return newFileName; // 저장된 파일명 반환
    }


    // 저장된 경로 반환
    public String getUploadDir() {
        return UPLOAD_DIR;
    }

}
