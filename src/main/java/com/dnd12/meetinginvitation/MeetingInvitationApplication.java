package com.dnd12.meetinginvitation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MeetingInvitationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingInvitationApplication.class, args);
    }

}
