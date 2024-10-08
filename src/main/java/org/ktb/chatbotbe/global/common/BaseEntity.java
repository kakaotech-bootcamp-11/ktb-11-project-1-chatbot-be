package org.ktb.chatbotbe.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    public void delete(LocalDateTime currentTime){
        if (deletedAt == null){
            deletedAt = currentTime;
        }
    }

    public boolean isDeleted(){
        return deletedAt != null;
    }
}
