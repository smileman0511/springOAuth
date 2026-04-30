package com.app.oauth.domain.dto.request;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class VerificationRequestDTO {

    private String memberEmail;
    private String memberPhone;
    private String code;

}
