package com.green.blue.red.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MainTest5 {

    private static void generateGu(List<String> strs, int [] u, AddressDTO dto) {
        strs.add(2,"자랑");
        u[1]=100;
        dto.setGu("generateGu 에 의해 변경");
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("사랑");
        list.add("증오");
        list.add("우정");

        int[] arr = new int[] {1,2,3};

        AddressDTO dto = new AddressDTO();
        dto.setGu("초기값");

        generateGu(list,arr,dto);
        list.forEach(i -> System.out.println("list = " + i));
        Arrays.stream(arr).forEach(i -> System.out.println("arr = " + i));
        System.out.println("dto = " + dto);
    }


}
