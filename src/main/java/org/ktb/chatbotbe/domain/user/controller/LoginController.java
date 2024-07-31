package org.ktb.chatbotbe.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    @GetMapping("/success")
    public OAuth2User success(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal OAuth2User user) {
        User temp = userService.findBySocialId(user.getAttribute("id"));
        return temp.toString();
    }
}
