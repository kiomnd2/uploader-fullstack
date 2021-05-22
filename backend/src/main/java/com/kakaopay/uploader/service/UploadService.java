package com.kakaopay.uploader.service;

import com.kakaopay.uploader.domain.Person;
import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.exception.InvalidRequestException;
import com.kakaopay.uploader.repository.PersonRepository;
import com.kakaopay.uploader.util.ValidateUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Service
public class UploadService {

    private final PersonRepository personRepository;

    private final CountManager countManager;

    public CountDto savePerson(MultipartFile is, String uuid) {
        CountDto countDto = getCountDto(uuid);

            List<Person> personList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(is.getInputStream(), StandardCharsets.UTF_8))) {

            CSVReader reader = new CSVReaderBuilder(br).withSkipLines(1).build();
            String[] data;
            while ((data = reader.readNext()) != null) {
                if (!ValidateUtil.validData(data)) {
                    countDto.addFailCount(1);
                } else {
                    Person person = Person.builder()
                            .id(Long.parseLong(data[0]))
                            .firstname(data[1])
                            .lastname(data[2])
                            .email(data[3])
                            .build();

                    personList.add(person);

                    try {
                        if (personList.size() % 100 == 0) {
                            personRepository.saveAll(personList);
                            countDto.addSuccessCount(personList.size());
                            personList.clear();
                        }
                    } catch (DataIntegrityViolationException e) {
                        countDto.addFailCount(personList.size());
                        personList.clear();
                    }
                }
            }
            // 나머지 데이터 insert
            personRepository.saveAll(personList);
            countDto.addSuccessCount(personList.size());
        } catch (IOException e) {
            throw new InvalidRequestException();
        } catch (DataIntegrityViolationException e) {
            countDto.addFailCount(personList.size());
        }

        return countDto;
    }

    public String createUploadUUID() {
        String uuid = UUID.randomUUID().toString();
        countManager.createNewCount(uuid);
        return uuid;
    }

    public CountDto getCountDto(String uuid) {
        CountDto countDto = countManager.getCount(uuid);
        if (countDto == null) {
            throw new InvalidRequestException();
        }

        return countDto;
    }

    public void deleteUUID(String uuid) {
        countManager.deleteCount(uuid);
    }

}
