package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.UserSignUpDto;
import CDProject.vfmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    @CrossOrigin(origins = "http://localhost:3000/")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        userService.signUp(userSignUpDto);
        return "회원 가입 성공";
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
<<<<<<< HEAD

    @PostMapping("/mail-verify")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity mailVerify(String email){
        int sendCode = mailService.createNumber();

        EmailVerificationResponseDto emailVerificationResponse =
                EmailVerificationResponseDto.builder()
                        .email(senderEmail)
                        .sendCode(sendCode)
                        .message("입력하신 이메일로 인증번호가 전송되었습니다.\n인증번호를 입력하세요.")
                        .build();

        mailService.sendVerificationMail(email, sendCode);

        return new ResponseEntity(emailVerificationResponse, HttpStatus.OK);

//        return "    <form id=\"verifyform\" method=\"post\" action=\"/account/mailVerify\">\n" +
//                "        <div>입력하신 이메일로 인증번호가 전송되었습니다.</div>\n" +
//                "        <div>인증번호를 입력하세요</div>\n" +
//                "        <input name=\"sendCode\" type=\"hidden\" value=\""+sendCode+"\">\n" +
//                "        <input name=\"inputCode\" type=\"text\">\n" +
//                "        <input type=\"submit\" value=\"인증\">\n" +
//                "        <input type=\"button\" value=\"취소\" onclick=\"window.close()\">\n" +
//                "    </form>";
    }
//    @GetMapping("/mail-verify")
//    public String mailVerify(String sentCode, String inputCode){
//        if (sentCode.equals(inputCode)){
//            return "이메일 인증에 성공했습니다.\n창을 닫고 회원 가입을 계속 진행하시면 됩니다.";
//        }else{
//            return "이메일 인증에 실패했습니다.\n창을 닫고 인증을 다시 진행하시기 바랍니다.";
//        }
//    }

    @PostMapping("/find-password")
    public String findPassword(String email){
        String newPassword = String.valueOf(mailService.createNumber());

        try{
            userService.findPassword(email, newPassword);
            mailService.sendFindPasswordMail(email, newPassword);
            return "입력하신 이메일로 새로운 비밀번호가 전송되었습니다.\n" +
                    "빠른 시일 내에 로그인을 부탁드립니다.\n";
        } catch (Exception e){
            return e.getMessage();
        }
    }
=======
>>>>>>> origin/connect-front
}
