package com.app.oauth.config;

import com.app.oauth.filter.JwtAuthenticationFilter;
import com.app.oauth.handler.Oauth2LoginSuccessHandler;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // 시큐리티 검증 경로
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
                        .logoutSuccessUrl("http://localhost:3000/login")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 세션을 비우든, 토큰을 삭제하는 코드 들
                            HttpSession session = request.getSession(false);
                            if(session != null) {
                                session.invalidate();
                            }

                            response.sendRedirect("http://localhost:3000/login");
                        })
                        .permitAll()
                );

        return http.build();
    }
}
