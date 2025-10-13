package com.green.blue.red.dto;

import com.green.blue.red.domain.AddressImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long ano;

    private int age;
    private String city;
    private String gu;
    private String dong;
    private String name;

//    public AddressDTO(Long ano, int age, String city, String gu, String dong, String name, String fileName) {
//        this.ano = ano;
//        this.age = age;
//        this.city = city;
//        this.gu = gu;
//        this.dong = dong;
//        this.name = name;
//        this.uploadFileNames.add(fileName);
//    }

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();
}
