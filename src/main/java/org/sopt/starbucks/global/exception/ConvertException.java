package org.sopt.starbucks.global.exception;

public class ConvertException extends BusinessException {
    public ConvertException(ErrorCode errorCode) {
        super(errorCode);
    }
}
