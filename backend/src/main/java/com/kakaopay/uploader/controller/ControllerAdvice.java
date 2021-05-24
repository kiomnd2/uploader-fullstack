package com.kakaopay.uploader.controller;

import com.kakaopay.uploader.exception.InvalidFileException;
import com.kakaopay.uploader.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<UploadResponse<String>> validate(InvalidRequestException e) {
        return ResponseEntity.badRequest().body(UploadResponse.fail(e.getMessage()));
    }


    @ExceptionHandler(InvalidFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<UploadResponse<String>> validate(InvalidFileException e) {
        return ResponseEntity.badRequest().body(UploadResponse.fail(e.getMessage()));
    }
}
