package com.kakaopay.uploader.controller;

import com.kakaopay.uploader.code.Codes;
import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.repository.PersonRepository;
import com.kakaopay.uploader.service.UploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UploadService uploadService;

    @Autowired
    PersonRepository personRepository;

    @BeforeEach
    void beforeEach() {
        personRepository.deleteAll();
    }

    @Test
    void request_uuid_success() throws Exception {
        mockMvc.perform(get("/api/uuid")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Codes.S0000.code))
                .andExpect(jsonPath("message").value(Codes.S0000.desc))
                .andExpect(jsonPath("body").exists());

    }

    @Test
    void request_upload_success() throws Exception {
        String uploadUUID = uploadService.createUploadUUID();

        byte[] bytes = Files.readAllBytes(
                Paths.get("./src/test/resources/dataset_success.csv"));
        MockMultipartFile file = new MockMultipartFile("file", bytes);
        mockMvc.perform(multipart("/api/upload")
                .file(file)
                .header("X-UPLOAD-UUID", uploadUUID)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Codes.S0000.code))
                .andExpect(jsonPath("message").value(Codes.S0000.desc))
                .andExpect(jsonPath("body.successCount").value(1));

    }


    @Test
    void request_upload_fail() throws Exception {
        String uploadUUID = uploadService.createUploadUUID();

        byte[] bytes = Files.readAllBytes(
                Paths.get("./src/test/resources/dataset_fail.csv"));
        MockMultipartFile file = new MockMultipartFile("file", bytes);
        mockMvc.perform(multipart("/api/upload")
                .file(file)
                .header("X-UPLOAD-UUID", uploadUUID)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Codes.S0000.code))
                .andExpect(jsonPath("message").value(Codes.S0000.desc))
                .andExpect(jsonPath("body.successCount").value(1))
                .andExpect(jsonPath("body.failCount").value(1));
    }

    @Test
    void request_inquire_no_count() throws Exception {
        String uuid = UUID.randomUUID().toString();


        mockMvc.perform(get("/api/inquire")
                .header("X-UPLOAD-UUID", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(Codes.E2000.code));
    }


    @Test
    void request_inquire_get_count() throws Exception {

        String uploadUUID = uploadService.createUploadUUID();

        byte[] bytes = Files.readAllBytes(
                Paths.get("./src/test/resources/dataset_success.csv"));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        MockMultipartFile file = new MockMultipartFile("file", byteArrayInputStream);
        CountDto countDto = uploadService.savePerson(file, uploadUUID);


        mockMvc.perform(get("/api/inquire")
                .header("X-UPLOAD-UUID", uploadUUID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body.successCount").value(countDto.getSuccessCount()))
                .andExpect(jsonPath("body.failCount").value(countDto.getFailCount()));

    }

}
