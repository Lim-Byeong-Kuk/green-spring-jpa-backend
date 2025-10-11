package com.green.blue.red.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private boolean delFlag;

    // 자바에서는 클래스 안에 클래스를 넣는 식으로 관계를 정의한다
    // 그러나 DB 에서는 foreign key 로 관계를 정의함
    @ElementCollection // ProductImage 가 값 타입(value type)일때
    //@OneToMany // ProductImage 가 Entity 일때,
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();
    //ProductImage 가 pno 를 primary key 로 갖는 foreign key

    // 지연 로딩 전략 쓰는 이유:
    // imageList 의 크기가 크고 Product 의 다른 필드는 자주 사용하지만 imageList 를 사용하는 경우가 많지 않다면
    // Product 를 조회할 때 imageList 를 항상 꺼내올 필요가 있을까??
    // 만약 imageList Product 조회할 때 함께 가져오려면 join 쿼리를 날려야함 -> 성능 저하

    public void changePrice(int price) {
        this.price = price;
    }
    public void changeDesc(String desc) {
        this.pdesc = desc;
    }
    public void changeName(String name) {
        this.pname = name;
    }
    public void addImage(ProductImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }
    public void addImageString(String fileName) {
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(productImage);
    }
    public void clearList() {
        this.imageList.clear();
    }
}
