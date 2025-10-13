package com.green.blue.red.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_address")
@Getter
@ToString(exclude = "imageList")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    private int age;
    private String city;
    private String gu;
    private String dong;
    private String name;

    @ElementCollection
    @Builder.Default
    List<AddressImage> imageList = new ArrayList<>();

    public void changeAge(int age) {
        this.age = age;
    }
    public void changeCity(String city) {
        this.city = city;
    }
    public void changeGu(String gu) {
        this.gu = gu;
    }
    public void changeDong(String dong) {
        this.dong = dong;
    }
    public void changeName(String name) {
        this.name = name;
    }

    public void addImage(AddressImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName) {
        AddressImage addressImage = AddressImage.builder()
                .fileName(fileName)
                .build();
        addImage(addressImage);
    }

    public void cleatList() {
        this.imageList.clear();
    }
}
