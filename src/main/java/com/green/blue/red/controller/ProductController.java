package com.green.blue.red.controller;

import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.dto.ProductDTO;
import com.green.blue.red.service.ProductService;
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
@RequestMapping("/api/product")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService service;

    @PostMapping("/")
    public Map<String, Long> register(@ModelAttribute ProductDTO dto) {
        log.info("register:{}",dto);
        List<MultipartFile> files = dto.getFiles();
        List<String> uploadFiles = fileUtil.saveFiles(files);
        dto.setUploadFileNames(uploadFiles);
        log.info("uploadFileNames: {}",uploadFiles);
        Long pno = service.register(dto);
        return Map.of("result", pno);
    }

    //이미지 보여주기
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName) {
        log.info("viewFileGet 진입 fileName => {}", fileName);
        return fileUtil.getFile(fileName);
    }

    //Page 당 전체 목록 조회
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list controller .......... pageRequest:{}", pageRequestDTO);
        return service.getList(pageRequestDTO);
    }
}
