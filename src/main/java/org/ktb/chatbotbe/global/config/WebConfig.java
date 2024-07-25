package org.ktb.chatbotbe.global.config;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.global.oauth.CustomOAuth2UserService;
import org.ktb.chatbotbe.global.oauth.handler.OAuth2FailureHandler;
import org.ktb.chatbotbe.global.oauth.handler.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebConfig {
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 경로별 인가작업
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // Spring Security 세션으로 인증된 사용자 상태 유지
                // @AuthenticationPrincipal 통해서 정보 안가져옴
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인가가 있는 사용자에 대해 접근권한 확인
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/success", "/login/**").permitAll()
                        .requestMatchers("/oauth2/token").authenticated()
                        .anyRequest().authenticated()
                );

        // OAuth 로그인
        http
                .oauth2Login((oauth) -> oauth
                                .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
                                        // baseUri로 들어오는 요청을 redirectionEndpoint에 설정된 곳으로 리디렉트
                                        // default -> {baseUrl}
                                        .baseUri("/login/oauth2/code")
                                )
//                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
//                                .accessTokenResponseClient())
                                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                        .userService(customOAuth2UserService))
                                .successHandler(successHandler)
                                .failureHandler(failureHandler)
                );

        return http.build();
    }
}
