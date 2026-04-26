package com.app.oauth.api;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.service.AuthService;
import com.app.oauth.service.MemberService;
import com.app.oauth.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;

    // 일반 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@RequestBody() MemberDTO memberDTO, HttpServletResponse response) {
        JwtTokenDTO jwtTokenDTO = authService.login(memberDTO);

        // accessToken 쿠키
        ResponseCookie accessTokenCookie = ResponseCookie
                .from("accessToken", jwtTokenDTO.getAccessToken())
                .httpOnly(true) // XSS 공격 차단
                .sameSite("Lax") // CSRF 공격 차단
                .path("/")
                .secure(false) // 개발 환경 false, 배포 환경 true (http <-> https)
                .maxAge(60 * 60 * 24) // 쿠키 만료 기간
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", jwtTokenDTO.getRefreshToken())
                .httpOnly(true) // XSS 공격 차단
                .sameSite("Lax") // CSRF 공격 차단
                .path("/")
                .secure(false)
                .maxAge(60 * 60 * 24 * 30) // 쿠키 만료 기간
                .build();

        // 쿠키 심기
        return ResponseEntity
                .status(HttpStatus.OK)
                // 쿠키 심기
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString())
                .body(new ApiResponseDTO(true, "로그인 성공"));
    }
    // 소셜 로그인 -> security filter
    // 사용자 정보 조회
}
