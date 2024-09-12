package org.ktb.chatbotbe.domain.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatHistory;
import org.ktb.chatbotbe.domain.chat.dto.service.response.strategy.ChatMessageResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageType;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatResponse;
import org.ktb.chatbotbe.domain.chat.entity.Chat;
import org.ktb.chatbotbe.domain.chat.entity.ChatMessage;
import org.ktb.chatbotbe.domain.chat.exception.UnauthorizedUserChatException;
import org.ktb.chatbotbe.domain.chat.repository.ChatMessageRepository;
import org.ktb.chatbotbe.domain.chat.repository.ChatRepository;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ChatMessageRepository chatMessageRepository;
    private final WebClient chatWebClient;

    public List<ChatResponse> findChatTitles(Long userSocialId) {
        User user = userService.findBySocialId(userSocialId);
        List<Chat> targetChats = chatRepository.findAllByUser(user);

        return targetChats.stream()
                .map(chat -> ChatResponse.builder()
                        .id(chat.getId())
                        .title(chat.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ChatHistory> findChatMessagesByChatId(Long chatId, Long userSocialId) {
        User user = userService.findBySocialId(userSocialId);
        Chat userChat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat ID"));

        // 채팅방을 만든 유저가 조회하는 게 아닌 경우
        if (!userChat.getUser().equals(user)) {
            throw new UnauthorizedUserChatException("User is not authorized to delete this chat");
        }

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatIdOrderByChatIdAsc(chatId);

        return chatMessages.stream()
                .filter(chat -> chat.getDeletedAt() == null)
                .map(message -> ChatHistory.builder()
                        .chatMessageId(message.getChatMessageId())
                        .isUser(message.getIsUser())
                        .content(message.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    public Flux<ChatMessageResponse> addChatMessage(Long chatId, ChatMessageCreateRequest chatMessageRequest, Long userSocialId) {
        StringBuilder sb = new StringBuilder();
        User user = userService.findBySocialId(userSocialId);
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat ID"));

        // 채팅방을 만든 유저가 조회하는 게 아닌 경우
        if (!chat.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not authorized to delete this chat");
        }
        String userContent = chatMessageRequest.getContent();
        ChatMessage userMessage = ChatMessage.builder()
                .chat(chat)
                .content(userContent)
                .isUser(true)
                .build();
        chatMessageRepository.save(userMessage);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("content", userContent);
        requestBody.put("user_id", user.getId());
        requestBody.put("chat_id", chatId);

        return chatWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/conv")
                        .build())
                .header("Content-Type", "application/json")
                .header("Accept", "application/stream")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .map(response -> {
                    sb.append(response);
                    return ChatMessageResponse.builder()
                            .type(ChatMessageType.MESSAGE)
                            .chatId(chatId)
                            .content(response)
                            .build();
                })
                .doOnComplete(() -> {
                            chatMessageRepository.save(
                                    ChatMessage.builder()
                                            .chat(chat)
                                            .content(sb.toString())
                                            .isUser(false)
                                            .build()
                            );
                        }
                );
    }

    public Long createNewChat(Long userSocialId) {
        User user = userService.findBySocialId(userSocialId);
        Chat chat = Chat.builder()
                .user(user)
                .title("New Chat")
                .build();

        chatRepository.save(chat);
        return chat.getId();
    }

    public void deleteChat(Long chatId, Long userSocialId) {
        User user = userService.findBySocialId(userSocialId);

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat ID"));

        if (!chat.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not authorized to delete this chat");
        }

        chat.delete(LocalDateTime.now());
        chatRepository.save(chat);
    }

    public String createChatTitle(String message, Long userId, Long chatId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("content", message);
        requestBody.put("user_id", userId);
        requestBody.put("chat_id", chatId);


        Mono<Map> responseMono = chatWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/title")      // todo 채팅 api 완성시 URL
                        .build())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class);
        String response = (String) responseMono.block().get("title");
        log.info(response);
        int maxLength = Math.min(response.length(), 100);  // 응답 길이와 100 중 작은 값 선택
        return response.substring(0, maxLength);
    }

    @Transactional
    public void saveChatTitle(Long chatId, String title) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        chat.updateTitle(title);
    }

}
