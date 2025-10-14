package com.green.blue.red.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class MainTest2 {

    public static void main(String[] args) {


        // 5개의 Predicate 타입을 만들어서 Map에 저장하고 호출해서 확인하기

        List<String> keys = List.of("a", "b", "c", "d", "e");
        Map<String, Predicate<Integer>> pList = IntStream.rangeClosed(1, 5).boxed()
                .collect(Collectors.toMap(
                        index -> keys.get(index-1),
                        index -> (Predicate<Integer>)(i -> i%(index+1)==0 )
                        ));
        System.out.println(pList);



        Map<String, List<Predicate<Integer>>> map = new HashMap<>();
        // randData 20개를 5개의 Predicate 로 검사하면서
        // 3의배수, 5의 배수, 7의배수, 3보다 큰 수인가, 20보다 작은 수인가를 체크
        List<Predicate<Integer>> list = new ArrayList<>();
        list.add(i -> i%3 ==0);
        list.add(i -> i%5 ==0);
        list.add(i -> i%7 ==0);
        list.add(i -> i>3);
        list.add(i -> i<20);

        map.put("a", list);

        System.out.println(map);

        List<Integer> randData = new ArrayList<>();
        for (int i=0; i<20; i++) {
            randData.add((int)(Math.random()*100));
        }
        log.info("{}", randData);



        Map<String, List<Boolean>> resultMap = new HashMap<>();

        resultMap.put("참", new ArrayList<>());
        resultMap.put("거짓", new ArrayList<>());

        randData.forEach(n -> {
            map.get("a").forEach(p -> {
                if(p.test(n)) { resultMap.get("참").add(true); }
                else { resultMap.get("거짓").add(false); }
            });
        });

        System.out.println(resultMap);


        System.out.println("거짓 갯수 : " +resultMap.get("거짓").size());
        System.out.println(" 참 갯수 : " +resultMap.get("참").size());


    }
}
