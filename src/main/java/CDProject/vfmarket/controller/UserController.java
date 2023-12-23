package CDProject.vfmarket.controller;

import static CDProject.vfmarket.global.AuthenticationUserId.getAuthenticatedUser;

import CDProject.vfmarket.domain.dto.SignUpDTO.FirstRegistUserInfoDto;
import CDProject.vfmarket.domain.dto.SignUpDTO.UserSignUpDto;
import CDProject.vfmarket.domain.dto.UserDTO.UserInfoDTO;
import CDProject.vfmarket.domain.entity.EmailVerification;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.service.MailService;
import CDProject.vfmarket.service.UserService;
import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/sign-up")
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
    public ResponseEntity mailVerify(String email) {
        EmailVerification emailVerification = mailService.findByEmail(email);
        if (emailVerification != null) {
            return new ResponseEntity(HttpStatus.IM_USED);
        }

        int sendCode = mailService.createNumber();

        mailService.sendVerificationMail(email, sendCode);  // 인증 코드 메일 전송

        mailService.saveVerificationCode(email, sendCode);  // 인증 코드 DB 저장

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/check-verification")
    public ResponseEntity checkVerification(String email, String verificationCode) {
        EmailVerification emailVerification = mailService.findByEmail(email);

        if (emailVerification == null) {
            // 이메일 정보 없음
            return new ResponseEntity("이메일을 다시 확인해주십시오.", HttpStatus.NO_CONTENT);
        } else if (LocalDateTime.now().isAfter(emailVerification.getCreateDate().plusMinutes(3L))) {
            // 인증 기간 만료
            mailService.deleteVerificationCode(emailVerification);
            return new ResponseEntity("인증 기간이 만료되었습니다.", HttpStatus.REQUEST_TIMEOUT);
        } else if (emailVerification.getVerificationCode().equals(verificationCode)) {
            // 인증 성공
            mailService.deleteVerificationCode(emailVerification);
            return new ResponseEntity("인증에 성공했습니다.",HttpStatus.OK);
        } else {
            // 인증번호 틀림
            return new ResponseEntity("인증번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/find-password")
    public ResponseEntity findPassword(String email, String name) {
        int verificationCode = mailService.createNumber();

        try {
            User user = userService.findEmail(email);           // 임의의 비밀번호 설정
            if (user.getName().equals(name)) {
                mailService.sendFindPasswordMail(email, String.valueOf(verificationCode));   // 메일 발송
                mailService.saveVerificationCode(email, verificationCode);  // 인증 번호 DB 저장
                return new ResponseEntity<>(
                        "인증번호가 이메일로 발송되었습니다\n3분 내로 입력 바랍니다", HttpStatus.OK);  // 메일 발송 성공
            } else {
                return new ResponseEntity<>("일치하는 정보가 없습니다.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("존재하지 않는 이메일입니다.", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/change-password")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity changePassword(String email, String password) {
        try {
            User user = userService.findEmail(email);
            userService.updatePassword(user, password);
            return new ResponseEntity("비밀번호 변경에 성공했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            // 이메일에 해당하는 계정이 없음
            return new ResponseEntity("존재하지 않는 이메일입니다.", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/user/update")
    public String firstLoginUpdateUserInfo(@RequestBody FirstRegistUserInfoDto firstRegistUserInfoDto) {
        try {
            userService.updateUserInfo(firstRegistUserInfoDto);
            return "회원 정보 업데이트 성공";
        } catch (NoSuchObjectException e) {
            return "해당 유저를 찾을 수 없습니다. => 근데 이럴리 없지";
        }
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public UserInfoDTO getUserInfo() {
        Long userId = getAuthenticatedUser();
        return userService.userInformation(userId);
    }
}
