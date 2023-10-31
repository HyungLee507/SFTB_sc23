package CDProject.vfmarket.controller;


import CDProject.vfmarket.service.MailService;
import CDProject.vfmarket.vo.MailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

//    @GetMapping("/")
//    public String mailPage(){
//        System.out.println("MailPage");
//        return "index.html";
//    }

//    @PostMapping("/mailSend")
//    public String mailSend(MailVo mailVo){
//        System.out.println("전송 전");
//        mailService.createMail(mailVo);
//        System.out.println("MailSend");
//        return "mail";
//    }
    @ResponseBody
    @PostMapping("/mailSend")
    public String mailSend(String receiver){

        int number = mailService.sendMail(receiver);

        String num = "" + number;

        return num;
    }
}