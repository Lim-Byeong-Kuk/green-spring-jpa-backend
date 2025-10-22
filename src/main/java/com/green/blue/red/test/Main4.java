package com.green.blue.red.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
public class Main4 {

    public static void main(String[] args) {
        String[][] str = {
                {""+11, "23","17","21" },
                {""+21, "33","117","210"},
                {""+19, "73","127","212"},
                {""+111, "63","172","213"},
                {""+1, "13","117","214"},
        };
//4 ,3
//        {"a_1":11+21+19+14+111+1,"a_2":23+33+73+63+13, ...

        int sum1 =0;
        int sum2 =0;
        int sum3 =0;
        int sum4 =0;

        for (int i=0; i<str.length; i++) { //0~4
                sum1+=Integer.parseInt(str[i][0]);
                sum2+=Integer.parseInt(str[i][1]);
                sum3+=Integer.parseInt(str[i][2]);
                sum4+=Integer.parseInt(str[i][3]);
        }
        System.out.println("sum1 = " + sum1);
        System.out.println("sum2 = " + sum2);
        System.out.println("sum3 = " + sum3);
        System.out.println("sum4 = " + sum4);

        Map<String, Integer> map = new HashMap<>();

        map.put("a_1",sum1);
        map.put("a_2",sum2);
        map.put("a_3",sum3);
        map.put("a_4",sum4);

        System.out.println("map = " + map);
    }


}
