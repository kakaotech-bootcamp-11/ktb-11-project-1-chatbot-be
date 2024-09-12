//package org.ktb.chatbotbe.domain.chat.repository;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.ktb.chatbotbe.domain.chat.entity.Chat;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class ChatRepositoryTest {
//    @Autowired
//    private ChatRepository chatRepository;
//    @Autowired
//    private JPAQueryFactory jpaQueryFactory;
//
//    @TestConfiguration
//    static class QuerydslTestConfig {
//        @PersistenceContext
//        private EntityManager entityManager;
//
//        @Bean
//        public JPAQueryFactory jpaQueryFactory() {
//            return new JPAQueryFactory(entityManager);
//        }
//    }
//
//    private Chat savedChat;
//
//
//    @BeforeEach
//    void setUp() {
//        // Chat 객체를 생성하여 저장
//        Chat chat = Chat.builder()
//                .title("title")
//                .build();
//        savedChat = chatRepository.save(chat);
//    }
//
//    @Test
//    @DisplayName("성공적으로 findById를 통해 Chat을 가져온다")
//    void findById_notDeletedChat_shouldReturnChat() {
//        // When
//        Optional<Chat> foundChat = chatRepository.findById(savedChat.getId());
//
//        // Then
//        assertThat(foundChat)
//                .isPresent()
//                .hasValueSatisfying(chat -> assertThat(chat.getTitle()).isEqualTo(savedChat.getTitle()));
//    }
//
//    @Test
//    @DisplayName("soft delete된 Chat을 가져오지 않는다")
//    void findById_deletedChat_shouldReturnEmpty() {
//        // Given
//        performSoftDelete(savedChat);
//
//        // When
//        Optional<Chat> foundChat = chatRepository.findById(savedChat.getId());
//
//        // Then
//        assertThat(foundChat).isNotPresent();
//    }
//
//    private Chat createAndSaveChat(String title) {
//        Chat chat = Chat.builder()
//                .title(title)
//                .build();
//        return chatRepository.save(chat);
//    }
//
//    private void performSoftDelete(Chat chat) {
//        chat.delete(LocalDateTime.now());
//        chatRepository.save(chat);
//    }
//
//}