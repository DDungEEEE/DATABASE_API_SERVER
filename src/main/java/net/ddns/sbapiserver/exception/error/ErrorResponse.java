package net.ddns.sbapiserver.exception.error;

import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ErrorResponse {
    private List<ValidationError> errors;
    private String reason;
    private String divisionCode;
    private int status;

    public ErrorResponse(final ErrorCode errorCode) {
        this.reason = errorCode.getReason();
        this.divisionCode = errorCode.getDivisionCode();
        this.status = errorCode.getStatus();
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
