package com.app.oauth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private")
@Slf4j
public class MyPageApi {

    // "/private/*"으로 시작하는 모든 경로는 Token을 header에서 담아서 요청하게 만든다.
    // 그리고 한번에 추출한다
    @PostMapping("/my-page-test")
    public void test(Authentication authentication){
        log.info("test", authentication.getPrincipal().toString());
    }
}
