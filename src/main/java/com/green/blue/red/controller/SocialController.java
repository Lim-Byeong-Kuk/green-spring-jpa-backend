package com.green.blue.red.controller;

import com.green.blue.red.dto.MemberDTO;
import com.green.blue.red.dto.MemberModifyDTO;
import com.green.blue.red.service.MemberService;
import com.green.blue.red.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public ResponseEntity<Map<String, Object>> getMemberFromKakao(String accessToken) {

        log.info("access Token : {}", accessToken);
        MemberDTO memberDTO = memberService.getKakaoMember(accessToken);
        Map<String, Object> claims = memberDTO.getClaims();
        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
        String jwtRefreshToken = JWTUtil.generateToken(claims, 60*24);
        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return ResponseEntity.ok(claims);
    }

    @PutMapping("/api/member/modify")
    public Map<String,String> modify(@RequestBody MemberModifyDTO memberModifyDTO) {
        log.info("member modify : " + memberModifyDTO);
        memberService.modifyMember(memberModifyDTO);
        return Map.of("result", "modified");
    }

}
