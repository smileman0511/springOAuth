package com.app.oauth.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtTokenDTO {
    private String accessToken;
    private String refreshToken;
}
