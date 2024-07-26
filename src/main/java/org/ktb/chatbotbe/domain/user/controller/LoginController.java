package org.ktb.chatbotbe.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/success")
    public OAuth2User success(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal OAuth2User user) {
        return "test";
    }
}
