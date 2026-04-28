package com.app.oauth.mapper;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    //    회원 추가
    public void insert(MemberVO memberVO);
    //    회원 조회(ID)
    public MemberDTO select(Long id);
    //    회원 조회(memberEmail)
    public MemberDTO selectByMemberEmailAndSocialMemberProvider(MemberDTO memberDTO);
    //    회원 가입 여부 조회(memberEmail)
    public boolean existsMemberByMemberEmailAndSocialMemberProvider(MemberDTO memberDTO);
    //    회원 수정
    public void update(MemberVO memberVO);
    //    회원 삭제
    public void delete(Long id);
}
