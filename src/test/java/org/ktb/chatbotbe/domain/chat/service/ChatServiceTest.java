package org.ktb.chatbotbe.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ktb.chatbotbe.domain.chat.entity.Chat;
import org.ktb.chatbotbe.domain.chat.exception.UnauthorizedUserChatException;
import org.ktb.chatbotbe.domain.chat.repository.ChatMessageRepository;
import org.ktb.chatbotbe.domain.chat.repository.ChatRepository;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.ktb.chatbotbe.domain.user.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

        @DisplayName("성공적으로 ")
        @Test
        void _willSuccess() {

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
