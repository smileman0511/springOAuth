package com.app.oauth.api;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.service.MemberService;
import com.app.oauth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

    // 일반 로그인의 회원가입 경로
    @PostMapping("/join")
    public ResponseEntity<ApiResponseDTO> join(@RequestBody() MemberDTO memberDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.join(memberDTO));
    }

    // 토큰 정보로 데이터 파싱 후 화면에 응답
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO> me(
            @CookieValue(name = "accessToken", required = false) String accessToken
    ){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.me(accessToken));
    }

}











