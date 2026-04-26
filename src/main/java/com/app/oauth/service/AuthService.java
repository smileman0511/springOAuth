package com.app.oauth.service;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;

public interface AuthService {

    // 로컬 로그인
    public JwtTokenDTO login(MemberDTO memberDTO);

    // 소셜 로그인
    public void socialLogin(MemberDTO memberDTO);

    // Redis에 refresh Token 저장
    // Redis에 저장된 refresh Token이 유효한지 검증
    // Redis에 블랙리스트를 등록 (로그아웃 시 토큰 무효화)
    // Redis에 등록된 블랙리스트인지 검증
    // refresh 토큰을 검증하고, 새로운 accessToken 발급 서비스


    // refresh 토큰을 검증하고, 새로운 accessToken 발급 서비스
    public JwtTokenDTO reissueAccessToken(JwtTokenDTO jwtTokenDTO);


}
