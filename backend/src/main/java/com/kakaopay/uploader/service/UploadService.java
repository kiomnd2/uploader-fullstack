package com.kakaopay.uploader.service;

import com.kakaopay.uploader.domain.Person;
import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.exception.InvalidFileException;
import com.kakaopay.uploader.exception.InvalidRequestException;
import com.kakaopay.uploader.repository.PersonRepository;
import com.kakaopay.uploader.util.ValidateUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Service
public class UploadService {

    private final static int BATCH_SIZE = 100;

    private final PersonRepository personRepository;

    private final CountManager countManager;

    @Value("${app.upload.dir:${user.home}}")
    private  String uploadDir;

    /**
     * 파일 정보를 DB에 업데이트 합니다.
     * @param uuid 고유 아이디
     * @param file 파일
     * @return
     */
    @Transactional
    public CountDto savePerson(String uuid, File file) {

        final CountDto countDto = getCountDto(uuid);

        final List<Person> personList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            final CSVReader reader = new CSVReaderBuilder(br).withSkipLines(1).build();
            String[] data;
            while ((data = reader.readNext()) != null) {
                if (!ValidateUtil.validData(data)) {
                    countDto.addFailCount(1);
                    continue;
                }
                final Person person = Person.builder()
                        .id(Long.parseLong(data[0]))
                        .firstname(data[1])
                        .lastname(data[2])
                        .email(data[3])
                        .build();
                personList.add(person);

                try {
                    if (personList.size() % BATCH_SIZE == 0) {
                        personRepository.batchInsert(personList);
                        countDto.addSuccessCount(personList.size());
                        personList.clear();
                    }
                } catch (DataIntegrityViolationException e) {
                    countDto.addFailCount(personList.size());
                    personList.clear();
                }
            }
            // 나머지 데이터 insert
            personRepository.batchInsert(personList);
            countDto.addSuccessCount(personList.size());
        } catch (IOException e) {
            throw new InvalidFileException();
        } catch (DataIntegrityViolationException e) {
            countDto.addFailCount(personList.size());
        }
        deleteUUID(uuid);

        return countDto;
    }

    /**
     * 청크 파일일 경우 파일을 합칩니다.
     * @param uuid 고유 아이디
     * @param file 업로드된 파일
     * @return
     */
    public File combineChuck(String uuid, MultipartFile file) {
        final Path copyLocation = Paths.get(uploadDir + File.separator + uuid.trim());
        try(FileOutputStream fileOutputStream = new FileOutputStream(copyLocation.toFile(), true)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidFileException();
        }
        return copyLocation.toFile();
    }

    /**
     * 업로드 uuid 를 발급 합니다
     * @return uuid
     */
    public String createUploadUUID() {
        final String uuid = UUID.randomUUID().toString();
        countManager.createNewCount(uuid);
        return uuid;
    }

    /**
     * uuid를 기준으로 Count 정보를 담은 객체를 반환합니다
     * @param uuid
     * @return CountDto
     */
    public CountDto getCountDto(String uuid) {
        final CountDto countDto = countManager.getCount(uuid);
        if (countDto == null) {
            throw new InvalidRequestException();
        }
        return countDto;
    }

    /**
     * uuid를 제거합니다
     * @param uuid
     */
    public void deleteUUID(String uuid) {
        countManager.deleteCount(uuid);
    }

}
