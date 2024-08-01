package org.ktb.chatbotbe.domain.chat.repository;

import org.ktb.chatbotbe.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatIdOrderByChatIdAsc(Long chatId);
}
