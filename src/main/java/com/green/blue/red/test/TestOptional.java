package com.green.blue.red.test;

import com.green.blue.red.domain.Member;
import com.green.blue.red.domain.MemberRole;
import com.green.blue.red.dto.MemberDTO;

import java.util.List;
import java.util.Optional;

public class TestOptional {

    public static void main(String[] args) {

        MemberDTO dto = new MemberDTO("abc@aaa.com","1234","nickname",false, List.of(MemberRole.USER.name()));

        Optional<MemberDTO> optionalDTO = Optional.of(dto);

        MemberDTO resultDTO = optionalDTO.orElseGet(() -> {
            MemberDTO m = new MemberDTO("a", "1", "n", false, List.of(MemberRole.USER.name()));
            return m;
        });


        String name2 = Optional.of("").orElseGet(() -> "test");
        System.out.println("name2 = " + name2);

        String name3 = Optional.of("kwonkihyun").filter(i->!(i.length()%3==0)).orElseGet(() -> "3의 배수가 아닙니다.");
        System.out.println("name3 = " + name3);

        List<String> strList = List.of("kwonkihyun", "jeonbyul", "johyeongwoo", "kimyura","shinsora","limbyeogkuk","parkjongmin","kangminseok","nasinyoung","kimkeonho","jeonjaeseok");

        List<Optional<Member>> optionalList = strList.stream().map(i -> Optional.of(Member.builder().nickname(i).build())).toList();

        optionalList.stream().map(o -> (o.orElseGet(() -> {
            return Member.builder().nickname("default").build();
                }).getNickname()))
                .forEach(str -> System.out.println(str.substring(0,1)+"로 시작하는 이름 = " + str));

//        strList.stream().filter(str-> !(str.startsWith("k"))).forEach(str -> {
//
//        });
//
//        String name4 = Optional.of(strList.get(0)).filter(i -> !(i.startsWith("k"))).orElseGet(() -> "첫글자가 k로 시작되지 않음");
//        System.out.println("name4 = " + name4);
    }
}
