package com.green.blue.red.test;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
public class MainTest4 {

    private static String gugu (String s, int i) {
        String merge="";
        for(int j=0; j<i; j++) {
            merge+=s;
        }
        return merge;
    }

    private static String generateGu(List<String> dongStr, int repeat) {
        String gu = "";
        for (int i=0; i<repeat; i++) {
            gu+=dongStr.get((int)(Math.random()* dongStr.size()));
        }
        return gu;
    }

    public static void main(String[] args) {

        Map<String, List<Predicate<AddressDTO>>> map = new HashMap<>();

        List<Predicate<AddressDTO>> fuList = Arrays.asList(
                (Predicate<AddressDTO>) i  -> i.getDong().length()>3,
                (Predicate<AddressDTO>)  i -> gugu(i.getGu(), i.getGu().length()).startsWith("도"),
                (Predicate<AddressDTO>)  i -> (i.getDong()+i.getGu()+i.getCity()).trim().toLowerCase().split(",")[0].indexOf("f")==3,
                i -> i.getAge()>i.getAno(),
                i -> i.getAge() <= i.getAno(),
                i -> i.getAge() == i.getAno(),
                i -> i.getName().length() > i.getAno(),
                i -> i.getName().length() > i.getAno(),
                i -> i.getCity().equals(i.getGu()),
                i -> !i.getCity().equals(i.getGu())
        );
        map.put("fn", fuList);

        String[] dong = new String[30];
        String[][] addressArr = new String[4][30];
        String[] data = {"도시","구","동","bk"};
        List<String> dongStr = Arrays.asList("a","b","c","d","e","f");

        for(int j=0; j<4; j++) {
            for(int i=0; i<addressArr[j].length; i++) {
                addressArr[j][i] = data[j]+">>"+generateGu(dongStr,(int)(Math.random()*20+1));
            }
        }


        // List<AddressDTO> 생성
        List<AddressDTO> dtoList = new ArrayList<>();
        for(int i=0; i < addressArr[0].length; i++) {
            AddressDTO dto = AddressDTO.builder()
                    .ano((long)(i+1))
                    .city(addressArr[0][i])
                    .gu(addressArr[1][i])
                    .dong(addressArr[2][i])
                    .name(addressArr[3][i])
                    .age((i+1)*7)
                    .build();
            dtoList.add(dto);
        }

        // predicate 적용
        Map<String, List<ResultDTO>> resultmap = new HashMap<>();


        List<ResultDTO> resultDTOS = new ArrayList<>();
        map.get("fn").forEach(p -> {
            dtoList.forEach(dto -> {
                List<ResultDTO> cities = resultmap.computeIfAbsent("city", k -> new ArrayList<>());
                List<ResultDTO> gus = resultmap.computeIfAbsent("gu", k -> new ArrayList<>());
                List<ResultDTO> ages = resultmap.computeIfAbsent("age", k -> new ArrayList<>());
                ResultDTO resultDTO = ResultDTO.builder().build();
                if(p.test(dto)) {
                     resultDTO = ResultDTO.builder()
                            .cityAndGuAndDong(dto.getCity() + dto.getGu() + dto.getDong())
                            .anoAndAge((int) (dto.getAno() + dto.getAge()))
                            .build();

                    if(resultDTO.getAnoAndAge()>20) {
                        cities.add(resultDTO);
                    } else {
                        gus.add(resultDTO);
                    }
                } else {
                    ages.add(resultDTO);
                }
            });
        });

        log.info("resultmap => {}",resultmap);



    }
}
