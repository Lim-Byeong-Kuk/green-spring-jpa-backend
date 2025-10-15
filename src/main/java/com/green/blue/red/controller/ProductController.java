package com.green.blue.red.controller;

import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.dto.ProductDTO;
import com.green.blue.red.service.ProductService;
import com.green.blue.red.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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

    // 1개 조회
    @GetMapping("/{pno}")
    public ResponseEntity<ProductDTO> getOne(@PathVariable("pno") Long pno) {
        log.info("controller 진입, getOne 호출, pno :{}",pno);
        ProductDTO productDTO = service.get(pno);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) {
        productDTO.setPno(pno);
        ProductDTO oldProductDTO = service.get(pno);

        // 새로 업로드 된 파일은 getFiles() 에 데이터가 있고, 원래 파일들(이름)은 getUploadFileNames() 에 각각 존재한다.

        //기존의 파일들(데이터베이스에 존재하는 파일들 - 수정과정에서 삭제되었을수 있음)
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        //새로 업로드 해야 하는 파일들
        List<MultipartFile> files = productDTO.getFiles();
        //저장하고 새로 업로드 되어서 만들어진 파일 이름들
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //화면에서 변화없이 계속 유지된 파일들
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        // 유지되는 파일들 + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
        if(currentUploadFileNames != null && currentUploadFileNames.size()>0) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        //수정 작업
        service.modify(productDTO);

        if(oldFileNames!=null && oldFileNames.size()>0) {
            //지워야 하는 파일목록 찾기
            //예전 파일들 중에서 지워야하는 하는 파일이름들
            List<String> removeFiles = oldFileNames
                    .stream().filter(i -> !uploadedFileNames.contains(i)).toList();

            //실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT", "SUCCESS");

    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable(name = "pno") Long pno) {
        ProductDTO oldProductDTO = service.get(pno);
        //삭제해야할 파일들 알아내기
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        service.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
}
