package com.app.oauth.repository;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.vo.MemberVO;
import com.app.oauth.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDAO {
    private final MemberMapper memberMapper;

    //    회원 추가
    public void save(MemberVO memberVO) {
        memberMapper.insert(memberVO);
    }

    //    회원 조회(Id)
    public Optional<MemberDTO> findMemberById(Long id){
        return Optional.ofNullable(memberMapper.select(id));
    }

    //    회원 조회(memberEmail)
    public Optional<MemberDTO> findMemberByMemberEmail(String memberEmail){
        return Optional.ofNullable(memberMapper.selectByMemberEmail(memberEmail));
    }

    //    회원 가입 여부 조회(memberEmail)
    public boolean existsMemberByMemberEmail(String memberEmail){
        return memberMapper.existsMemberByMemberEmail(memberEmail);
    }

    //    회원 수정
    public void update(MemberVO memberVO){
        memberMapper.update(memberVO);
    }

    //    회원 삭제
    public void delete(Long id){
        memberMapper.delete(id);
    }



}
