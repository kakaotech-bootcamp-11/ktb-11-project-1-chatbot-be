package org.ktb.chatbotbe.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404, "Not Found"),
    NOT_AUTHORIZED(401, "Unauthorized"),
    NULL_ADDRESS(400, "Address Not Registered");

    private final int code;
    private final String message;
}
