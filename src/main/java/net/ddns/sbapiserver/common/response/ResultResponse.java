package net.ddns.sbapiserver.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Builder(builderMethodName = "dataResponse")
public class ResultResponse<T> implements Serializable {
    private T result;

    private HttpStatus resultCode;

    private String resultMessage;
}
