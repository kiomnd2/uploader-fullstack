package com.kakaopay.uploader.controller;

import com.kakaopay.uploader.dto.CountDto;
import com.kakaopay.uploader.service.UploadService;
import com.oracle.tools.packager.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UploadController {

    private final UploadService uploadService;


    /**
     * uuid를 발급받습니다
     * @return uuid
     */
    @GetMapping(value = "/api/uuid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadResponse<String>> requestUUID() {
        final String uuid = uploadService.createUploadUUID();
        log.debug("get UUID : {}",uuid);
        return ResponseEntity.ok().body(UploadResponse.success(uuid));
    }

    /**
     * 파일을 업로드 합니다
     * @param uuid 고유 아이디
     * @param file 파일
     * @param chunkIdx 현재 chunk
     * @param totalIdx 총 chunk
     * @return CountDto
     */
    @PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadResponse<CountDto>> upload(
            @RequestHeader("X-UPLOAD-UUID") String uuid,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dzchunkindex", required = false) Integer chunkIdx,
            @RequestParam(value = "dztotalchunkcount", required = false) Integer totalIdx
    ) {
        // 파일을 합칩니다
        final File combineFile = uploadService.combineChuck(uuid, file);

        CountDto countDto = new CountDto(0,0);
        if ((chunkIdx == null && totalIdx == null) || chunkIdx != null && chunkIdx.equals(totalIdx-1) ) {
            countDto = uploadService.savePerson(uuid, combineFile);
        }
        return ResponseEntity.ok().body(UploadResponse.success(countDto));
    }


    /**
     * 현재 DB 입력 정보를 리턴합니다
     * @param uuid 고유 아이디
     * @return CountDto
     */
    @GetMapping(value = "/api/inquire")
    public ResponseEntity<UploadResponse<CountDto>> inquireRate(@RequestHeader("X-UPLOAD-UUID") String uuid) {
        return ResponseEntity.ok().body(UploadResponse.success(uploadService.getCountDto(uuid)));
    }

}
