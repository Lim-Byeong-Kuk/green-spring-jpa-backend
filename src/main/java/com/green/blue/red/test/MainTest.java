package com.green.blue.red.test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;

public class MainTest {

    public static void main(String[] args) {

        Map<String, List<Integer>> map = new HashMap<>();
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        list.forEach(i -> {
            map.computeIfAbsent(i, key ->
                new ArrayList<>()
            );
        });
//        System.out.println(map);

        int[] nums = {1,10,3,4,8};

        list.forEach(key -> {
            Arrays.stream(nums).boxed().forEach(i -> {
                map.get(key).add(i);
                map.get(key).add(i*2);
                map.get(key).add(i*3);
                map.get(key).add(i*4);
                map.get(key).add(i*5);
            });
        });

//         map.get("a");
//
//        IntStream.rangeClosed(1,5).boxed().forEach(i -> {
//            map.get("a").add(i);
//            map.get("b").add(i*10);
//            map.get("c").add(i*3);
//            map.get("d").add(i*4);
//            map.get("e").add(i*8);
//        });

        System.out.println(map);

//        { a= [1,2,3,4,5], b=[10,20,30,40,50],
//            c=[3,6,9,12,15], d=[4의 배수 5개], e=[8의 배수 5개]}
    }
}
