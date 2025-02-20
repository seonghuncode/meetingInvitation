package com.dnd12.meetinginvitation.user.service;

import com.dnd12.meetinginvitation.invitation.service.InvitationService;
import com.dnd12.meetinginvitation.user.dto.GetUserDto;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final InvitationService invitationService;

    //회원 탈퇴
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    //유저 조회
    public Optional<GetUserDto> getUser(Long userId) {

        //map을 사용하여 null 체크
        return userRepository.findById(userId)
                .map(user -> GetUserDto.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .profileImage(user.getProfileImageUrl())
                        .build());
    }

}
