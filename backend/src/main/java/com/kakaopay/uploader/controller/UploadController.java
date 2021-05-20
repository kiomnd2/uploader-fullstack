package com.kakaopay.uploader.controller;

import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RestController
public class UploadController {

    private final UploadService uploadService;

    @GetMapping(value = "/api/uuid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadResponse<String>> requestUUID() {
        String uuid = uploadService.createUploadUUID();
        log.debug("get UUID : {}",uuid);
        return ResponseEntity.ok().body(UploadResponse.success(uuid));
    }

    @PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadResponse<CountDto>> upload(@RequestHeader("X-UPLOAD-UUID") String uuid
            , MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("file");
        CountDto countDto = uploadService.savePerson(file, uuid);
        return ResponseEntity.ok().body(UploadResponse.success(countDto));
    }

    @GetMapping(value = "/api/inquire")
    public ResponseEntity<UploadResponse<CountDto>> inquireRate(@RequestHeader("X-UPLOAD-UUID") String uuid) {
        return ResponseEntity.ok().body(UploadResponse.success(uploadService.getCountDto(uuid)));
    }

}
