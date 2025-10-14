package com.green.blue.red.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${com.green.blue.red.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);
        if(tempFolder.exists() == false) {
            tempFolder.mkdir();
        }
        uploadPath = tempFolder.getAbsolutePath();
        log.info("----------------------폴더 생성");
        log.info(uploadPath);
    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        if(files == null || files.isEmpty()) {
            return null;
        }

        List<String> uploadNames = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            String savedName = UUID.randomUUID().toString()+"_"+multipartFile.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, savedName);
            try {
                Files.copy(multipartFile.getInputStream(), savePath);
                //thumbnail 관련 추가
                String contentType = multipartFile.getContentType();
                if(contentType!=null && contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_"+savedName);

                    Thumbnails.of(savePath.toFile())
                            .size(200,200)
                            .toFile(thumbnailPath.toFile());
                }

                uploadNames.add(savedName);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return uploadNames;
    }


    // 일반적으로 file server 가 있고 그 파일 서버의 경로를 db에서 가지고 있음
    // db는 고가라서 가능한 큰 파일을 저장하지 않음
    // separator : 구분자
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        if(!resource.exists()) resource = new FileSystemResource(uploadPath+File.separator+fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            String check = Files.probeContentType(resource.getFile().toPath());
            log.info("CustomFileUtil => check:{}",check);
            headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);//resource (실제 이미지)
        //브라우저가 encoding 된 데이터를 header 정보를 기반으로 decoding(복호화)
    }


    public void deleteFiles(List<String> fileNames) {
        if(fileNames == null || fileNames.size()==0) return;
        fileNames.forEach(i -> {
            //썸네일이 있는지 확인하고 삭제
            String thumbnailFileName = "s_"+i;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, i);
            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);

            } catch (IOException e) {
                throw new RuntimeException((e.getMessage()));
            }
        });
    }


}
