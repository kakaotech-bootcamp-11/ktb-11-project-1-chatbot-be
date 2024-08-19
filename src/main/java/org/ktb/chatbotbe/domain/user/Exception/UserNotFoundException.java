package org.ktb.chatbotbe.domain.user.Exception;

import org.ktb.chatbotbe.global.exception.ApplicationException;
import org.ktb.chatbotbe.global.exception.ErrorCode;

public class UserNotFoundException extends ApplicationException {
    private final static ErrorCode errorCode = ErrorCode.NOT_FOUND;
    public UserNotFoundException() {
        super(errorCode);
    }

    public UserNotFoundException(String message) {
        super(errorCode, message);
    }
}
