package com.app.oauth.config;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.filter.JwtAuthenticationFilter;
import com.app.oauth.handler.Oauth2LoginSuccessHandler;
import com.app.oauth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final AuthService authService;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.setAllowCredentials(true); // 쿠키 사용 여부

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // 시큐리티 검증 경로
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(anonymous -> anonymous.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/private/**").authenticated() // "/private" -> 보호된 라우트
                        .anyRequest().permitAll() // 위 경로를 제외한 나머지 경로는 허용된 라우트
                ) // 모든 경로 해제
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2LoginSuccessHandler))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("http://localhost:3000/member/login")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 세션을 비우든, 토큰을 삭제하는 코드 들
                            HttpSession session = request.getSession(false);
                            if(session != null) {
                                session.invalidate();
                            }
                            // 로그아웃 서비스 -> 토큰 블랙리스트에 등록

//                            authService.login();


                            JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
                            if(request.getCookies() != null) {
                                for(Cookie cookie : request.getCookies()) {
                                    if("refreshToken".equals(cookie.getName())) {
                                        jwtTokenDTO.setRefreshToken(cookie.getValue());
                                    }
                                    if("accessToken".equals(cookie.getName())) {
                                        jwtTokenDTO.setAccessToken(cookie.getValue());
                                    }
                                }
                            }

                            authService.logout(jwtTokenDTO);

                            ResponseCookie refreshTokenCookie = ResponseCookie
                                    .from("refreshToken", null)
                                    .httpOnly(true) // XSS 공격 차단
                                    .sameSite("Lax") // CSRF 공격 차단
                                    .path("/")
                                    .secure(false) // 개발 환경 false, 배포 환경 true (http <-> https)
                                    .maxAge(0) // 쿠키 만료 기간
                                    .build();

                            ResponseCookie accessTokenCookie = ResponseCookie
                                    .from("accessToken", null)
                                    .httpOnly(true) // XSS 공격 차단
                                    .sameSite("Lax") // CSRF 공격 차단
                                    .path("/")
                                    .secure(false) // 개발 환경 false, 배포 환경 true (http <-> https)
                                    .maxAge(0) // 쿠키 만료 기간
                                    .build();

                            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
                            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

                            response.sendRedirect("http://localhost:3000/member/login");
                        })
                        .permitAll()
                );

        return http.build();
    }
}
