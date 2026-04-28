package com.app.oauth.domain.dto.response;

import com.app.oauth.domain.dto.MemberDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MemberResponseDTO {
    private Long id;
    private String memberEmail;
    private String memberPicture;
    private String memberName;
    private String memberNickname;
    private String socialMemberProviderId;
    private String socialMemberProvider;
    private Long memberId;

    public static MemberResponseDTO from(MemberDTO memberDTO){
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        memberResponseDTO.setId(memberDTO.getId());
        memberResponseDTO.setMemberEmail(memberDTO.getMemberEmail());
        memberResponseDTO.setMemberPicture(memberDTO.getMemberPicture());
        memberResponseDTO.setMemberName(memberDTO.getMemberName());
        memberResponseDTO.setMemberNickname(memberDTO.getMemberNickname());
        memberResponseDTO.setSocialMemberProviderId(memberDTO.getSocialMemberProviderId());
        memberResponseDTO.setSocialMemberProvider(memberDTO.getSocialMemberProvider());
        memberResponseDTO.setMemberId(memberDTO.getMemberId());
        return memberResponseDTO;
    }

}
