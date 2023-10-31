package CDProject.vfmarket.service;

import CDProject.vfmarket.vo.MailVo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.host}")
    String host;
    @Value("${spring.mail.port}")
    Integer port;
    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.mail.password}")
    String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    Boolean smtpAuth;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    Integer timeout;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    Boolean starttlsEnable;
    private final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    private static final String senderEmail = "trace1997@ajou.ac.kr";
    private static int number;

    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage createMail(String receiver){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mailVo.getReceiver());
//        message.setFrom(senderEmail);
//        message.setSubject(mailVo.getTitle());
//        message.setText(mailVo.getContent());
//
//        javaMailSender.send(message);
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, receiver);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String receiver){
        MimeMessage message = createMail(receiver);

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", smtpAuth);
        prop.put("mail.smtp.timeout", timeout);
        prop.put("mail.smtp.starttls.enable", starttlsEnable);

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(prop);

        javaMailSender.send(message);

        return number;
    }
}
