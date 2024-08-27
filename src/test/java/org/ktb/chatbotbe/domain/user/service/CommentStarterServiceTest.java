package org.ktb.chatbotbe.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ktb.chatbotbe.domain.user.dto.CommentResponse;
import org.ktb.chatbotbe.domain.user.dto.UpdateCommentRequest;
import org.ktb.chatbotbe.domain.user.dto.UpdateResponse;
import org.ktb.chatbotbe.domain.user.entity.CommentStarter;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.CommentStarterRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CommentStarterServiceTest {
    @InjectMocks
    CommentStarterService commentStarterService;
    @Mock
    CommentStarterRepository commentStarterRepository;
    @Mock
    UserService userService;

    @DisplayName("getComments()는 ")
    @Nested
    class Context_getComments {
        @DisplayName("성공적으로 comment starter 3개를 가지고 온다")
        @Test
        void _willSuccess() {
            // given
            User mockUser = mock(User.class);

            when(commentStarterRepository.findAllByUser(any())).thenReturn(List.of(CommentStarter.builder()
                            .comment("오늘 날씨에 맞는 음식 추천해줘")
                            .user(mockUser)
                            .build(),
                    CommentStarter.builder()
                            .comment("집 어떻게 가야할지 추천해줘")
                            .user(mockUser)
                            .build(),
                    CommentStarter.builder()
                            .comment("가장 빠른 코딩테스트 날짜가 언제야?")
                            .user(mockUser)
                            .build()));
            Long userId = 33L;

            // when
            List<CommentResponse> responses = commentStarterService.getComments(userId);

            // then
            assertThat(responses).hasSize(3);
            assertThat(responses.get(0)).isNotNull();
            assertThat(responses.get(0).getComment()).isEqualTo("오늘 날씨에 맞는 음식 추천해줘");

            assertThat(responses.get(1)).isNotNull();
            assertThat(responses.get(1).getComment()).isEqualTo("집 어떻게 가야할지 추천해줘");

            assertThat(responses.get(2)).isNotNull();
            assertThat(responses.get(2).getComment()).isEqualTo("가장 빠른 코딩테스트 날짜가 언제야?");

        }
    }

    @DisplayName("updateComment()는 ")
    @Nested
    class Context_updateComment {
        @DisplayName("성공적으로 comment starter를 업데이트 한다.")
        @Test
        void _willSuccess(){
            // given
            User mockUser = mock(User.class);
            Long userId = 44L;
            Long firstCommentId = 3L;
            Long secondCommentId = 4L;

            CommentStarter firstMockCommentStarter = CommentStarter.builder()
                    .comment("오늘 날씨에 맞는 음식 추천해줘")
                    .build();
            CommentStarter secondMockCommentStarter = CommentStarter.builder()
                    .comment("가장 빠른 코딩테스트 날짜가 언제야?")
                    .build();


            when(userService.findBySocialId(any())).thenReturn(mockUser);
            when(commentStarterRepository.findByUserAndId(any(User.class), eq(firstCommentId))).thenReturn(Optional.ofNullable(firstMockCommentStarter));
            when(commentStarterRepository.findByUserAndId(any(User.class), eq(secondCommentId))).thenReturn(Optional.ofNullable(secondMockCommentStarter));

            List<UpdateCommentRequest> requests = List.of(
                    UpdateCommentRequest.builder()
                            .comment("오늘 날씨는 어때?")
                            .build(),
                    UpdateCommentRequest.builder()
                            .comment("지금 교육장까지 가면 얼마나 걸릴까?")
                            .build()
            );

            // when
            List<UpdateResponse> updateResponse = commentStarterService.updateComment(userId, requests);

            // then
            assertThat(updateResponse.size()).isEqualTo(2);
            assertThat(updateResponse.get(0)).isNotNull();
            assertThat(updateResponse.get(0).getComment()).isEqualTo("오늘 날씨는 어때?");


            assertThat(updateResponse.get(1)).isNotNull();
            assertThat(updateResponse.get(1).getComment()).isEqualTo("지금 교육장까지 가면 얼마나 걸릴까?");
        }

    }
}