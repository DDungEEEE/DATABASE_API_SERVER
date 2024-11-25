package net.ddns.sbapiserver.exception.custom;

import lombok.Getter;
import net.ddns.sbapiserver.exception.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
