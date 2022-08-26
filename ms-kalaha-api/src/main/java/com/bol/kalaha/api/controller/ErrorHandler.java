package com.bol.kalaha.api.controller;

import com.bol.kalaha.api.log.RzLogger;
import com.bol.kalaha.api.model.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.bol.kalaha.api.model.constant.Constants.UNEXPECTED_EXCEPTION_CODE;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE_TEMPLATE = "Handling {}. Message: {}";
    private static final RzLogger logger = RzLogger.getLogger(ErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handle(Exception ex) {
        var response = new ExceptionResponseDto(UNEXPECTED_EXCEPTION_CODE + "unexpected", null);
        logger.error(ERROR_MESSAGE_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
