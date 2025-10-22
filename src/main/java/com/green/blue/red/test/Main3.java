package com.green.blue.red.test;

import java.util.*;
import java.util.stream.Collectors;

public class Main3 {

    public static void main(String[] args) {


        String[] data = {
                "홍길동3", "김말자9", "김개똥이다8", "권기현이다아6", "지렁이3"
        };

//        {"문자열결합": "홍길동김말자김개똥이다권기현이다아지렁이", "더하기":3+9+6+3}

        Map<String, Object> map = new HashMap<>();

        String merge = Arrays.stream(data).reduce("", (acc, i) -> i.substring(0, i.length() - 1) + acc);
        System.out.println("merge = " + merge);

        Integer sum = Arrays.stream(data).map(str -> Integer.valueOf(str.substring(str.length() - 1)))
                .reduce(0, (acc, i) -> acc + i);
        System.out.println("sum = " + sum);

        map.put("문자열결합",merge);
        map.put("더하기",sum);

        System.out.println(" map :" + map);
    }
}
