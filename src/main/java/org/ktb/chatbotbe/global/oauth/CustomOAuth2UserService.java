package org.ktb.chatbotbe.global.oauth;

import org.ktb.chatbotbe.domain.user.entity.CommentStarter;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.CommentStarterRepository;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
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
        User findUser = userRepository.findBySocialId(socialId).orElse(null);

        if (findUser == null) {
            User createUser = User.builder()
                    .nickname(properties.get("nickname"))
                    .socialId(socialId)
                    .profileImage(properties.get("profile_image"))
                    .build();
            userRepository.save(createUser);
            commentStarterRepository.saveAll(
                    createDefaultCommentStarters(createUser)
            );

            return createUser;
        }

        return findUser;
    }

    private List<CommentStarter> createDefaultCommentStarters(User createUser) {
        // todo
        // 대화스타터 수정
        // "오늘 날씨에 맞는 음식 추천해줘" -> 오늘 병가 쓰고 싶어
        // "집 어떻게 가야할지 추천해줘" -> 통합 신청 센터 링크 알려줘
        // "가장 빠른 코딩테스트 날짜가 언제야?" -> 이번주 일정 알려줘
        return List.of(
                CommentStarter.builder()
                        .comment("오늘 병가 쓰고 싶어")
                        .user(createUser)
                        .build(),
                CommentStarter.builder()
                        .comment("통합 신청 센터 링크 알려줘")
                        .user(createUser)
                        .build(),
                CommentStarter.builder()
                        .comment("이번주 일정 알려줘")
                        .user(createUser)
                        .build());
    }

}
