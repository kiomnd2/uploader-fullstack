package com.kakaopay.uploader.exception;

public class InvalidFileException extends RuntimeException{
    public InvalidFileException() {
        super("잘못된 파일 입니다.");
    }
}
