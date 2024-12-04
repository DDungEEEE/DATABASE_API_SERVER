package net.ddns.sbapiserver.exception.custom;

import lombok.Getter;
import net.ddns.sbapiserver.common.code.ErrorCode;

@Getter
public class UserNotValidException extends RuntimeException{
    private final ErrorCode errorCode;

    public UserNotValidException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
