package com.kakaopay.uploader.service;

import com.kakaopay.uploader.dto.CountDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class CountManager {

    private final ConcurrentHashMap<String, CountDto> countMap = new ConcurrentHashMap<>();

    public CountDto createNewCount(String uuid) {
        CountDto countDto = new CountDto();
        this.countMap.put(uuid, countDto);
        return countDto;
    }

    public CountDto getCount(String uuid) {
        return countMap.get(uuid);
    }

    public void deleteCount(String uuid) {
        countMap.remove(uuid);
    }

    public void clear() {
        countMap.clear();
    }



}
