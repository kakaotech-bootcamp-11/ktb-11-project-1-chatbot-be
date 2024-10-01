package org.ktb.chatbotbe.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.user.dto.UserInfo;
import org.ktb.chatbotbe.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/setting")
public class UserController {
    private final UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        Long userId = principal.getAttribute("id");
        UserInfo userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/address")
    @Deprecated
    public ResponseEntity<String> setAddress() {
        return new ResponseEntity<>("This endpoint is deprecated", HttpStatus.GONE);
    }

}
