package com.dnd12.meetinginvitation.user.service;

import com.dnd12.meetinginvitation.invitation.service.InvitationService;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final InvitationService invitationService;

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
