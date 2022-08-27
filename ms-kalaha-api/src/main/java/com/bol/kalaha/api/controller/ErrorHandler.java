package com.bol.kalaha.api.controller;

import com.bol.kalaha.api.logger.RzLogger;
import com.bol.kalaha.api.model.dto.ExceptionResponseDto;
import com.bol.kalaha.core.exception.IllegalMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.bol.kalaha.api.model.constant.Constants.ILLEGAL_MOVE_EXCEPTION_CODE;
import static com.bol.kalaha.api.model.constant.Constants.UNEXPECTED_EXCEPTION_CODE;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE_TEMPLATE = "Handling {}. Message: {}";
    private static final RzLogger logger = RzLogger.getLogger(ErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handle(Exception ex) {
        var response = new ExceptionResponseDto(UNEXPECTED_EXCEPTION_CODE, null);
        logger.error(ERROR_MESSAGE_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalMoveException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalMove(IllegalMoveException ex) {
        var response = new ExceptionResponseDto(ILLEGAL_MOVE_EXCEPTION_CODE, ex.getMessage());
        logger.error(ERROR_MESSAGE_TEMPLATE, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
