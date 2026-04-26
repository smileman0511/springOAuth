package com.app.oauth.util;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class JwtTokenUtilTests {
    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    @Test
    public void generateTokenTest(){

        Map<String, String> claims = new HashMap<>();
        claims.put("id", "1");
        claims.put("memberEmail", "hong123@gmail.com");

        String accessToken = jwtTokenUtil.generateAccessToken(claims);
        String refreshToken = jwtTokenUtil.generateRefreshToken(claims);
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);
    }

    @Test
    public void parseTokenTest(){
        Map<String, String> claims = new HashMap<>();
        claims.put("id", "1");
        claims.put("memberEmail", "hong123@gmail.com");
        String accessToken = jwtTokenUtil.generateAccessToken(claims);

        Claims parseClaims = jwtTokenUtil.parseToken(accessToken);
        log.info("parseClaims : {}", parseClaims);
    }

    @Test
    public void validateTokenTest(){
        Map<String, String> claims = new HashMap<>();
        claims.put("id", "1");
        claims.put("memberEmail", "hong123@gmail.com");
        String accessToken = jwtTokenUtil.generateAccessToken(claims);

        log.info("{}", jwtTokenUtil.validateToken("이규학"));
    }

}
