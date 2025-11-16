package org.sopt.starbucks.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.fail(errorCode.getCode(), errorCode.getMsg());

        log.warn(errorCode.getMsg(), e);

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {

        ApiResponse<Void> response = ApiResponse.fail(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                ErrorCode.INTERNAL_SERVER_ERROR.getMsg()
        );

        log.error("Unhandled Exception: {}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
