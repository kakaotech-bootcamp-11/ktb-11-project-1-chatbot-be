package org.ktb.chatbotbe.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.NewChatResponse;
import org.ktb.chatbotbe.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/me/titles")
    public ResponseEntity<List<ChatResponse>> getChatTitles(@AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");

//        return ResponseEntity.status(HttpStatus.OK).body(chatService.findChatTitles(userId));
        return ResponseEntity.ok(chatService.findChatTitles(userId));
    }

    @GetMapping("/me/{chatId}")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessages(@PathVariable Long chatId, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        List<ChatMessageResponse> messages = chatService.findChatMessagesByChatId(chatId, userId);

        return ResponseEntity.ok(messages);
    }

    @PostMapping("/me/{chatId}/messages")
    public ResponseEntity<ChatMessageResponse> addChatMessage(@PathVariable Long chatId, @RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        log.info(userId.toString());
        ChatMessageResponse chatMessageResponse = chatService.addChatMessage(chatId, chatMessageRequest, userId);
        return ResponseEntity.ok(chatMessageResponse);
    }

    @DeleteMapping("/me/{chatId}")
    public ResponseEntity<String> deleteChat(@AuthenticationPrincipal OAuth2User user, @PathVariable Long chatId) {
        Long userId = user.getAttribute("id");
        chatService.deleteChat(chatId, userId);
        return ResponseEntity.ok("성공적으로 채팅 쓰레드를 삭제했습니다.");
    }

    @PostMapping("/me/new")
    public ResponseEntity<NewChatResponse> createNewChat(@RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        NewChatResponse newChatResponse = chatService.createNewChat(chatMessageRequest, userId);
        return ResponseEntity.ok(newChatResponse);
    }
}
