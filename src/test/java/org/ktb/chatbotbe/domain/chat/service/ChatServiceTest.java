package org.ktb.chatbotbe.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageResponse;
import org.ktb.chatbotbe.domain.chat.entity.Chat;
import org.ktb.chatbotbe.domain.chat.entity.ChatMessage;
import org.ktb.chatbotbe.domain.chat.exception.UnauthorizedUserChatException;
import org.ktb.chatbotbe.domain.chat.repository.ChatMessageRepository;
import org.ktb.chatbotbe.domain.chat.repository.ChatRepository;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.ktb.chatbotbe.domain.user.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Slf4j
class ChatServiceTest {
    @Mock
    ChatRepository chatRepository;
    @Mock
    ChatMessageRepository chatMessageRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @InjectMocks
    ChatService chatService;

    @DisplayName("findChatMessagesByChatId()는 ")
    @Nested
    class Context_findChatMessageByChatId {

        @DisplayName("채팅메세지를 성공적으로 가져온다")
        @Test
        void _willSuccess() {
            // given
            Long chatId = 1L;
            Long userSocialId = 3L;
            User chatUser = mock(User.class);
            Chat chat = mock(Chat.class);
            List<ChatMessage> chatMessages = List.of(
                    ChatMessage.builder()
                            .chatMessageId(1L)
                            .chat(chat)
                            .content("안녕하세요! 일단 테스트입니다")
                            .isUser(true)
                            .build()
                    , ChatMessage.builder()
                            .chatMessageId(2L)
                            .chat(chat)
                            .content("안녕하세요! AI입니다")
                            .isUser(false)
                            .build()
            );
            when(userService.findBySocialId(anyLong())).thenReturn(chatUser);
            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
            when(chat.getUser()).thenReturn(chatUser);
            when(chatMessageRepository.findAllByChatIdOrderByChatIdAsc(anyLong())).thenReturn(chatMessages);

            // when
            List<ChatMessageResponse> responses = chatService.findChatMessagesByChatId(chatId, userSocialId);

            // then
            assertEquals(2, responses.size());
            assertEquals(1L, responses.get(0).getChatMessageId());
            assertEquals("안녕하세요! 일단 테스트입니다", responses.get(0).getContent());
            assertTrue(responses.get(0).getIsUser());

            assertEquals(2L, responses.get(1).getChatMessageId());
            assertEquals("안녕하세요! AI입니다", responses.get(1).getContent());
            assertFalse(responses.get(1).getIsUser());

        }

        @DisplayName("채팅방을 만든 유저가 조회하는 게 아닌 경우 UnauthorizedUserChatException()을 던진다")
        @Test
        void itShouldThrowUnauthorizedUserChatExceptionIfUserIsNotCreator() {
            // given
            User chatUser = User.builder()
                    .nickname("Chatted User")
                    .socialId(1234L)
                    .build();

            long notChattedUserSocialId = 4321L;
            User notChatUser = User.builder()
                    .nickname("Not Chatted User")
                    .socialId(notChattedUserSocialId)
                    .build();

            Long chatId = 1L;
            Chat chat = Chat.builder()
                    .id(chatId)
                    .title("test title")
                    .user(chatUser)
                    .build();

            // when
            when(userService.findBySocialId(notChattedUserSocialId)).thenReturn(notChatUser);
            when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

            // then
            assertThrows(UnauthorizedUserChatException.class, () -> {
                chatService.findChatMessagesByChatId(chatId, notChattedUserSocialId);
            });
        }

    }
}
