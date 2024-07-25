package org.ktb.chatbotbe.global.oauth;

import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DefaultErrorAttributes errorAttributes;

    public CustomOAuth2UserService() {
        super();
    }

    // 성공적으로 로그인시 행동할 유저 서비스
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 유저 로딩
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        User user = getUser(attributes);

        return oAuth2User;
    }

    private User getUser(Map<String, Object> attributes) {
        String socialId =  String.valueOf(attributes.get("id"));
        Map<String, String>  properties = (Map<String, String>) attributes.get("properties");
        User findUser = userRepository.findBysocialId(socialId).orElse(null);

        if (findUser == null) {
            User createUser = User.builder()
                    .nickname(properties.get("nickname"))
                    .socialId(socialId)
                    .build();
            return userRepository.save(createUser);
        }

        return findUser;
    }

}