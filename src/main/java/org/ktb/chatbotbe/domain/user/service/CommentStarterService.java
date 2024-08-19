package org.ktb.chatbotbe.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.user.Exception.UserNotFoundException;
import org.ktb.chatbotbe.domain.user.dto.CommentResponse;
import org.ktb.chatbotbe.domain.user.dto.UpdateResponse;
import org.ktb.chatbotbe.domain.user.entity.CommentStarter;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.CommentStarterRepository;
import org.ktb.chatbotbe.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentStarterService {
    private final CommentStarterRepository commentStarterRepository;
    private final UserService userService;

    public List<CommentResponse> getComments(Long userId) {
        User user = userService.findBySocialId(userId);
        List<CommentResponse> response = commentStarterRepository.findAllByUser(user).stream()
                .map(i ->
                        CommentResponse.builder()
                                .id(i.getId())
                                .comment(i.getComment())
                                .build()
                )
                .toList();

        return response;
    }

    public UpdateResponse updateComment(Long userId, Long commentId, String comment) {
        User user = userService.findBySocialId(userId);
        CommentStarter commentStarter = commentStarterRepository.findByUserAndId(user, commentId).orElseThrow();
        commentStarter.updateComment(comment);
        return UpdateResponse.builder()
                .comment(commentStarter.getComment())
                .build();
    }
}

