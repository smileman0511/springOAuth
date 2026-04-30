package com.app.oauth.api;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.vo.MemberVO;
import com.app.oauth.service.MemberService;
import com.app.oauth.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/api/members")
@RequiredArgsConstructor
public class MemberPrivateApi {

    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

    // 일반 로그인의 회원가입 경로
    @PostMapping("/join")
    public ResponseEntity<ApiResponseDTO> join(@RequestBody() MemberDTO memberDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.join(memberDTO));
    }

    // 토큰 정보로 데이터 파싱 후 화면에 응답
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO> me(Authentication authentication){
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(memberService.me(memberDTO.getId()));
    }

    @PostMapping("/profile-update")
    public ResponseEntity<ApiResponseDTO> profileUpdate(
            Authentication authentication,
            @RequestBody MemberVO memberVO
    ){
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        memberVO.setId(memberDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updatePicture(memberVO));
    }


}











