package com.kakaopay.uploader.code;

public enum Codes {
    S0000("0000", "성공"),
    E2000("2000", "잘못된 요청");

    final public String code;

    final public String desc;

    Codes(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
