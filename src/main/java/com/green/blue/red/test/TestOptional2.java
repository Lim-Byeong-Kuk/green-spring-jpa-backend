package com.green.blue.red.test;

import com.green.blue.red.domain.Member;
import com.green.blue.red.domain.MemberRole;
import com.green.blue.red.dto.MemberDTO;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestOptional2 {

    public static void main(String[] args) {

        // 세팅
        List<Optional<Star>> list = new ArrayList<>();
        list.add(Optional.of(new Star(100,100,1)));
        list.add(Optional.ofNullable(null));

        // newList
        // list 를 순회하면서 list 를 변경 할 수 없기 때문에 새로운 리스트 변수 필요
        List<Optional<Star>> newStar = new ArrayList<>();

        // list 에서 꺼내면서 null 이면 Star 객체 생성 후 newList 에 적재
        // null 아니라 값이 있는것도 newList 에 담음
        list.forEach(o -> {
            Star resultStar = o.orElseGet(() -> {
                        Star star = new Star(50, 50, 2);
                        return star;
                    });
            newStar.add(Optional.of(resultStar));
            System.out.println("resultStar = " + resultStar);
        });

        // newStar 에 적재한 데이터로 변경
        list = newStar;
        System.out.println("list = " + list);



    }
}
