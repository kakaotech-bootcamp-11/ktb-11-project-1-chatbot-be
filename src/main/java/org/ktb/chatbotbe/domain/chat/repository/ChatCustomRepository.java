package org.ktb.chatbotbe.domain.chat.repository;

import org.ktb.chatbotbe.domain.chat.entity.Chat;
import org.ktb.chatbotbe.domain.user.entity.User;

import java.util.List;

public interface ChatCustomRepository {
    List<Chat> findAllByUser(User user);
}
