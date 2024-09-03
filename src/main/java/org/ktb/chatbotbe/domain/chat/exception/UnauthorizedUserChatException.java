package org.ktb.chatbotbe.domain.chat.exception;

import org.ktb.chatbotbe.global.exception.ApplicationException;
import org.ktb.chatbotbe.global.exception.ErrorCode;

public class UnauthorizedUserChatException extends ApplicationException {
    private final static ErrorCode errorCode = ErrorCode.NOT_AUTHORIZED;

    public UnauthorizedUserChatException() {
        super(errorCode);
    }

    public UnauthorizedUserChatException(String message) {
        super(errorCode, message);
    }
}
