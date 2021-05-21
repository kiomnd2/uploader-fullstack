package com.kakaopay.uploader.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CountDto implements Cloneable{

    private int successCount;

    private int failCount;

    public void addSuccessCount(int count) {
        this.successCount += count;
    }

    public void addFailCount(int count) {
        this.failCount += count;
    }

    public CountDto clone() {
        CountDto countDto = null;
        try{
            countDto = (CountDto) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return countDto;
    }

}
