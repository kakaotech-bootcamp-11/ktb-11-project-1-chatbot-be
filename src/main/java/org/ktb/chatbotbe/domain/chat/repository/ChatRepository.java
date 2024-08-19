package org.ktb.chatbotbe.domain.chat.repository;

import org.ktb.chatbotbe.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatCustomRepository {
    @Query(value = "select * from chat where id = ?1 and deleted_at IS NULL", nativeQuery = true)
    Optional<Chat> findById(Long id);
}
