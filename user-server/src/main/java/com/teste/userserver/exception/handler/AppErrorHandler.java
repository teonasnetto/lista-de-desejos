package com.teste.userserver.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import static com.teste.userserver.constants.AppConstants.ERROR;
import static com.teste.userserver.constants.AppConstants.ERRORS;
import static com.teste.userserver.constants.AppConstants.MESSAGE;
import static com.teste.userserver.constants.AppConstants.STATUS;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppErrorHandler extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        var errorAttributes = super.getErrorAttributes(request, options);
        var error = getError(request);

        var errorList = new ArrayList<Map<String, Object>>();
        determineHttpStatusAndBuildErrorList(error, errorList, errorAttributes);

        errorAttributes.put(ERRORS, errorList);
        return errorAttributes;
    }

    private void determineHttpStatusAndBuildErrorList(Throwable error, List<Map<String, Object>> errorList,
            Map<String, Object> errorAttributes) {
        if (error instanceof ExpiredJwtException expiredJwtException) {
            handleExpiredJwtException(expiredJwtException, errorList);
        } else if (error instanceof MalformedJwtException malformedJwtException) {
            handleMalformedJwtException(malformedJwtException, errorList);
        } else {
            handleInternalServerError(error, errorList);
        }
    }

    private void handleExpiredJwtException(ExpiredJwtException expiredJwtException,
            List<Map<String, Object>> errorList) {
        buildErrorMap(expiredJwtException.getMessage(), HttpStatus.UNAUTHORIZED, errorList);
    }

    private void handleMalformedJwtException(MalformedJwtException malformedJwtException,
            List<Map<String, Object>> errorList) {
        buildErrorMap(malformedJwtException.getMessage(), HttpStatus.UNAUTHORIZED, errorList);
    }

    private void handleInternalServerError(Throwable error, List<Map<String, Object>> errorList) {
        buildErrorMap((error.getMessage() != null) ? error.getMessage() : error.getClass().getName(),
                HttpStatus.INTERNAL_SERVER_ERROR, errorList);
    }

    private void buildErrorMap(String message, HttpStatus status, List<Map<String, Object>> errorList) {
        var errorMap = createErrorMap(message, status);
        errorList.add(errorMap);
    }

    private Map<String, Object> createErrorMap(String message, HttpStatus status) {
        var errorMap = new LinkedHashMap<String, Object>();
        errorMap.put(MESSAGE, message);
        errorMap.put(STATUS, status.value());
        errorMap.put(ERROR, status.getReasonPhrase());
        return errorMap;
    }
}
