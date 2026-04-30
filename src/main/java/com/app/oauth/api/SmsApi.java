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
public class SmsApi {

    private final AuthService authService;
    // ✅ ApiResponseDTO 필드 제거

    // 핸드폰 전송
    @PostMapping("/phone/verification-code")
    public ResponseEntity<ApiResponseDTO<?>> sendMemberPhoneVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ) {
        String memberPhone = verificationRequestDTO.getMemberPhone();

        // ✅ of(boolean, String) 시그니처로 통일
        ApiResponseDTO<?> response = authService.sendMemberPhoneVerificationCode(memberPhone)
                ? ApiResponseDTO.of(true, "메세지가 발송되었습니다.")
                : ApiResponseDTO.of(false, "휴대폰 번호를 확인해주세요.");

        return ResponseEntity.ok(response);
    }

    // 핸드폰 인증 코드 검증
    @PostMapping("/phone/verification-code/verify")
    public ResponseEntity<ApiResponseDTO<?>> verifyMemberPhoneVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ) {
        String memberPhone = verificationRequestDTO.getMemberPhone();
        String code = verificationRequestDTO.getCode();

        ApiResponseDTO<?> response = authService.verifyMemberPhoneVerificationCode(memberPhone, code)
                ? ApiResponseDTO.of(true, "인증이 완료되었습니다.")
                : ApiResponseDTO.of(false, "인증번호를 확인해주세요.");

        return ResponseEntity.ok(response);
    }

    // 이메일 전송
    @PostMapping("/email/verification-code")
    public ResponseEntity<ApiResponseDTO<?>> sendEmailVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ) {
        String memberEmail = verificationRequestDTO.getMemberEmail();

        ApiResponseDTO<?> response = authService.sendEmailVerificationCode(memberEmail)
                ? ApiResponseDTO.of(true, "이메일이 발송되었습니다.")
                : ApiResponseDTO.of(false, "이메일을 확인해주세요.");

        return ResponseEntity.ok(response);
    }

    // 이메일 인증 코드 검증
    @PostMapping("/email/verification-code/verify")
    public ResponseEntity<ApiResponseDTO<?>> verifyEmailVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ) {
        String memberEmail = verificationRequestDTO.getMemberEmail();
        String code = verificationRequestDTO.getCode();

        ApiResponseDTO<?> response = authService.verifyEmailVerificationCode(memberEmail, code)
                ? ApiResponseDTO.of(true, "인증이 완료되었습니다.")
                : ApiResponseDTO.of(false, "인증번호를 확인해주세요.");

        return ResponseEntity.ok(response);
    }
}