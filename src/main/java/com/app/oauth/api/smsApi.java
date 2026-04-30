package com.app.oauth.api;

import com.app.oauth.domain.dto.request.VerificationRequestDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class smsApi {

    private final AuthService authService;
    private final ApiResponseDTO apiResponseDTO;

    // 핸드폰 전송
    @PostMapping("/phone/verification-code")
    public ResponseEntity<ApiResponseDTO> sendMemberPhoneVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ){
        String memberPhone = verificationRequestDTO.getMemberPhone();
        ApiResponseDTO apiResponseDTO = null;
        if(authService.sendMemberPhoneVerificationCode(memberPhone)){
            apiResponseDTO = ApiResponseDTO.of("메세지가 발송되었습니다.");
        }else {
            apiResponseDTO = ApiResponseDTO.of("휴대폰 번호를 확인해주세요.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    // 핸드폰 인증 코드 검증
    @PostMapping("/phone/verification-code/verify")
    public ResponseEntity<ApiResponseDTO> verifyMemberPhoneVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ){
        String memberPhone = verificationRequestDTO.getMemberPhone();
        String code = verificationRequestDTO.getCode();
        ApiResponseDTO apiResponseDTO = null;
        if(authService.verifyMemberPhoneVerificationCode(memberPhone, code)){
            apiResponseDTO = ApiResponseDTO.of(true, "인증이 완료되었습니다.");
        }else {
            apiResponseDTO = ApiResponseDTO.of(false, "인증번호를 확인해주세요.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    // 이메일 전송
    @PostMapping("/email/verification-code")
    public ResponseEntity<ApiResponseDTO> sendEmailVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ){
        String memberEmail = verificationRequestDTO.getMemberEmail();
        ApiResponseDTO apiResponseDTO = null;
        if(authService.sendEmailVerificationCode(memberEmail)){
            apiResponseDTO = ApiResponseDTO.of("이메일 발송");
        }else {
            apiResponseDTO = ApiResponseDTO.of("이메일을 확인해주세요.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    @PostMapping("/email/verification-code/verify")
    public ResponseEntity<ApiResponseDTO> verifyEmailVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ){
        String memberEmail = verificationRequestDTO.getMemberEmail();
        String code = verificationRequestDTO.getCode();
        ApiResponseDTO apiResponseDTO = null;
        if(authService.verifyEmailVerificationCode(memberEmail, code)){
            apiResponseDTO = ApiResponseDTO.of(true, "인증이 완료되었습니다.");
        }else {
            apiResponseDTO = ApiResponseDTO.of(false, "인증번호를 확인해주세요.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }
}
