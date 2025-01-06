package net.ddns.sbapiserver.common.response;

import lombok.Builder;
import lombok.Getter;
import net.ddns.sbapiserver.common.code.SuccessCode;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public class ResultResponse<T> implements Serializable {

    // API 응답 결과
    private T result;

    // API 응답 코드
    private int resultCode;

    // API 응답 메시지
    private String resultMessage;


    @Builder(builderMethodName = "successResponse")
    public ResultResponse(final T result, final SuccessCode successCode){
        this.result = result;
        if(successCode != null){
            this.resultCode = successCode.getStatus();
            this.resultMessage = successCode.getMessage();
        }
    }
}
