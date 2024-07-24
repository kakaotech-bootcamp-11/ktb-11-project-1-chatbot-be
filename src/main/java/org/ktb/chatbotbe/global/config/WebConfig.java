package org.ktb.chatbotbe.global.config;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.global.oauth.CustomOAuth2UserService;
import org.ktb.chatbotbe.global.oauth.OAuth2MemberSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebConfig {
    //    private final OAuth2MemberSuccessHandler successHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 경로별 인가작업
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))           // 이거 있으면 @AuthenticationPrincipal 통해서 정보 안가져옴
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/**").permitAll()
                        .anyRequest().authenticated()
                );
        // OAuth 로그인
        http.oauth2Login((oauth) -> oauth
//                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
//                                .baseUri("/oauth/login/kakao"))
                        .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
                                .baseUri("/login/oauth2/code")
                        )
                        .defaultSuccessUrl("/success")
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
//                        .successHandler(successHandler)
//                .failureHandler(customFailureHandler)
        );


        return http.build();
    }
}
