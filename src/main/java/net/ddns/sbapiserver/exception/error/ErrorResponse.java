package net.ddns.sbapiserver.exception.error;

import lombok.*;
import net.ddns.sbapiserver.common.code.ErrorCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ErrorResponse {
    private List<ValidationError> errors; // Validation 검증 오류
    private String reason; // ErrorCode 이유
    private String divisionCode; // ErrorCode 구분 코드
    private int status; // Error 상태 코드
    private String reasonMsg; // ErrorCode 상세화 -> 구체적인 이유

    public ErrorResponse(final ErrorCode errorCode) {
        this.reason = errorCode.getReason();
        this.divisionCode = errorCode.getDivisionCode();
        this.status = errorCode.getStatus();
    }

    @Builder
    public ErrorResponse(final ErrorCode errorCode, String reasonMsg){
        this.reason = errorCode.getReason();
        this.status = errorCode.getStatus();
        this.divisionCode = errorCode.getDivisionCode();
        this.reasonMsg = reasonMsg;
    }
    @Builder
    public ErrorResponse(List<ValidationError> errors) {
        this.errors = errors;
    }

    /**
     * MethodArgumentValidationException 발생 시
     * BindingResult를 매개변수로 받아 ErrorResponse로 변환시켜주는 클래스
     */
    @Getter
    @RequiredArgsConstructor
    public static class ValidationError{
        private final String field;
        private final String value;
        private final String reason;


        public static List<ValidationError> of(final BindingResult bindingResult){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ValidationError> errors = fieldErrors.stream()
                    .map(error -> new ValidationError(error.getField(),
                            error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());

            return errors;
        }
    }



}
