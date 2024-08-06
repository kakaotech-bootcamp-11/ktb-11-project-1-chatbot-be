package org.ktb.chatbotbe.global.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponse {
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ApplicationException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().getMessage())
                        .message(ex.getMessage())
                        .build());
    }
}
