package org.ktb.chatbotbe.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.user.dto.AddressUpdateRequest;
import org.ktb.chatbotbe.domain.user.dto.UserInfo;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findBySocialId(Long socialId) {
        return userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserInfo getUserInfo(Long userId) {
        User user = userRepository.findBySocialId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserInfo.builder()
                .username(user.getNickname())
                .profileImage(user.getProfileImage())
                .address(user.getAddress())
                .build();
    }

    @Transactional
    public void updateUserAddress(Long userId, AddressUpdateRequest request){
        User user = userRepository.findBySocialId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.updateAddress(request.getAddress(), request.getZipNo(), request.getAddrDetail());
    }

}
