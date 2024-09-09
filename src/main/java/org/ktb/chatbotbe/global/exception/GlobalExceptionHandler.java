package org.ktb.chatbotbe.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.toResponseEntity(e);
    }
}
