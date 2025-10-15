package com.green.blue.red.test;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class MainTest3 {

    public static void main(String[] args) {

        String[] cities = {"서울","제주","안산","인천","부산","대구","대전","춘천"};
        String[] categories = {"미취학아동", "초등학생", "중학생", "고등학생", "20대", "30대", "40대", "50대", "60대", "70대"};

        Map<String, List<Predicate<AddressDTO>>> map = new HashMap<>();

        //1) Predicate 10개 임의로 만들기
        List<Predicate<AddressDTO>> pList = List.of(
                dto -> dto.getAge()>=0 && dto.getAge()<=7,
                dto -> dto.getAge()>=8 && dto.getAge()<=13,
                dto -> dto.getAge()>=14 && dto.getAge()<=16,
                dto -> dto.getAge()>=17 && dto.getAge()<=19,
                dto -> dto.getAge()>=20 && dto.getAge()<=29,
                dto -> dto.getAge()>=30 && dto.getAge()<=39,
                dto -> dto.getAge()>=40 && dto.getAge()<=49,
                dto -> dto.getAge()>=50 && dto.getAge()<=59,
                dto -> dto.getAge()>=60 && dto.getAge()<=69,
                dto -> dto.getAge()>=70 && dto.getAge()<=79
        );
        map.put("predicate", pList);

        //2) random 데이터(AddressDTO) 30개를 저장, 10개의 Predicate 를 하나씩 꺼내어 30개의 random 데이터 검증하고

        List<AddressDTO> addList = new ArrayList<>();
        for(int i=0; i<30; i++) {
            AddressDTO dto = AddressDTO.builder()
                    .age((int) (Math.random() * 100) + 1)
                    .gu("gu"+i)
                    .city(cities[(int)(Math.random()* cities.length)]+i)
                    .dong("dong"+i)
                    .ano((long)(i+1))
                    .name("name_"+i)
                    .build();
            addList.add(dto);
        }

        Map<String, List<AddressDTO>> resultmap = new LinkedHashMap<>();

        addList.forEach(dto -> {

            IntStream.range(0, categories.length).boxed()
                    .forEach(i -> {
                        if(pList.get(i).test(dto)) {
                            resultmap.computeIfAbsent(categories[i], key -> new ArrayList<>())
                                    .add(dto);
                        }
                    });
        });

//        System.out.println(resultmap);

        LinkedHashMap<String, List<AddressDTO>> sortedResult = resultmap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(i -> i.get(0).getAge())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (k1, k2) -> k1,
                        LinkedHashMap::new
                ));

        System.out.println(sortedResult);

        //{"city":[], "gu":[], "age":[]}




    }
}
