package org.ktb.chatbotbe.domain.chat.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.chat.entity.Chat;
import org.ktb.chatbotbe.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.ktb.chatbotbe.domain.chat.entity.QChat.chat;

@Repository
@RequiredArgsConstructor
public class ChatCustomRepositoryImpl implements ChatCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Chat> findAllByUser(User user) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(chat.deletedAt.isNull());
        return jpaQueryFactory.selectFrom(chat)
                .where(builder)
                .orderBy(chat.createdAt.desc())
                .fetch();
    }
}
