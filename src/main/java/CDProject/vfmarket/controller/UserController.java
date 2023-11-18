package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.EmailVerificationResponseDto;
import CDProject.vfmarket.domain.dto.UserSignUpDto;
import CDProject.vfmarket.domain.entity.EmailVerification;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.global.login.service.LoginService;
import CDProject.vfmarket.service.MailService;
import CDProject.vfmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final LoginService loginService;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @PostMapping("/sign-up")
    @CrossOrigin(origins = "http://localhost:3000/")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        userService.signUp(userSignUpDto);
        return "회원 가입 성공";
    }

    @DeleteMapping("/sign-out")
    public String signOut(@RequestParam String email) {
        userService.signOut(email);
        return "회원 탈퇴 성공";
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

    @PostMapping("/mail-verify")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity mailVerify(String email){
        Optional<EmailVerification> optionalEmailVerification = mailService.findByEmail(email);
        if (optionalEmailVerification.isPresent()){
            return new ResponseEntity(HttpStatus.IM_USED);
        }

        int sendCode = mailService.createNumber();

        EmailVerificationResponseDto emailVerificationResponse =
                EmailVerificationResponseDto.builder()
                        .email(senderEmail)
                        .sendCode(sendCode)
                        .message("입력하신 이메일로 인증번호가 전송되었습니다.\n인증번호를 입력하세요.")
                        .build();

        mailService.sendVerificationMail(email, sendCode);  // 인증 코드 메일 전송

        mailService.saveVerificationCode(email, sendCode);  // 인증 코드 DB 저장

        return new ResponseEntity(emailVerificationResponse, HttpStatus.OK);
    }

    @PostMapping("/check-verification")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity checkVerification(String email, String verificationCode){

        Optional<EmailVerification> optionalEmailVerification = mailService.findByEmail(email);

        if (optionalEmailVerification.isPresent()){
            EmailVerification emailVerification = optionalEmailVerification.get();
            if (emailVerification.getVerificationCode().equals(verificationCode)){
                mailService.deleteVerificationCode(emailVerification);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                // 인증번호 틀림
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            // 이메일 정보 없음
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/find-password")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity findPassword(String email){
        String newPassword = String.valueOf(mailService.createNumber());

        try{
            userService.findPassword(email, newPassword);
            mailService.sendFindPasswordMail(email, newPassword);
            return new ResponseEntity(newPassword, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/change-password")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity changePassword(String email, String password){
        try {
            User user = userService.findEmail(email);
            userService.updatePassword(user, password);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            // 이메일에 해당하는 계정이 없음
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

    }
}