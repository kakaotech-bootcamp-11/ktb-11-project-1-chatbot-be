
//package org.ktb.chatbotbe.domain.chat.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
//import org.ktb.chatbotbe.domain.chat.dto.service.response.strategy.ChatMessageResponse;
//import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatResponse;
//import org.ktb.chatbotbe.domain.chat.dto.service.response.strategy.NewChatResponse;
//import org.ktb.chatbotbe.domain.chat.entity.Chat;
//import org.ktb.chatbotbe.domain.chat.entity.ChatMessage;
//import org.ktb.chatbotbe.domain.chat.exception.UnauthorizedUserChatException;
//import org.ktb.chatbotbe.domain.chat.repository.ChatMessageRepository;
//import org.ktb.chatbotbe.domain.chat.repository.ChatRepository;
//import org.ktb.chatbotbe.domain.user.entity.User;
//import org.ktb.chatbotbe.domain.user.repository.UserRepository;
//import org.ktb.chatbotbe.domain.user.service.UserService;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//@Slf4j
//class ChatServiceTest {
//    @Mock
//    ChatRepository chatRepository;
//    @Mock
//    ChatMessageRepository chatMessageRepository;
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    UserService userService;
//    @InjectMocks
//    ChatService chatService;
//    @Spy
//    @InjectMocks
//    ChatService spyChatService;
//
//    @DisplayName("findChatTitles()는 ")
//    @Nested
//    class Context_findChatTitles {
//        @DisplayName("채팅제목을 성공적으로 가져온다.")
//        @Test
//        void _willSuccess() {
//            // given
//            User user = mock(User.class);
//            Chat chat1 = Chat.builder()
//                    .user(user)
//                    .title("test1")
//                    .build();
//
//            Chat chat2 = Chat.builder()
//                    .user(user)
//                    .title("test2")
//                    .build();
//
//            when(userService.findBySocialId(anyLong())).thenReturn(user);
//            when(chatRepository.findAllByUser(user)).thenReturn(List.of(chat1, chat2));
//
//            // when
//            List<ChatResponse> responses = chatService.findChatTitles(1L);
//
//            // then
//            assertEquals(responses.size(), 2);
//            assertEquals(responses.get(0).title(), chat1.getTitle());
//            assertEquals(responses.get(1).title(), chat2.getTitle());
//        }
//
//        @DisplayName("삭제된 채팅은 가져오지 않는다.")
//        @Test
//        void _shouldNotReturnDeletedChats() {
//            // given
//            User user = mock(User.class);
//            Chat chat1 = Chat.builder()
//                    .user(user)
//                    .title("test1")
//                    .build();
//
//            Chat chat2 = Chat.builder()
//                    .user(user)
//                    .title("test2")
//                    .build();
//
//            chat2.delete(LocalDateTime.now());
//
//            when(userService.findBySocialId(anyLong())).thenReturn(user);
//            when(chatRepository.findAllByUser(user)).thenReturn(List.of(chat1));
//
//            // when
//            List<ChatResponse> responses = chatService.findChatTitles(1L);
//
//            // then
//            assertEquals(responses.size(), 1);
//            assertEquals(responses.get(0).title(), chat1.getTitle());
//        }
//    }
//
//    @DisplayName("findChatMessagesByChatId()는 ")
//    @Nested
//    class Context_findChatMessageByChatId {
//
////        @DisplayName("채팅메세지를 성공적으로 가져온다")
////        @Test
////        void _willSuccess() {
////            // given
////            Long chatId = 1L;
////            Long userSocialId = 3L;
////            User chatUser = mock(User.class);
////            Chat chat = mock(Chat.class);
////            List<ChatMessage> chatMessages = List.of(
////                    ChatMessage.builder()
////                            .chatMessageId(1L)
////                            .chat(chat)
////                            .content("안녕하세요! 일단 테스트입니다")
////                            .isUser(true)
////                            .build(),
////                    ChatMessage.builder()
////                            .chatMessageId(2L)
////                            .chat(chat)
////                            .content("안녕하세요! AI입니다")
////                            .isUser(false)
////                            .build());
////
////            when(userService.findBySocialId(anyLong())).thenReturn(chatUser);
////            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
////            when(chat.getUser()).thenReturn(chatUser);
////            when(chatMessageRepository.findAllByChatIdOrderByChatIdAsc(anyLong())).thenReturn(chatMessages);
////
////            // when
////            List<ChatMessageResponse> responses = chatService.findChatMessagesByChatId(chatId, userSocialId);
////
////            // then
////            assertEquals(2, responses.size());
////            assertEquals(1L, responses.get(0).getChatMessageId());
////            assertEquals("안녕하세요! 일단 테스트입니다", responses.get(0).getContent());
////            assertTrue(responses.get(0).getIsUser());
////
////            assertEquals(2L, responses.get(1).getChatMessageId());
////            assertEquals("안녕하세요! AI입니다", responses.get(1).getContent());
////            assertFalse(responses.get(1).getIsUser());
////
////        }
//
//        @DisplayName("채팅방을 만든 유저가 조회하는 게 아닌 경우 UnauthorizedUserChatException()을 던진다")
//        @Test
//        void itShouldThrowUnauthorizedUserChatExceptionIfUserIsNotCreator() {
//            // given
//            User chatUser = User.builder()
//                    .nickname("Chatted User")
//                    .socialId(1234L)
//                    .build();
//
//            long notChattedUserSocialId = 4321L;
//            User notChatUser = User.builder()
//                    .nickname("Not Chatted User")
//                    .socialId(notChattedUserSocialId)
//                    .build();
//
//            Long chatId = 1L;
//            Chat chat = Chat.builder()
//                    .title("test title")
//                    .user(chatUser)
//                    .build();
//
//            // when
//            when(userService.findBySocialId(notChattedUserSocialId)).thenReturn(notChatUser);
//            when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
//
//            // then
//            assertThrows(UnauthorizedUserChatException.class, () -> {
//                chatService.findChatMessagesByChatId(chatId, notChattedUserSocialId);
//            });
//        }
//
//    }
//
//    @DisplayName("addChatMessage()는 ")
//    @Nested
//    class Context_addChatMessage {
//        @DisplayName("성공적으로 메시지를 추가한다.")
//        @Test
//        void _willSuccess() {
//            // given
//            Long chatId = 1L;
//            Long userSocialId = 3L;
//            String userComment = "응답해줘";
//            ChatMessageCreateRequest chatMessageRequest = new ChatMessageCreateRequest();
//            chatMessageRequest.setContent(userComment);
//
//            User user = mock(User.class);
//            Chat chat = mock(Chat.class);
//            when(userService.findBySocialId(anyLong())).thenReturn(user);
//            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
//            when(chat.getUser()).thenReturn(user);
//            when(chat.getUser().getId()).thenReturn(1L);
//            when(user.getId()).thenReturn(1L);
//
////            doReturn("ai response").when(spyChatService).callAiServer(userComment);
//
//            // when
////            ChatMessageResponse response = spyChatService.addChatMessage(chatId, chatMessageRequest, userSocialId);
//
//            // then
////            assertNotNull(response);
////            assertEquals(response.getIsUser(), false);
////            assertEquals(response.getContent(), "ai response");
//
//        }
//    }
//
////    @DisplayName("createNewChat()은 ")
////    @Nested
////    class Context_CreateNewChat {
////        @DisplayName("성공적으로 새로운 채팅방을 만든다")
////        @Test
////        void _willSuccess() {
////            // given
////            Long userSocialId = 3L;
////            String userComment = "test content";
////
////            ChatMessageCreateRequest chatMessageRequest = new ChatMessageCreateRequest();
////            chatMessageRequest.setContent(userComment);
////            User user = mock(User.class);
////            Chat chat = mock(Chat.class);
////
////            when(userService.findBySocialId(anyLong())).thenReturn(user);
////
////            doReturn("ai response").when(spyChatService).callAiServer(userComment);
////
////            // when
////            NewChatResponse response = spyChatService.createNewChat(chatMessageRequest, userSocialId);
////
////            // then
////            assertEquals(response.getTitle(), "ai title");
////            assertEquals(response.getAiResponse().getContent(), "ai response");
////            assertEquals(response.getAiResponse().getIsUser(), false);
////        }
////    }
//
//    @DisplayName("deleteChat()은 ")
//    @Nested
//    class Context_deleteChat {
//        @DisplayName("성공적으로 채팅방을 삭제한다.")
//        @Test
//        void _willSuccess() {
//            // given
//
//            // when
//
//            // then
//        }
//
//        @DisplayName("사용자가 다른경우 IllegalArgumentException을 던진다")
//        @Test
//        void itShouldThrowIllegalArgumentException() {
//            // given
//            Chat chat = mock(Chat.class);
//            User chatUser = mock(User.class);
//            User user = mock(User.class);
//            Long chatId = 1L;
//            Long userSocialId = 3L;
//
//            when(userService.findBySocialId(anyLong())).thenReturn(user);
//            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
//            when(chat.getUser()).thenReturn(chatUser);
//            when(chat.getUser().getId()).thenReturn(1L);
//            when(user.getId()).thenReturn(3L);
//
//            // when
//            // then
//            assertThrows(IllegalArgumentException.class, () -> {
//                chatService.deleteChat(chatId, userSocialId);
//            });
//
//        }
//    }
//}
