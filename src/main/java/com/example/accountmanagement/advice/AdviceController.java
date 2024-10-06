package com.example.accountmanagement.advice;

import com.example.accountmanagement.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity DateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date syntax is not valid try DD.mm.YYYY eg:01.01.2024");
    }
    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity NumberFormatException(NumberFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount syntax is not valid try 100.0");
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity ApiException(ApiException e){
        String message=e.getMessage();
        return ResponseEntity.status(e.getStatus()).body(message);
    }
}
