package com.kakaopay.uploader.controller;

import com.kakaopay.uploader.code.Codes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UploadResponse<T> {

    final private String code;

    final private String message;

    final private T body;

    public static <T> UploadResponse<T> success(T body) {
        return new UploadResponse<>(Codes.S0000.code, Codes.S0000.desc, body);
    }

    public static <T> UploadResponse<T> fail(T body) {
        return new UploadResponse<>(Codes.E2000.code, Codes.E2000.desc, body);
    }
}
