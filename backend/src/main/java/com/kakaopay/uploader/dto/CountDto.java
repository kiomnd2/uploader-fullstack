package com.kakaopay.uploader.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CountDto {

    private int successCount;

    private int failCount;

    public void addSuccessCount(int count) {
        this.successCount += count;
    }

    public void addFailCount(int count) {
        this.failCount += count;
    }

}
