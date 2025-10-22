package com.green.blue.red.service;

import com.green.blue.red.domain.Member;
import com.green.blue.red.domain.MemberRole;
import com.green.blue.red.dto.MemberDTO;
import com.green.blue.red.dto.MemberModifyDTO;
import com.green.blue.red.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();

        for(int i=0; i<10; i++) {
            buffer.append((char) ((int)(Math.random()*55)+65));
        }
        return buffer.toString();
    }

    private Member makeSocialMember(String email) {
        String tempPassword = makeTempPassword();

        log.info("tempPassword: " + tempPassword);
        String nickname = "소셜회원";

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();
        member.addRole(MemberRole.USER);
        return member;
    }


    @Override
    public MemberDTO getKakaoMember(String accessToken) {

        String email = getEmailFormKakaoAccessToken(accessToken);
        log.info("1) email:{}", email);
        Optional<Member> result = memberRepository.findById(email);

        //기존 회원이라면
        if(result.isPresent()) {
            MemberDTO memberDTO = entityToDTO(result.get());
            return memberDTO;
        }
        //회원이 아니었다면 닉네임은 '소셜회원'으로 패스워드는 임의로 생성
        Member socialMember = makeSocialMember(email);
        memberRepository.save(socialMember);
        MemberDTO memberDTO = entityToDTO(socialMember);
        return memberDTO;
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());
        Member member = result.orElseThrow();

        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeSocial(false);
        member.changeNickname(memberModifyDTO.getNickname());

        memberRepository.save(member);
    }

    private String getEmailFormKakaoAccessToken(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
        if(accessToken==null) throw new RuntimeException("Access Token is null");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        headers.add("Content-Type","application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromUriString(kakaoGetUserURL).build();
        ResponseEntity<LinkedHashMap> response
                = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);
        log.info("res : {}", response);
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("------");
        log.info("bodyMap: {}", bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");
        log.info("kakaoAccount: {}", kakaoAccount);
        return kakaoAccount.get("email");


    }
}
