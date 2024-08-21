package org.ktb.chatbotbe.domain.chat.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ktb.chatbotbe.domain.chat.dto.controller.request.ChatMessageCreateRequest;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatResponse;
import org.ktb.chatbotbe.domain.chat.dto.service.response.NewChatResponse;
import org.ktb.chatbotbe.domain.chat.service.ChatService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatControllerTest {
    @MockBean
    ChatService chatService;
    @Autowired
    MockMvc mockMvc;
    MockHttpSession session;
    OAuth2User mockUser;

    @BeforeEach
    void setup() {
        session = new MockHttpSession();
        mockUser = mock(OAuth2User.class);
        // Spring Security Context에 OAuth2User 설정
        PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(
                mockUser,
                null
        );
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }


    @DisplayName("채팅 이름목록 가져오기 테스트")
    @Test
    void 채팅_이름목록_가져오기() throws Exception {
        when(mockUser.getAttribute("id")).thenReturn(1L);
        when(chatService.findChatTitles(anyLong())).thenReturn(List.of(
                ChatResponse.builder()
                        .id(2L)
                        .title("테스트 타이틀")
                        .build(),
                ChatResponse.builder()
                        .id(5L)
                        .title("오늘 날씨 알려줘")
                        .build(),
                ChatResponse.builder()
                        .id(9L)
                        .title("가는데 얼마나 걸려?")
                        .build()
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/chats/me/titles")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3)) // 반환된 JSON 배열의 길이 확인
                .andExpect(jsonPath("$[0].id").value(2L)) // 첫 번째 아이템의 id 필드 검증
                .andExpect(jsonPath("$[0].title").value("테스트 타이틀")) // 첫 번째 아이템의 title 필드 검증
                .andExpect(jsonPath("$[1].id").value(5L)) // 두 번째 아이템의 id 필드 검증
                .andExpect(jsonPath("$[1].title").value("오늘 날씨 알려줘")) // 두 번째 아이템의 title 필드 검증
                .andExpect(jsonPath("$[2].id").value(9L)) // 세 번째 아이템의 id 필드 검증
                .andExpect(jsonPath("$[2].title").value("가는데 얼마나 걸려?")); // 세 번째 아이템의 title 필드 검증
    }

    @DisplayName("채팅 기록 가져오기 테스트")
    @Test
    void 채팅_기록_가져오기() throws Exception {
        when(mockUser.getAttribute("id")).thenReturn(1L);
        when(chatService.findChatMessagesByChatId(anyLong(), anyLong())).thenReturn(
                List.of(
                        ChatMessageResponse.builder()
                                .chatMessageId(1L)
                                .content("오늘 날씨 알려줘")
                                .isUser(true)
                                .build(),
                        ChatMessageResponse.builder()
                                .chatMessageId(2L)
                                .content("오늘은 비가 올 예정입니다.")
                                .isUser(false)
                                .build(),
                        ChatMessageResponse.builder()
                                .chatMessageId(3L)
                                .content("교육장까지 가는데 얼마나 걸릴까?")
                                .isUser(true)
                                .build(),
                        ChatMessageResponse.builder()
                                .chatMessageId(4L)
                                .content("약 35분 걸릴 예정입니다.")
                                .isUser(false)
                                .build()
                )
        );


        mockMvc.perform(MockMvcRequestBuilders.get("/chats/me/3")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].chatMessageId").value(1L))
                .andExpect(jsonPath("$[0].content").value("오늘 날씨 알려줘"))
                .andExpect(jsonPath("$[1].chatMessageId").value(2L))
                .andExpect(jsonPath("$[1].content").value("오늘은 비가 올 예정입니다."))
                .andExpect(jsonPath("$[2].chatMessageId").value(3L))
                .andExpect(jsonPath("$[2].content").value("교육장까지 가는데 얼마나 걸릴까?"))
                .andExpect(jsonPath("$[3].chatMessageId").value(4L))
                .andExpect(jsonPath("$[3].content").value("약 35분 걸릴 예정입니다."));
    }

    @DisplayName("채팅 메시지 보내기 테스트")
    @Test
    void 채팅_메시지_보내기() throws Exception {
        ChatMessageResponse response = ChatMessageResponse.builder()
                .chatMessageId(3L)
                .content("AI 응답")
                .isUser(false)
                .build();

        when(mockUser.getAttribute("id")).thenReturn(1L);
        when(chatService.addChatMessage(anyLong(), any(ChatMessageCreateRequest.class), anyLong())).thenReturn(response);
        String requestBody = "{\"content\": \"응답해줘\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/chats/me/3/messages")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("AI 응답"))
                .andExpect(jsonPath("$.chatMessageId").value(3L))
                .andExpect(jsonPath("$.isUser").value(false));
    }


    @DisplayName("채팅방 삭제 테스트")
    @Test
    @WithMockUser
    void 채팅방_삭제() throws Exception {
        // given
        when(mockUser.getAttribute("id")).thenReturn(1L);

        Mockito.doNothing().when(chatService).deleteChat(anyLong(), eq(1L));

        // 실제 테스트 수행
        mockMvc.perform(MockMvcRequestBuilders.delete("/chats/me/1")
                        .session(session)  // 세션 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("성공적으로 채팅 쓰레드를 삭제했습니다."));
    }

    @DisplayName("새로운 채팅방 생성 테스트")
    @Test
    void 새로운_채팅방_생성() throws Exception {
        ChatMessageResponse aiResponse = ChatMessageResponse.builder()
                .chatMessageId(2L)
                .content("AI 응답")
                .isUser(false)
                .build();
        NewChatResponse response = NewChatResponse.builder()
                .title("테스트용 채팅방")
                .chatId(1L)
                .aiResponse(aiResponse)
                .build();
        when(mockUser.getAttribute("id")).thenReturn(1L);
        when(chatService.createNewChat(any(), anyLong())).thenReturn(response);

        String requestBody = "{\"content\": \"응답해줘\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/chats/me/new")
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트용 채팅방"))
                .andExpect(jsonPath("$.chatId").value(1L))
                .andExpect(jsonPath("$.aiResponse.chatMessageId").value(2L))
                .andExpect(jsonPath("$.aiResponse.content").value("AI 응답"))
                .andExpect(jsonPath("$.aiResponse.isUser").value(false))
                .andDo(print());
    }
}