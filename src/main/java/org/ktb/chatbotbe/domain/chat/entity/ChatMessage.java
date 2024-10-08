package org.ktb.chatbotbe.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ktb.chatbotbe.global.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne
    @JoinColumn(name = "chat_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Chat chat;


    @Column(columnDefinition = "TEXT", length = 1000)
    private String content;

    private Boolean isUser;

    @Builder
    private ChatMessage(Long chatMessageId, Chat chat, String content, Boolean isUser) {
        this.chatMessageId = chatMessageId;
        this.chat = chat;
        this.content = content;
        this.isUser = isUser;
    }
}
