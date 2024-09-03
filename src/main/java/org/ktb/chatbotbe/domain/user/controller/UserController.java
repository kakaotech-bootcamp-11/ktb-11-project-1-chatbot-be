package org.ktb.chatbotbe.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.user.dto.AddressUpdateRequest;
import org.ktb.chatbotbe.domain.user.dto.UserInfo;
import org.ktb.chatbotbe.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======
import java.util.Map;

>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
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
<<<<<<< HEAD
    public void setAddress(@AuthenticationPrincipal OAuth2User user,
                           @RequestBody AddressUpdateRequest requestBody){
        Long userId = user.getAttribute("id");
        userService.updateUserAddress(userId, requestBody);
=======
    public ResponseEntity<Map<String, String>> setAddress(@AuthenticationPrincipal OAuth2User user,
                                                          @RequestBody AddressUpdateRequest requestBody){
        Long userId = user.getAttribute("id");
        userService.updateUserAddress(userId, requestBody);

        return ResponseEntity.ok(Map.of("message", "success"));
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
    }

}
