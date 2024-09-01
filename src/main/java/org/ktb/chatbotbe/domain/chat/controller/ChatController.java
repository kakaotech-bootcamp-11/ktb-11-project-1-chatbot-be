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
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/me/titles")
    public ResponseEntity<List<ChatResponse>> getChatTitles(@AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        return ResponseEntity.ok(chatService.findChatTitles(userId));
    }

    @GetMapping("/me/{chatId}")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessages(@PathVariable Long chatId, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        List<ChatMessageResponse> messages = chatService.findChatMessagesByChatId(chatId, userId);

        return ResponseEntity.ok(messages);
    }

    @PostMapping(value = "/me/{chatId}/messages", produces = "text/event-stream; charset=euc-kr")
    public Flux<ChatMessageResponse> addChatMessage(@PathVariable Long chatId, @RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        return chatService.addChatMessage(chatId, chatMessageRequest, userId);
    }

    @DeleteMapping("/me/{chatId}")
    public ResponseEntity<Map<String, String>> deleteChat(@AuthenticationPrincipal OAuth2User user, @PathVariable Long chatId) {
        Long userId = user.getAttribute("id");
        chatService.deleteChat(chatId, userId);
        return ResponseEntity.ok(Map.of("message", "성공적으로 채팅 쓰레드를 삭제했습니다."));
    }


    // todo
    // crateNewChat()는 지금 채팅방 생성과 메시지를 보내는 동작 두 가지를 실행중
    // 분리하는게 좋아 보임
    @PostMapping("/me/new")
    public ResponseEntity<NewChatResponse> createNewChat(@RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        NewChatResponse newChatResponse = chatService.createNewChat(chatMessageRequest, userId);

        return ResponseEntity.ok(newChatResponse);
    }

}
