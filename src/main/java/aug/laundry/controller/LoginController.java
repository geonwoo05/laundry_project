package aug.laundry.controller;



import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.MemberDto;
import aug.laundry.service.LoginService_kgw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService_kgw service;

    @GetMapping("/login/naver_callback")
    public String naverLogin_callback(HttpServletRequest request, Model model) {
        service.naverLogin(request, model);

        return "project_mypage";
    }
    @RequestMapping(value = "/kakaoLogin", method = RequestMethod.GET)
    public String kakaoLogin_Redirect(String code, Model model){
        service.kakaoLogin(code);

        return "/test";


    }

    @PostMapping("/login")
    public String login(MemberDto memberDto, HttpSession session) {
        MemberDto dto = service.login(memberDto.getMemberAccount());
        session.setAttribute(SessionConstant.LOGIN_MEMBER, dto.getMemberId());
        return "redirect:/members/" + dto.getMemberId() + "/mypage";
    }





}


















