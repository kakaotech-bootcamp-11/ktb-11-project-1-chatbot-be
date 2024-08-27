package org.ktb.chatbotbe.global.oauth;

import org.ktb.chatbotbe.domain.user.entity.CommentStarter;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.CommentStarterRepository;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentStarterRepository commentStarterRepository;

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
        Long socialId = (Long) attributes.get("id");
        Map<String, String> properties = (Map<String, String>) attributes.get("properties");
        User findUser = userRepository.findBysocialId(socialId).orElse(null);

        if (findUser == null) {
            User createUser = User.builder()
                    .nickname(properties.get("nickname"))
                    .socialId(socialId)
                    .build();
            userRepository.save(createUser);
            commentStarterRepository.saveAll(
                    List.of(
                            CommentStarter.builder()
                                    .comment("오늘 날씨에 맞는 음식 추천해줘")
                                    .user(createUser)
                                    .build(),
                            CommentStarter.builder()
                                    .comment("집 어떻게 가야할지 추천해줘")
                                    .user(createUser)
                                    .build(),
                            CommentStarter.builder()
                                    .comment("가장 빠른 코딩테스트 날짜가 언제야?")
                                    .user(createUser)
                                    .build())
                    );

            return createUser;
        }

        return findUser;
    }

}
