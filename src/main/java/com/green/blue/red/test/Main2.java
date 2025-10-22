package com.green.blue.red.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2 {

    public static void main(String[] args) {



//        [
//         {"A":3, "B":5, "C":7, "D":9},
//         {"A":13,"B",15,"C":17,"D":19},
//         {"A":23,"B",25,"C":27,"D":29},
//         {"A":50,"B":100,"C":150,"D":200}
//        ]
        List<Integer> nums = List.of(1, 2, 3, 4);

        List<Map<String, Integer>> resultList = nums.stream().map(i -> {
            Map<String, Integer> map = new HashMap<>();
            map.put("A", 3 + ((i - 1) * 10));
            map.put("B", 5 + ((i - 1) * 10));
            map.put("C", 7 + ((i - 1) * 10));
            map.put("D", 9 + ((i - 1) * 10));
            if(i == 4) {
                map.put("A",50*nums.get(i-4));
                map.put("B",50*nums.get(i-3));
                map.put("C",50*nums.get(i-2));
                map.put("D",50*nums.get(i-1));
            }

            return map;
        }).toList();

        System.out.println("resultList = " + resultList);



    }
}
