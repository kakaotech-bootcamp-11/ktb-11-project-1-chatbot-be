package org.ktb.chatbotbe.domain.user.repository;

import org.ktb.chatbotbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialId(Long socialId);
}
