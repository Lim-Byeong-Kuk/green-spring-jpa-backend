package com.green.blue.red.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "owner")
@Table(
        // member_owner 컬럼을 기준으로 하는 idx_cart_email 이라는 이름의 새로운 보조 인덱스를 추가로 생성
        name = "tbl_cart", indexes = {@Index(name="idx_cart_email", columnList = "member_owner")}
)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne
    @JoinColumn(name = "member_owner") // Member 의 primary Key 가 "member_owner" 라는 이름(컬럼)으로
    private Member owner;

}
