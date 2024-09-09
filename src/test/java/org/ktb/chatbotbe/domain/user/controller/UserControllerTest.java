//package org.ktb.chatbotbe.domain.user.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.ktb.chatbotbe.domain.user.entity.User;
//import org.ktb.chatbotbe.domain.user.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.mockito.ArgumentMatchers.startsWith;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class UserControllerTest {
//    @MockBean
//    UserService userService;
//    @Autowired
//    MockMvc mockMvc;
//    MockHttpSession session;
//    OAuth2User mockUser;
//
//    @BeforeEach
//    void setup() {
//        session = new MockHttpSession();
//        mockUser = mock(OAuth2User.class);
//        // Spring Security Context에 OAuth2User 설정
//        PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(
//                mockUser,
//                null
//        );
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(auth);
//        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
//    }
//
//
//    @DisplayName("사용자 주소 업데이트")
//    @Test
//    void 주소_업데이트() throws Exception {
//        // given
//        User user = mock(User.class);
//
//
//        // then
//        mockMvc.perform(MockMvcRequestBuilders.post("/setting/address")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
////                        .content()
//                )
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//}