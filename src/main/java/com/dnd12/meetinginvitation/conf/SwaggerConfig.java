package com.dnd12.meetinginvitation.conf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "API 명세서",
        description = "API 명세서",
        version = "V1"))

@Configuration
public class SwaggerConfig {

}
