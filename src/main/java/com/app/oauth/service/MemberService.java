package com.app.oauth.service;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;

public interface MemberService {
    // 회원가입
    public ApiResponseDTO join(MemberDTO memberDTO);

    // 로컬 로그인
    public JwtTokenDTO login(MemberDTO memberDTO);

    // 소셜 로그인
    public void socialLogin(MemberDTO memberDTO);

    // 토큰 -> 회원 정보 조회
    public ApiResponseDTO me(String token);

    // 회원 수정


    // 회원 탈퇴


}
