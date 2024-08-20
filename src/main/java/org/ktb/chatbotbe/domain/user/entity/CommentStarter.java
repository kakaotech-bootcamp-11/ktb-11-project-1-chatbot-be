package org.ktb.chatbotbe.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ktb.chatbotbe.global.common.BaseEntity;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentStarter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String comment;

    @ManyToOne
    private User user;

    @Builder
    public CommentStarter(String comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }
}
