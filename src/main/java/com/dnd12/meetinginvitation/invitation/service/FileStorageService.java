package com.dnd12.meetinginvitation.invitation.service;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 폴더가 존재하지 않으면 생성
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 고유한 파일명 생성 (UUID 사용)
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;

        // 파일 저장
        String filePath = Paths.get(UPLOAD_DIR, newFileName).toString();
        file.transferTo(new File(filePath));

        // 저장된 이미지 URL 반환
        //return "/uploads/" + newFileName;
        return newFileName;
    }

    // 파일을 MultipartFile로 변환하는 메서드
    public MultipartFile loadFileAsMultipartFile(String fileName) throws IOException {
        // 파일 경로로부터 파일을 읽음
        //Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        //filePath = Path.of(Paths.get(UPLOAD_DIR) + "/e5c05854-da8a-401c-a303-dc14aaac0913.png");
        Path filePath = Path.of(Paths.get(UPLOAD_DIR) + "/" + fileName);
        File file = filePath.toFile();

        // InputStream을 이용해 MockMultipartFile로 변환
        return new MockMultipartFile(file.getName(), new FileInputStream(file));
    }

    // 저장된 경로 반환
    public String getUploadDir() {
        return UPLOAD_DIR;
    }

}
