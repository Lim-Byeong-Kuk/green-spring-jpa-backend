package com.green.blue.red.test;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class TestMain3 {

    public static void main(String[] args) {
        Map<String, AddressDTO> map = new LinkedHashMap<>();

        for(int i=0; i<10; i++) {
            map.put("a" + i, AddressDTO.builder()
                            .ano((long)i+1)
                            .age(i+10)
                            .city("city"+i)
                            .gu("gu"+i)
                            .dong("dong"+i)
                            .name("name"+i)
                    .build());
        }

        AddressDTO dto = map.computeIfAbsent("bb", k -> AddressDTO.builder()
                .ano(100l)
                .age(5)
                .name("bname")
                .city("Bcity")
                .gu("Bgu")
                .dong("Bdong")
                .build());

        map.put("bb",dto);

    log.info("map : {}", map);

    }
}
