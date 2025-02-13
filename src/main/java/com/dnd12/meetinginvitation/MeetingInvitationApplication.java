package com.dnd12.meetinginvitation;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class MeetingInvitationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingInvitationApplication.class, args);
    }

}
