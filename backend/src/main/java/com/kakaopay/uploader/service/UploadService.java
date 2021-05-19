package com.kakaopay.uploader.service;

import com.kakaopay.uploader.domain.Person;
import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.exception.InvalidRequestException;
import com.kakaopay.uploader.repository.PersonRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class UploadService {

    private final PersonRepository personRepository;

    private final CountManager countManager;

    public CountDto savePerson(InputStream is, String uuid) {
        CountDto countDto = countManager.getCount(uuid);

        if (countDto == null) {
            throw new InvalidRequestException();
        }

        List<Person> personList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {

            CSVReader reader = new CSVReaderBuilder(br).withSkipLines(1).build();
            String[] data;
            while ((data = reader.readNext()) != null) {
                if (!validData(data)) {
                    countDto.addFailCount(1);
                } else {
                    Person person = Person.builder()
                            .id(Long.parseLong(data[0]))
                            .firstname(data[1])
                            .lastname(data[2])
                            .email(data[3])
                            .build();

                    personList.add(person);
                }

                if (personList.size() % 100 == 0) {
                    personRepository.saveAll(personList);
                    countDto.addSuccessCount(personList.size());
                    personList.clear();
                }
            }
            // 나머지 데이터 insert
            countDto.addSuccessCount(personList.size());
            personRepository.saveAll(personList);

        } catch (Exception e) {
            countDto.addFailCount(personList.size());
            personList.clear();
            e.printStackTrace();
        }
        return countDto;
    }

    public String createUploadUUID() {
        String uuid = UUID.randomUUID().toString();
        countManager.createNewCount(uuid);
        return uuid;
    }

    public CountDto getCountDto(String uuid) {
        return countManager.getCount(uuid);
    }

    private boolean validData(String[] data) {
        if (data.length != 4) {
            return false;
        }
        for (String col : data) {
            if (col == null) {
                return false;
            }
        }
        return true;
    }
}