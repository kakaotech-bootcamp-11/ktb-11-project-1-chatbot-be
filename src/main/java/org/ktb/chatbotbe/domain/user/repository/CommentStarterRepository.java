package org.ktb.chatbotbe.domain.user.repository;


import org.ktb.chatbotbe.domain.user.entity.CommentStarter;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentStarterRepository extends JpaRepository<CommentStarter, Long> {
    List<CommentStarter> findAllByUser(User user);
    Optional<CommentStarter> findByUserAndId(User user, Long id);
}
