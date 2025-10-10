package com.green.blue.red.controller;

import com.green.blue.red.dto.ProductDTO;
import com.green.blue.red.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    @PostMapping("/")
    public Map<String, String> register(ProductDTO dto) {
        log.info("register:{}",dto);
        List<MultipartFile> files = dto.getFiles();
        List<String> uploadFiles = fileUtil.saveFiles(files);
        dto.setUploadFileNames(uploadFiles);
        log.info("uploadFileNames: {}",uploadFiles);
        return Map.of("RESULT", "SUCCESS");
    }

    //이미지 보여주기
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName) {
        log.info("viewFileGet 진입 fileName => {}", fileName);
        return fileUtil.getFile(fileName);
    }
}
