package com.kakaopay.uploader.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CountDto {

    private int totalCount;

    private int successCount;

    private int failCount;

    public void addSuccessCount(int count) {
        this.successCount += count;
        this.totalCount += count;
    }

    public void addFailCount(int count) {
        this.totalCount += count;
        this.failCount += count;
    }

}
