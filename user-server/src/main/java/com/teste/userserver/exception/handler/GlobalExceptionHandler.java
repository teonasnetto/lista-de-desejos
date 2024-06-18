package com.teste.userserver.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import com.teste.userserver.exception.ApiException;
import com.teste.userserver.exception.InvalidCredentialsException;
import com.teste.userserver.exception.NotFoundException;
import com.teste.userserver.exception.PasswordEncoderException;

import static com.teste.userserver.constants.AppConstants.ERRORS;
import static com.teste.userserver.constants.AppConstants.ERROR_CODE;
import static com.teste.userserver.constants.AppConstants.MESSAGE;
import static com.teste.userserver.constants.AppConstants.STATUS;
import static com.teste.userserver.constants.ErrorMessage.INTERNAL_SERVER_ERROR;
import static com.teste.userserver.constants.ErrorMessage.NOT_FOUND;
import static com.teste.userserver.constants.ErrorMessage.UNAUTHORIZED;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ ApiException.class, InvalidCredentialsException.class, PasswordEncoderException.class })
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        return ResponseEntity.badRequest().body(buildErrorResponse(ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(ApiException ex) {
        return ResponseEntity.badRequest().body(buildErrorResponse(ex.getMessage(),
                HttpStatus.NOT_FOUND.value(), NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerError(Exception ex) {
        var errorMessage = (ex.getMessage() != null) ? ex.getMessage() : ex.getClass().getName();
        return ResponseEntity.internalServerError().body(buildErrorResponse(errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR));
    }

    private Map<String, Object> buildErrorResponse(String message, int status, String errorCode) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();

        List<Map<String, Object>> errorList = new ArrayList<>();
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put(MESSAGE, message);
        errorMap.put(STATUS, status);
        errorMap.put(ERROR_CODE, errorCode);
        errorList.add(errorMap);

        errorAttributes.put(ERRORS, errorList);

        return errorAttributes;
    }
}
