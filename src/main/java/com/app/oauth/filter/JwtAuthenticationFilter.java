package com.app.oauth.filter;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.dto.response.MemberResponseDTO;
import com.app.oauth.exception.MemberException;
import com.app.oauth.repository.MemberDAO;
import com.app.oauth.service.MemberService;
import com.app.oauth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberDAO memberDAO;

    // /private으로 시작하는 모든 경로는 검사
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return !path.startsWith("/private");
    }

    private String getAccessTokenFromCookie(HttpServletRequest request){
        if(request.getCookies() == null){
            return null;
        }

        for(Cookie cookie : request.getCookies()){
            if("accessToken".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = getAccessTokenFromCookie(request);
        String memberEmail = null;
        String socialMemberProvider = null;

        if(accessToken != null){
            Claims claims = jwtTokenUtil.parseToken(accessToken);
            memberEmail = (String)claims.get("memberEmail");
            socialMemberProvider = (String)claims.get("socialMemberProvider");
        }

        // 중복 인증방지
        if(memberEmail != null && socialMemberProvider != null && SecurityContextHolder.getContext().getAuthentication() == null){
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberEmail(memberEmail);
            memberDTO.setSocialMemberProvider(socialMemberProvider);
            MemberDTO foundMember = memberDAO.findMemberByMemberEmailAndSocialMemberProvider(memberDTO)
                    .orElseThrow(() -> {throw new MemberException("doFilterInternal 회원 조회 실패", HttpStatus.BAD_REQUEST);});

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(foundMember, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
