package com.green.blue.red.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTUtil {

    private static String key = "1234567890123456789012345678901234567890";


    public static String generateToken(Map<String, Object> valueMap, int min) {

        SecretKey key = null;

        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ","JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();

        return jwtStr;
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;

        try {
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) //파싱 및 검증, 실패 시 에러
                    .getBody();
        } catch (MalformedJwtException malformedJwtException) {
            // 토큰의 형식이 잘못되었을 때 (서명이 훼손되었거나 구조가 올바르지 않을 때)
            throw new CustomJWTException("Malformed");
        } catch (ExpiredJwtException expiredJwtException) {
            // 토큰의 유효기간이 만료 되었을 때
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            // 클레임(정보)이 유효하지 않을 때
            throw new CustomJWTException("Invalid");
        } catch (JwtException jwtException) {
            // 위의 경우를 제외한 일반적인 JWT 관련 오류
            throw new CustomJWTException("JWTError");
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }
        return claim;
    }


}
