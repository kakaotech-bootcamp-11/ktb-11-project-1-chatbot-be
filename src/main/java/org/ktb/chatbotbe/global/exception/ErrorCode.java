package org.ktb.chatbotbe.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_AUTHORIZED("401", "Unauthorized");

    private final String code;
    private final String message;
}