package com.kakaopay.uploader.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException() {
        super("잘못된 요청입니다");
    }
}
