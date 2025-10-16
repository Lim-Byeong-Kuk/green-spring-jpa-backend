package com.green.blue.red.repository;

import com.green.blue.red.domain.Member;
import com.green.blue.red.domain.MemberRole;
import com.green.blue.red.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Test
    public void testInsertMember() {

        for(int i=0; i<10; i++) {
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("USER"+i)
                    .build();
            member.addRole(MemberRole.USER);
            if(i>=5) {
                member.addRole(MemberRole.MANAGER);
            }
            if(i>=8) {
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        }
    }

    @Test
    public void testRead() {
        // 반복문 활용하여 user1, user9 까지 조회하고 MemberDTO 를 만들어서 저장

        // {"user":[], "manager":[], "admin":[]}

        // 대응되는 eamil 저장

        List<MemberDTO> dtos = new ArrayList<>();
        for(int i=0; i<=9; i++) {
            String email = "user"+i+"@aaa.com";
            Member member = memberRepository.getWithRoles(email);
            MemberDTO dto = mapper.map(member, MemberDTO.class);
            dtos.add(dto);
        }

        Map<String, List<String>> resultmap = new HashMap<>();

        for(MemberDTO dto : dtos) {
            if(dto.getMemberRoleList().contains(MemberRole.ADMIN)) {
                List<String> findedList = resultmap.getOrDefault("admin", new ArrayList<>());
                findedList.add(dto.getEmail());
                resultmap.put("admin", findedList);
            }
            else if (dto.getMemberRoleList().contains(MemberRole.MANAGER)){
                List<String> findedList = resultmap.getOrDefault("manager", new ArrayList<>());
                findedList.add(dto.getEmail());
                resultmap.put("manager",findedList);
            }
            else if (dto.getMemberRoleList().contains(MemberRole.USER)) {
                List<String> findedList = resultmap.getOrDefault("user", new ArrayList<>());
                findedList.add(dto.getEmail());
                resultmap.put("user", findedList);
            }
        }

        System.out.println("dtos = " + dtos);

        System.out.println("resultmap : " +resultmap);

    }
}
