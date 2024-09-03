package org.ktb.chatbotbe.domain.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
<<<<<<< HEAD
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.NewChatResponse;
=======
import org.ktb.chatbotbe.domain.chat.dto.service.response.*;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
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

import java.time.LocalDateTime;
import java.util.List;
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

    public List<ChatMessageResponse> findChatMessagesByChatId(Long chatId, Long userSocialId) {
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
                .map(message -> ChatMessageResponse.builder()
<<<<<<< HEAD
                        .chatMessageId(message.getChatMessageId())
                        .content(message.getContent())
                        .isUser(message.getIsUser())
=======
                        .content(message.getContent())
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
                        .build())
                .collect(Collectors.toList());
    }

<<<<<<< HEAD
    public ChatMessageResponse addChatMessage(Long chatId, ChatMessageCreateRequest chatMessageRequest, Long userSocialId) {
=======
    public Flux<ChatMessageResponse> addChatMessage(Long chatId, ChatMessageCreateRequest chatMessageRequest, Long userSocialId) {
        StringBuilder sb = new StringBuilder();
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
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

<<<<<<< HEAD

        String aiResponseContent = callAiServer(userContent);

        // AI 응답 저장
        ChatMessage aiMessage = ChatMessage.builder()
                .chat(chat)
                .content(aiResponseContent)
                .isUser(false)
                .build();
        chatMessageRepository.save(aiMessage);

        // AI 응답 반환
        return ChatMessageResponse.builder()
                .chatMessageId(aiMessage.getChatMessageId())
                .content(aiResponseContent)
                .isUser(false)
                .build();
    }


    public NewChatResponse createNewChat(ChatMessageCreateRequest chatMessageRequest, Long userSocialId) {
        User user = userService.findBySocialId(userSocialId);
        Chat newChat = Chat.builder()
                .user(user)
                .title("New Chat")
                .build();
        chatRepository.save(newChat);

        // 사용자의 첫 번째 메시지 생성 및 저장
        String userContent = chatMessageRequest.getContent();
        ChatMessage userMessage = ChatMessage.builder()
                .chat(newChat)
                .content(userContent)
                .isUser(true)
                .build();
        chatMessageRepository.save(userMessage);

        // AI 서버 호출 (지금은 dummy response)
        String aiResponseContent = callAiServer(userContent);
//        String aiResponseContent = "test";

        // todo AI 타이틀 연결하기
        String chatTitle = "ai title";
//        String chatTitle = callAiServer(aiResponseContent);
        // AI 응답 메시지 생성 및 저장
        ChatMessage aiMessage = ChatMessage.builder()
                .chat(newChat)
                .content(aiResponseContent)
                .isUser(false)
                .build();
        chatMessageRepository.save(aiMessage);

        // Chat의 제목을 AI 응답으로 업데이트
        newChat.updateTitle(aiResponseContent);
        chatRepository.save(newChat);

        return NewChatResponse.builder()
                .title(chatTitle)
                .chatId(newChat.getId())
                .aiResponse(ChatMessageResponse.builder()
                        .chatMessageId(aiMessage.getChatMessageId())
                        .content(aiResponseContent)
                        .isUser(false)
                        .build())
                .build();
=======
        return chatWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/test/stream")
                        .build())
                .header("Content-Type", "application/json")
                .header("Accept", "application/stream")
                .bodyValue(String.format("{\"content\":\"%s\"}", userContent))
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

    public Long createNewChat(Long userSocialId){
        User user = userService.findBySocialId(userSocialId);
        Chat chat = Chat.builder()
                .user(user)
                .title("New Chat")
                .build();

        chatRepository.save(chat);
        return chat.getId();
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
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

<<<<<<< HEAD
    protected String callAiServer(String message) {
        Flux<String> responseFlux = chatWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/test")
=======
    protected String createChatTitle(String message) {
        Flux<String> responseFlux = chatWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/test")      // todo 채팅 api 완성시 URL
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
                        .build())
                .header("Content-Type", "application/json")
                .bodyValue(String.format("{\"content\":\"%s\"}", message))
                .retrieve().bodyToFlux(String.class);
<<<<<<< HEAD

        return responseFlux.blockLast();        // 동기적으로 사용
=======
        String response = responseFlux.blockLast();
        int maxLength = Math.min(response.length(), 100);  // 응답 길이와 100 중 작은 값 선택
        return response.substring(0, maxLength);
    }

    public String createTitle(Long chatId, String userMessage) {
        // todo
        // ai 서버에 제목 요청
        // 제목 DB에 저장
        // return
        return "test";
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
    }
}
