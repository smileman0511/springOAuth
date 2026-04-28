package com.app.oauth.service;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.response.MemberResponseDTO;
import com.app.oauth.domain.vo.MemberVO;
import com.app.oauth.domain.vo.SocialMemberVO;
import com.app.oauth.exception.MemberException;
import com.app.oauth.repository.MemberDAO;
import com.app.oauth.repository.SocialMemberDAO;
import com.app.oauth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO;
    private final SocialMemberDAO socialMemberDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    //    회원 가입
    @Override
    public ApiResponseDTO join(MemberDTO memberDTO) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();
        Map<String, Object> claims = new HashMap<>();

        if(memberDAO.existsMemberByMemberEmailAndSocialMemberProvider(memberDTO)){
            throw new MemberException("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }

        MemberVO memberVO = MemberVO.from(memberDTO);
        SocialMemberVO socialMemberVO = SocialMemberVO.from(memberDTO);

//        socialMemberVO.getSocialMemberProvider().equals("local")
        if("local".equals(socialMemberVO.getSocialMemberProvider())){
            memberVO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
        }

        memberDAO.save(memberVO);
        socialMemberVO.setMemberId(memberVO.getId());

        socialMemberDAO.save(socialMemberVO);

        claims.put("id", memberVO.getId());
        claims.put("memberEmail", memberVO.getMemberEmail());
        claims.put("memberProvider", socialMemberVO.getSocialMemberProvider());

        apiResponseDTO.setSuccess(true);
        apiResponseDTO.setMessage("회원가입이 완료되었습니다.");
        apiResponseDTO.setData(claims);

        return apiResponseDTO;
    }

    @Override
    public JwtTokenDTO login(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public void socialLogin(MemberDTO memberDTO) {

    }

    // 토큰 -> 회원 정보를 조회하는 서비스
    @Override
    public ApiResponseDTO me(String token) {
        Claims claims = jwtTokenUtil.parseToken(token);
        Long id = Long.parseLong((String)claims.get("id"));

        MemberDTO foundMember = memberDAO.findMemberById(id)
                .orElseThrow(() -> {
                    throw new MemberException("me 회원 조회 실패", HttpStatus.BAD_REQUEST);
                });

        MemberResponseDTO memberResponseDTO = MemberResponseDTO.from(foundMember);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(true, "회원 조회 성공", memberResponseDTO);
        return apiResponseDTO;
    }
}
