package org.ktb.chatbotbe.domain.chat.repository;

import org.ktb.chatbotbe.domain.chat.entity.Chat;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatCustomRepository {
//    List<Chat> findAllByUser(User user);
}
