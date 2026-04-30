package com.app.oauth.handler;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if(authentication instanceof OAuth2AuthenticationToken authToken){
            OAuth2User oauth2User = authToken.getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();
            String socialMemberProvider = authToken.getAuthorizedClientRegistrationId();

            String memberEmail = null;
            String socialMemberProviderId = null;
            String memberName = null;

            if("google".equals(socialMemberProvider)){
                memberEmail = (String)attributes.get("email");
                socialMemberProviderId = (String)attributes.get("sub");
                memberName = (String)attributes.get("name");
            }else if("kakao".equals(socialMemberProvider)){
                socialMemberProviderId = String.valueOf(attributes.get("id"));
                Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

                memberEmail = (String)kakaoAccount.get("email");
                memberName = (String)profile.get("nickname");

            }else if("naver".equals(socialMemberProvider)){
                Map<String, Object> naverResponse = (Map<String, Object>) attributes.get("response");
                socialMemberProviderId = (String)naverResponse.get("id");
                memberEmail = (String)naverResponse.get("email");
                memberName = (String)naverResponse.get("name");
            }

            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberEmail(memberEmail);
            memberDTO.setSocialMemberProviderId(socialMemberProviderId);
            memberDTO.setMemberName(memberName);
            memberDTO.setSocialMemberProvider(socialMemberProvider);

            JwtTokenDTO jwtTokenDTO = authService.socialLogin(memberDTO);

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
                    .secure(false) // 개발 환경 false, 배포 환경 true (http <-> https)
                    .maxAge(60 * 60 * 24 * 30) // 쿠키 만료 기간
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            String redirectUrl = "http://localhost:3000";
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }

    }
}
