package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.entity.Member;
import CDProject.vfmarket.service.MailService;
import CDProject.vfmarket.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class MemberController {
    private final MailService mailService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/join")
    public String join(){
        return "/account/join.html";
    }

    @PostMapping("/join")
    public String join(Member member){
        memberService.saveMember(member);
        return "redirect:/";
    }

    @GetMapping("/mailVerify")
    @ResponseBody
    public String mailVerify(String email){
        int sentCode = mailService.sendMail(email);
        return "    <form id=\"verifyform\" method=\"post\" action=\"/account/mailVerify\">\n" +
                "        <div>입력하신 이메일로 인증번호가 전송되었습니다.</div>\n" +
                "        <div>인증번호를 입력하세요</div>\n" +
                "        <input name=\"sentCode\" type=\"hidden\" value=\""+sentCode+"\">\n" +
                "        <input name=\"inputCode\" type=\"text\">\n" +
                "        <input type=\"submit\" value=\"인증\">\n" +
                "        <input type=\"button\" value=\"취소\" onclick=\"window.close()\">\n" +
                "    </form>";
    }

    @PostMapping("/mailVerify")
    public String mailVerify(String sentCode, String inputCode){
        if (sentCode.equals(inputCode)){
            return "redirect:/account/mailVerifySuccess";
        }
        else{
            return "redirect:/account/mailVerifyFail";
        }
    }

    @GetMapping("/mailVerifySuccess")
    public String mailVerifySuccess(){
        return "/account/verifysuccess.html";
    }
    @GetMapping("/mailVerifyFail")
    public String mailVerifyFail(){
        return "/account/verifyfail.html";
    }
}
