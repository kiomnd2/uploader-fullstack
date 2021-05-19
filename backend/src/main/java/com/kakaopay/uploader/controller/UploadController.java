package com.kakaopay.uploader.controller;

import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class UploadController {

    private final UploadService uploadService;

    @GetMapping("/uuid")
    public ResponseEntity<UploadResponse<String>> requestUUID() {
        String uuid = uploadService.createUploadUUID();
        return ResponseEntity.ok().body(UploadResponse.success(uuid));
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse<CountDto>> upload(@RequestHeader("X-UUID") String uuid
            , @RequestParam("file") MultipartFile file) throws IOException {
        CountDto countDto = uploadService.savePerson(file.getInputStream(), uuid);
        return ResponseEntity.ok().body(UploadResponse.success(countDto));
    }

    @GetMapping("/inquire")
    public ResponseEntity<UploadResponse<CountDto>> inquireRate(@RequestParam("uuid") String uuid) {
        return ResponseEntity.ok().body(UploadResponse.success(uploadService.getCountDto(uuid)));
    }

}
