package aug.laundry.controller;


import aug.laundry.service.MemberService_kgw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService_kgw service;

    @GetMapping("/login/naver_callback")
    public String naverLogin_callback(HttpServletRequest request, Model model) {
        service.naverLogin(request, model);

        return "/project_mypage";
    }













}
