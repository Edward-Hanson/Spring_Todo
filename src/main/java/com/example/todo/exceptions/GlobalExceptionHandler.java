package com.example.todo.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(buildResponse(errors, HttpStatus.BAD_REQUEST, request));

    }

    @ExceptionHandler({TodoException.class})
    public ResponseEntity<ExceptionResponse> handleGeneralTodoException(TodoException e, HttpServletRequest request) {
        logger.warn("Business error: {}" , e.getMessage());
        return ResponseEntity.badRequest().body(buildResponse(List.of(e.getMessage()), HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = ex.getMostSpecificCause().getMessage();

        if (message != null && message.contains("Unrecognized field")) {
            int fieldStart = message.indexOf("\"");
            int fieldEnd = message.indexOf("\"", fieldStart + 1);
            String unknownField = fieldStart >= 0 && fieldEnd > fieldStart
                    ? message.substring(fieldStart + 1, fieldEnd)
                    : "Unknown field";

            message = "Unexpected field in request: \"" + unknownField + "\" is not allowed.";
        }

            return ResponseEntity.badRequest().body(buildResponse(
                    List.of(message),
                    HttpStatus.BAD_REQUEST,
                    request
            ));
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception e, HttpServletRequest request) {
        logger.error("Unexpected error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(List.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR,request));
    }

    private ExceptionResponse buildResponse(List<String> messages, HttpStatus status, HttpServletRequest request) {
        return new ExceptionResponse(
                messages,
              status.value(),
              request.getRequestURI(),
              LocalDateTime.now()
        );
    }
}
