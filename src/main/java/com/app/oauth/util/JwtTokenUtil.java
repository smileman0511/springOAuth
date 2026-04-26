package com.app.oauth.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    // Access 토큰 생성
    public String generateAccessToken(Map<String, String> claims) {
        // 평균 1분 ~ 5분(수업 테스트용 24시간)
//        long expirationTimeInMillis = 1000 * 60 * 60 * 24;
        Long expirationTimeInMillis = 1000L * 10;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMillis);

        return Jwts
                .builder()
                .claims(claims) // 클레임 추가
                .expiration(expirationDate) // 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // sha-256 알고리즘
                .setHeaderParam("typ", "JWT") // 타입 JWT
                .compact();
    }

    // generateRefreshToken
    public String generateRefreshToken(Map<String, String> claims) {
        // (평균 1주일 ~ 한 달)
        Long expirationTimeInMillis = 1000L * 60 * 60 * 24 * 30;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMillis);

        return Jwts
                .builder()
                .claims(claims) // 클레임 추가
                .expiration(expirationDate) // 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // sha-256 알고리즘
                .setHeaderParam("typ", "JWT") // 타입 JWT
                .compact();
    }


    // 토큰 파싱(token -> claim)
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException();
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException();
        } catch (MalformedJwtException e) {
            throw new RuntimeException();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }
    }

    // 토큰 유효성 검사
    public Map<String, Object> validateToken(String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            result.put("success", true);
            result.put("message", "토큰 파싱 완료");
            return result;

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }

}
