package com.green.blue.red.controller;

import com.green.blue.red.util.CustomJWTException;
import com.green.blue.red.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class APIRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {

        log.info("/api/member/refresh 호출");
        if(refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if(authHeader == null || authHeader.length() <7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);
        //Access 토큰이 만료되지 않았다면
        if(checkExpiredToken(accessToken) == false) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        //Access Token 만료, Refresh Token 은 만료되지 않은 경우 -> 새로운 AccessToken
        // - Refresh Token 의 유효기간이 얼마 남지 않은 경우 -> 새로운 RefreshToken
        // - Refresh Token 의 유효 기한이 충분히 남은 경우 -> 기존의 Refresh Token

        //Refresh토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh... claims: " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);
        String newRefreshToken = checkTime((Integer)claims.get("exp")) == true ?
                JWTUtil.generateToken(claims, 60*24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    //시간이 1시감 미만으로 남았다면
    private boolean checkTime(Integer exp) {
        //JWT exp 를 날짜로 변환
        java.util.Date expDate = new java.util.Date((long) exp * (1000));
        //현재 시간과의차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();
        //분단위 계산
        long leftMin = gap / (1000*60);
        //1시간도 안남았는지...
        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if(ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}
