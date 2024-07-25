package org.ktb.chatbotbe.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {
    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/success")
    public OAuth2User success(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal OAuth2User user) {
        log.info(user.toString());
        return "test";
    }

    @GetMapping("/redirect")
    public ResponseEntity<String> redirect() {
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "https://naver.com").build();
    }

}
