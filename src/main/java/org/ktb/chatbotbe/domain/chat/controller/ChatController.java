package org.ktb.chatbotbe.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
import org.ktb.chatbotbe.domain.chat.dto.service.response.*;
import org.ktb.chatbotbe.domain.chat.dto.service.response.strategy.ChatMessageResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.strategy.DoneResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.strategy.NewChatResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.strategy.TitleAIResponse;
import org.ktb.chatbotbe.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public ResponseEntity<List<ChatHistory>> getChatMessages(@PathVariable Long chatId, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        List<ChatHistory> messages = chatService.findChatMessagesByChatId(chatId, userId);

        return ResponseEntity.ok(messages);
    }

    @PostMapping(value = "/me/{chatId}/messages", produces = "text/event-stream; charset=euc-kr")
    public Flux<ServerSentEvent<NewChatResponse>> addChatMessage(@PathVariable Long chatId, @RequestBody ChatMessageCreateRequest chatMessageRequest, @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        Flux<ChatMessageResponse> responseFlux = chatService.addChatMessage(chatId, chatMessageRequest, userId);
        return responseFlux
                .map(response -> {
                    return NewChatResponse.builder()
                            .aiResponse(response)
                            .build();
                }).map(next -> ServerSentEvent.<NewChatResponse>builder().data(next).build())
                .concatWith(Mono.just(ServerSentEvent.builder(NewChatResponse.builder()
                                .aiResponse(new DoneResponse())
                                .build())
                        .build()
                ));
    }


    @PostMapping(value = "/me/new", produces = "text/event-stream; charset=euc-kr")
    public Flux<ServerSentEvent<NewChatResponse>> createNewChat(@RequestBody ChatMessageCreateRequest chatMessageRequest,
                                                                @AuthenticationPrincipal OAuth2User user) {
        Long userId = user.getAttribute("id");
        Long chatId = chatService.createNewChat(userId);
        Flux<ChatMessageResponse> chatMessageResponseFlux = chatService.addChatMessage(chatId, chatMessageRequest, userId);
        String title = chatService.createChatTitle(chatMessageRequest.getContent(), userId, chatId);

        // 기존 응답을 NewChatResponse로 변환 후 ServerSentEvent로 래핑
        Flux<ServerSentEvent<NewChatResponse>> eventFlux = chatMessageResponseFlux
                .map(response -> NewChatResponse.builder()
                        .aiResponse(response)
                        .build())
                .map(next -> ServerSentEvent.<NewChatResponse>builder()
                        .data(next)
                        .build());

        // 추가 응답 데이터들을 각각 ServerSentEvent로 래핑하여 연결
        ServerSentEvent<NewChatResponse> titleResponse = ServerSentEvent.<NewChatResponse>builder()
                .data(NewChatResponse.builder()
                        .aiResponse(TitleAIResponse.builder()
                                .chatMessageType(ChatMessageType.TITLE)
                                .title(title)
                                .build())
                        .build())
                .build();

        ServerSentEvent<NewChatResponse> doneResponse = ServerSentEvent.<NewChatResponse>builder()
                .data(NewChatResponse.builder()
                        .aiResponse(new DoneResponse())
                        .build())
                .build();

        chatService.saveChatTitle(chatId, title);
        // 기존 Flux와 추가 데이터들을 결합하여 반환
        return eventFlux.concatWith(Flux.just(titleResponse, doneResponse));
    }


    @DeleteMapping("/me/{chatId}")
    public ResponseEntity<Map<String, String>> deleteChat(@AuthenticationPrincipal OAuth2User user, @PathVariable Long chatId) {
        Long userId = user.getAttribute("id");
        chatService.deleteChat(chatId, userId);
        return ResponseEntity.ok(Map.of("message", "성공적으로 채팅 쓰레드를 삭제했습니다."));
    }


}
