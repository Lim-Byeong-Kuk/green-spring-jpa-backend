package com.green.blue.red.test;

import java.util.Arrays;
import java.util.List;

public class JavaGrammarTest {

    private static String generateGu(List<String> dongStr, int repeat) {
        String gu = "";
        for (int i=0; i<repeat; i++) {
            gu+=dongStr.get((int)(Math.random()* dongStr.size()));
        }
        return gu;
    }

    public static void main(String[] args) {

        String[] dong = new String[30];
        List<String> dongStr = Arrays.asList("a","b","c","d","e","f");
        for(int i=0; i<dong.length; i++) {
            String r = generateGu(dongStr, (int) (Math.random() * 20 + 1));
            dong[i] = r;
        }

        for(int i=0; i<dong.length; i++) {
            System.out.println("dong["+i+"] : " + dong[i]);
        }



    }
}
