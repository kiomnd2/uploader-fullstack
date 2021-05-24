package com.kakaopay.uploader.service;

import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
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
        personRepository.deleteAll();
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

        File file = Paths.get("./src/test/resources/dataset_success.csv").toFile();

        CountDto countDto = uploadService.savePerson(uploadUUID, file);

        assertThat(countDto).isNotNull();
        assertThat(countDto.getSuccessCount()).isEqualTo(101);
        assertThat(countDto.getFailCount()).isEqualTo(0);
    }

    @Test
    void combineChunkTest() throws Exception {
        String uploadUUID = uploadService.createUploadUUID();


        byte[] bytes = Files.readAllBytes(Paths.get("./src/test/resources/dataset_success.csv"));

        File file = uploadService.combineChuck(uploadUUID, new MockMultipartFile("file", bytes));

        assertThat(file).exists();
        assertThat(Files.readAllBytes(file.toPath())).isEqualTo(bytes);

    }
}
