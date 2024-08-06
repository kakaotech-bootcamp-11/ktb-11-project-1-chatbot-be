package org.ktb.chatbotbe.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findBySocialId(Long socialId) {
        return userRepository.findBysocialId(socialId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
