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
public class MainTest3 {

    public static void main(String[] args) {


        Map<String, List<Predicate<AddressDTO>>> map = new HashMap<>();

        //1) Predicate 10개 임의로 만들기
        List<Predicate<AddressDTO>> pList = List.of(
                dto -> dto.getAge()>=0 && dto.getAge()<=10,
                dto -> dto.getAge()>=10 && dto.getAge()<=20,
                dto -> dto.getAge()>=20 && dto.getAge()<=30,
                dto -> dto.getAge()>=30 && dto.getAge()<=40,
                dto -> dto.getAge()>=40 && dto.getAge()<=50,
                dto -> dto.getAge()>=50 && dto.getAge()<=60,
                dto -> dto.getAge()>=60 && dto.getAge()<=70,
                dto -> dto.getAge()>=70 && dto.getAge()<=80,
                dto -> dto.getAge()>=80 && dto.getAge()<=90,
                dto -> dto.getAge()>=90 && dto.getAge()<=100
        );
        map.put("predicate", pList);

        //2) random 데이터(AddressDTO) 30개를 저장, 10개의 Predicate 를 하나씩 꺼내어 30개의 random 데이터 검증하고

        List<AddressDTO> addList = new ArrayList<>();
        for(int i=0; i<30; i++) {
            AddressDTO dto = AddressDTO.builder()
                    .age((int) (Math.random() * 100) + 1)
                    .gu("gu"+i)
                    .city("city"+i)
                    .dong("city"+i)
                    .ano((long)(i+1))
                    .name("name_"+i)
                    .build();
            addList.add(dto);
        }


//        Map<String, List<>>

        pList.forEach(p -> {
            addList.forEach(dto -> p.test(dto));
        });


        //{"city":[], "gu":[], "age":[]}


    }
}
