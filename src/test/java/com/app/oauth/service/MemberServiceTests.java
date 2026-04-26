package com.app.oauth.service;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthService authService;

    @Test
    public void joinTest(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("test1234@gmail.com");
        memberDTO.setMemberPassword("test123!@#");
        memberDTO.setMemberName("홍길동");
        memberDTO.setMemberNickname("개복치 홍길동");
        memberService.join(memberDTO);
    }

    @Test
    public void loginTest(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("test123@gmail.com");
        memberDTO.setMemberPassword("test123!@#");
        memberDTO.setMemberName("홍길동");
        memberDTO.setMemberNickname("개복치 홍길동");

        log.info("{}", authService.login(memberDTO));
    }


}
