package org.ktb.chatbotbe.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
<<<<<<< HEAD

@RestControllerAdvice
public class GlobalExceptionHandler {
=======
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(Exception ex, WebRequest request) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }

>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException e) {
        return ErrorResponse.toResponseEntity(e);
    }
}
