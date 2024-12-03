package net.ddns.sbapiserver.common.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    SELECT_SUCCESS(200, "200","SELECT_SUCCESS"),

    LOGIN_SUCCESS(200, "200", "LOGIN_SUCCESS"),

    DELETE_SUCCESS(200, "200", "DELETE_SUCCESS"),

    INSERT_SUCCESS(201, "201", "INSERT_SUCCESS"),

    UPDATE_SUCCESS(200, "200", "UPDATE_SUCCESS");

    private final int  status;

    private final String code;

    private final String message;

    SuccessCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
