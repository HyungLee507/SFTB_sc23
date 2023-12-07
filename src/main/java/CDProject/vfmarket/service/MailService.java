package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.entity.EmailVerification;
import CDProject.vfmarket.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private Boolean smtpAuth;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private Integer timeout;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private Boolean starttlsEnable;
    private final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    private final EmailVerificationRepository emailVerificationRepository;

    public int createNumber(){
        return (int)(Math.random() * (899999) + 100000);// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    @Transactional
    public void saveVerificationCode(String receiver, int sendCode){
        String verificationCode = String.valueOf(sendCode);

        EmailVerification emailVerification =
                EmailVerification.builder()
                        .email(receiver)
                        .verificationCode(verificationCode)
                        .build();

        emailVerificationRepository.save(emailVerification);
    }

    @Transactional
    public void deleteVerificationCode(EmailVerification emailVerification){
        emailVerificationRepository.delete(emailVerification);
    }


    @Transactional
    public EmailVerification findByEmail(String email){
        List<EmailVerification> emailVerificationList =
                emailVerificationRepository.findByEmailOrderByIdDesc(email);
        if(emailVerificationList.isEmpty()){
            return null;
        } else {
            return emailVerificationList.get(0);
        }
    }

    public MimeMessage createVerificationMail(String receiver, int sendCode){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, receiver);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + sendCode + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    @Async
    public void sendVerificationMail(String receiver, int sendCode){
        MimeMessage message = createVerificationMail(receiver, sendCode);

        configJavaMailSender();

        javaMailSender.send(message);
    }

    public MimeMessage createFindPasswordMail(String receiver, String verificationCode){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, receiver);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "비밀번호 찾기 요청에 따라, 인증번호를 발급해드립니다." + "</h3>";
            body += "<h1>" + verificationCode + "</h1>";
            body += "<h3>" + "제한시간 내로 인증을 완료하시길 바랍니다.. 감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    @Async
    public void sendFindPasswordMail(String receiver, String newPassword){
        MimeMessage message = createFindPasswordMail(receiver, newPassword);

        configJavaMailSender();

        javaMailSender.send(message);
    }

    public void configJavaMailSender(){
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", smtpAuth);
        prop.put("mail.smtp.timeout", timeout);
        prop.put("mail.smtp.starttls.enable", starttlsEnable);

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(senderEmail);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(prop);
    }
}