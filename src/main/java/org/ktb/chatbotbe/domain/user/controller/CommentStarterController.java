package org.ktb.chatbotbe.domain.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.user.dto.CommentResponse;
import org.ktb.chatbotbe.domain.user.dto.UpdateCommentRequest;
import org.ktb.chatbotbe.domain.user.dto.UpdateResponse;
import org.ktb.chatbotbe.domain.user.service.CommentStarterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/comment_starter")
@RequiredArgsConstructor
public class CommentStarterController {
    private final CommentStarterService commentStarterService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentStarters(@AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        return ResponseEntity.ok(commentStarterService.getComments(userId));
    }

    @PutMapping
    public ResponseEntity<List<UpdateResponse>> getCommentStartersByUser(@AuthenticationPrincipal OAuth2User user,
                                                                         @RequestBody List<UpdateCommentRequest> request) {
        Long userId = user.getAttribute("id");
        List<UpdateResponse> updateResponse = commentStarterService.updateComment(userId, request);
        return ResponseEntity.ok(updateResponse);
    }
}
