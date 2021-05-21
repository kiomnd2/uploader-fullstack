package com.kakaopay.uploader.service;

import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UploadServiceTest {

    @Autowired
    UploadService uploadService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CountManager countManager;

    @BeforeEach
    void beforeEach() {
        personRepository.deleteAllInBatch();
        countManager.clear();
    }

    @Test
    void getUUIDTest() throws Exception {
        String uploadUUID = uploadService.createUploadUUID();

        CountDto countDto = uploadService.getCountDto(uploadUUID);
        // UUID 40 자
        assertThat(uploadUUID.length()).isEqualTo(36);
        assertThat(countDto).isNotNull();
    }

    @Test
    void savePersonTest() throws Exception {
        String uploadUUID = uploadService.createUploadUUID();

        byte[] bytes = Files.readAllBytes(
                Paths.get("./src/test/resources/dataset_success.csv"));
        MockMultipartFile file = new MockMultipartFile("file", bytes);

        CountDto countDto = uploadService.savePerson(file, uploadUUID);

        assertThat(countDto).isNotNull();
        assertThat(countDto.getSuccessCount()).isEqualTo(101);
        assertThat(countDto.getFailCount()).isEqualTo(0);
    }
}
