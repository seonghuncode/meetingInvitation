package com.dnd12.meetinginvitation.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String UPLOAD_DIR;

    //정적리소스 제공 -> spring에서 /uploads/ url(DB에 저장되는 이미지 경로)로 이미지 접근 가능하도록 설정
    public WebConfig() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            UPLOAD_DIR = "file:C:\\Users\\USER\\OneDrive\\바탕 화면\\DND12기\\테스트데이터\\uploads";
        } else {
            UPLOAD_DIR = "file:/var/www/uploads/";
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(UPLOAD_DIR);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //모든 경로에 대해 CORS를 적용
                .allowedOrigins(
                        "http://localhost:8080", // 서버 배포 시 해당 도메인으로 변경 필요
                        "http://localhost:3000" // 프론트 서버 배포 후 수정
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true) //인증 정보 허용
                .maxAge(3600); //preflight 캐시 시간 (1시간)

    }
}