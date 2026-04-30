package com.app.oauth.util;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsUtil {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    private DefaultMessageService messageService;
    private final JavaMailSenderImpl mailSender;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendOneMemberPhone(String to, String content){
        Message message = new Message();

        // "01012341234" <- 형태로 전송해야 함.
        String toPhoneNumber = to.replaceAll("-", "");

        message.setTo(toPhoneNumber);
        message.setFrom("01047099813");
        message.setText(content);

        SingleMessageSentResponse response = this
                .messageService
                .sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

    // 이메일 전송
    public void sendMemberEmail(String to, String subject, String content){
        MimeMessage mineMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mineMessage, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject); // 제목
            helper.setText(content); // 내용
            helper.setFrom("codefuling@gmail.com", "살이 오른 제철 뀨잉"); // 보낸 이메일 , 보낸 사람 이름

            mailSender.send(mineMessage);

        } catch (Exception e) {
            throw new RuntimeException("메일 전송 실패 " + e.getMessage());
        }

    }

//    화면에 리액트 이메일을 입력 후
//    인증번호 전송 버튼을 누르면 이메일 발송하는 코드 완성하기
//    인증 확인 버튼 누르면 인증 확인

}












