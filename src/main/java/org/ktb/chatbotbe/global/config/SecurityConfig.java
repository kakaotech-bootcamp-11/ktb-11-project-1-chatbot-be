package org.ktb.chatbotbe.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.global.oauth.CustomOAuth2UserService;
import org.ktb.chatbotbe.global.oauth.handler.OAuth2FailureHandler;
import org.ktb.chatbotbe.global.oauth.handler.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Value("${spring.security.oauth2.home}")
    private String homepageUrl;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 경로별 인가작업
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 인가가 있는 사용자에 대해 접근권한 확인
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/oauth2/**", "/login/**", "/weather/**", "/schedule").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                .oauth2Login((oauth) -> oauth
                                .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
                                                // baseUri로 들어오는 요청을 redirectionEndpoint에 설정된 곳으로 리디렉트
                                                // default -> {baseUrl}
                                                .baseUri("/login/oauth2/code")
//                                        .baseUri("/test")
                                )
                                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                        .userService(customOAuth2UserService))
                                .successHandler(successHandler)
//                                .failureHandler(failureHandler)
                )
                // 로그아웃
                .logout(logout -> logout
                        .logoutUrl("/oauth2/logout")                       // 로그아웃 요청 url
                        .logoutSuccessUrl(homepageUrl)      // 로그아웃후 리디렌션 할 url
                        .invalidateHttpSession(true)                // 세션 무효화
                        .deleteCookies("JSESSIONID")    // 쿠키 삭제
                )
        ;

        return http.build();
    }
}
