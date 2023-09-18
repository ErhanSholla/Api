package com.example.ecommerceapi.service;

import com.example.ecommerceapi.entity.User;
import com.example.ecommerceapi.entity.VerificationToken;
import com.example.ecommerceapi.exception.EmailFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSentService {
    @Value("${email.from}")
    private String fromAddress;
    @Value("${app.frontend.url}")
    private String url;
    private final JavaMailSender javaMailSender;
    private SimpleMailMessage simpleMailMessage(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }
    public void setVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage simpleMailMessage = simpleMailMessage();
        simpleMailMessage.setTo(verificationToken.getUser().getEmail());
        simpleMailMessage.setSubject("Verify your email to activate your account");
        simpleMailMessage.setText("Please follow the link to activate your account.\n" +
                url+ "auth/verify?token="+verificationToken.getToken());
        try{
            javaMailSender.send(simpleMailMessage);
        }catch (MailException ex){
            throw new EmailFailureException();
        }
    }
    public void sentPasswordResetEmail(User user, String token) throws EmailFailureException{
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Your Password Request Link");
        simpleMailMessage.setText("Dear " + user.getFirstname() + ", \n You have required to reset password. " +
                "Please find the link above to reset password. \n" + url+"aut/reset?token="+token);
        try {
            javaMailSender.send(simpleMailMessage);
        }catch (MailException exception){
            throw new EmailFailureException();
        }

    }
}
