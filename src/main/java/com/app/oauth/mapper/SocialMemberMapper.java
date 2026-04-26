package com.app.oauth.mapper;

import com.app.oauth.domain.vo.SocialMemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SocialMemberMapper {
    //    회원 추가
    public void insert(SocialMemberVO socialMemberVO);
}
