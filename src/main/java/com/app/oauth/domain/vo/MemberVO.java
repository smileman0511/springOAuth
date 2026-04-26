package com.app.oauth.domain.vo;

import com.app.oauth.domain.dto.MemberDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MemberVO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberPicture;
    private String memberName;
    private String memberNickname;

    public static MemberVO from(MemberDTO memberJoinDTO){
        MemberVO memberVO = new MemberVO();
        memberVO.setId(memberJoinDTO.getId());
        memberVO.setMemberEmail(memberJoinDTO.getMemberEmail());
        memberVO.setMemberPassword(memberJoinDTO.getMemberPassword());
        memberVO.setMemberPicture(memberJoinDTO.getMemberPicture() != null ? memberJoinDTO.getMemberPicture() : "/default.jpg");
        memberVO.setMemberName(memberJoinDTO.getMemberName());
        memberVO.setMemberNickname(memberJoinDTO.getMemberNickname() != null ? memberJoinDTO.getMemberNickname() : "개복치 1단계");
        return memberVO;
    }
}
