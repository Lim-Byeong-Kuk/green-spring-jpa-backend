package com.green.blue.red.test;

import java.util.HashMap;
import java.util.Map;

public class Main6 {
    static String cutLastStr(String str) {
        return str.substring(0,str.length()-1);
    }
    static int cutLastNumAndParse(String str) {
        return Integer.parseInt(str.substring(str.length()-1));
    }

//    {"a_1":"사랑증오별", "a_2":"믿음필승기현", "a_3":"16", "a_4":"18"}

    public static void main(String[] args) {

        String[][] str = {
                {"사랑5","믿음7","공기4","산소2"},
                {"증오9","필승3","바람1","구름5"},
                {"별2","기현8","오이도2","안산9"}
        };

        String a1="";
        String a2="";
        Integer a3=0;
        Integer a4=0;

        for (int i=0; i<str.length; i++) {
            a1+= cutLastStr(str[i][0]);
            a2+= cutLastStr(str[i][1]);
            a3+= cutLastNumAndParse(str[i][0]);
            a4+= cutLastNumAndParse(str[i][1]);
        }

        Map<String, String> map = new HashMap<>();
        map.put("a_1",a1);
        map.put("a_2",a2);
        map.put("a_3",String.valueOf(a3));
        map.put("a_4",String.valueOf(a4));

        System.out.println("map = " + map);
    }

}
