package com.green.blue.red.util;

import com.green.blue.red.domain.MemberRole;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JWTUtilTests {

    private final Map<String, Object> testClaims = Map.of(
            "email", "user1@aaa.com",
            "pw","1111",
            "nickname","USER1",
            "roleNames", List.of(MemberRole.USER)
    );


    @Test
    public void jwtutilTest() {
        String jwt = JWTUtil.generateToken(testClaims, 5);
        System.out.println("jwt : " + jwt);
    }
}
