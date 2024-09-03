package org.ktb.chatbotbe.domain.chat.controller;

import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.NewChatResponse;
=======
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
import org.ktb.chatbotbe.domain.chat.dto.service.response.*;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
import org.ktb.chatbotbe.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
=======
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
<<<<<<< HEAD
=======
@Slf4j
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
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

<<<<<<< HEAD
    @PostMapping("/me/{chatId}/messages")
    public ResponseEntity<ChatMessageResponse> addChatMessage(@PathVariable Long chatId, @RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        ChatMessageResponse chatMessageResponse = chatService.addChatMessage(chatId, chatMessageRequest, userId);
        return ResponseEntity.ok(chatMessageResponse);
=======
    @PostMapping(value = "/me/{chatId}/messages", produces = "text/event-stream; charset=euc-kr")
    public Flux<NewChatResponse> addChatMessage(@PathVariable Long chatId, @RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        Flux<ChatMessageResponse> responseFlux = chatService.addChatMessage(chatId, chatMessageRequest, userId);
        return responseFlux.map(response -> {
            return NewChatResponse.builder()
                    .aiResponse(response)
                    .build();
        }).concatWith(Mono.just(NewChatResponse.builder()
                .aiResponse(new DoneResponse())
                .build()));
    }

    @PostMapping(value = "/me/new", produces = "text/event-stream; charset=euc-kr")
    public Flux<NewChatResponse> createNewChat(@RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {

        Long userId = user.getAttribute("id");
        Long chatId = chatService.createNewChat(userId);
        Flux<ChatMessageResponse> chatMessageResponseFlux = chatService.addChatMessage(chatId, chatMessageRequest, userId);
//        String title = chatService.createTitle(chatId, chatMessageRequest.getContent());
        return chatMessageResponseFlux.map(response -> {
            return NewChatResponse.builder()
                    .aiResponse(response)
                    .build();
        }).concatWith(Flux.just(NewChatResponse.builder()
                        .aiResponse(TitleAIResponse.builder()
                                .chatMessageType(ChatMessageType.TITLE)
                                .title("test title")
                                .build())
                        .build(),
                NewChatResponse.builder()
                        .aiResponse(new DoneResponse())
                        .build())
        );
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
    }

    @DeleteMapping("/me/{chatId}")
    public ResponseEntity<Map<String, String>> deleteChat(@AuthenticationPrincipal OAuth2User user, @PathVariable Long chatId) {
        Long userId = user.getAttribute("id");
        chatService.deleteChat(chatId, userId);
        return ResponseEntity.ok(Map.of("message", "성공적으로 채팅 쓰레드를 삭제했습니다."));
    }

<<<<<<< HEAD
    @PostMapping("/me/new")
    public ResponseEntity<NewChatResponse> createNewChat(@RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        NewChatResponse newChatResponse = chatService.createNewChat(chatMessageRequest, userId);
        return ResponseEntity.ok(newChatResponse);
    }
=======

>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
}
