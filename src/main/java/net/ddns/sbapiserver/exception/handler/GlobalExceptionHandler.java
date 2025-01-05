package net.ddns.sbapiserver.exception.handler;

import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.exception.error.custom.UserNotValidException;
import net.ddns.sbapiserver.exception.error.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.List;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final HttpStatus HTTP_NOT_FOUND_ERROR = HttpStatus.NOT_FOUND;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        BindingResult bindingResult = ex.getBindingResult();
        List<ErrorResponse.ValidationError> fieldErrors = ErrorResponse.ValidationError.of(bindingResult);

        return new ResponseEntity<>(fieldErrors, HTTP_STATUS_OK);
    }

    @ExceptionHandler(UserNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotValidException(UserNotValidException ex){
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex){
        String message = ex.getMessage().toString();
        return new ResponseEntity<>(message, HTTP_NOT_FOUND_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex){
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class, SQLException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleException(Exception e){
        log.error(e.toString(), e);
        return new ResponseEntity<>(e.getMessage().toString(), HTTP_STATUS_OK);
    }

}
