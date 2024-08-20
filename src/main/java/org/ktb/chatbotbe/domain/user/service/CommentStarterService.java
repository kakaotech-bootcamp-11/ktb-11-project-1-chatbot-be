package org.ktb.chatbotbe.domain.user.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.user.dto.CommentResponse;
import org.ktb.chatbotbe.domain.user.dto.UpdateCommentRequest;
import org.ktb.chatbotbe.domain.user.dto.UpdateResponse;
import org.ktb.chatbotbe.domain.user.entity.CommentStarter;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.ktb.chatbotbe.domain.user.repository.CommentStarterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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

    public List<UpdateResponse> updateComment(Long userId, List<UpdateCommentRequest> requests) {
        User user = userService.findBySocialId(userId);

        return requests.stream()
                .map(request -> {
                    CommentStarter commentStarter = commentStarterRepository
                            .findByUserAndId(user, request.getId())
                            .orElseThrow(() -> new EntityNotFoundException("CommentStarter not found for id: " + request.getId()));

                    commentStarter.updateComment(request.getComment());

                    return UpdateResponse.builder()
                            .id(commentStarter.getId())
                            .comment(commentStarter.getComment())
                            .build();
                })
                .collect(Collectors.toList());
    }

}

