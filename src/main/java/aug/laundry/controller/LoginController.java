package aug.laundry.controller;



import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.*;
import aug.laundry.service.BCryptService_kgw;
import aug.laundry.service.LoginService_kgw;
import aug.laundry.service.MemberService_kgw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final LoginService_kgw service;
    private final MemberService_kgw memberServce;

    @GetMapping("/naver_callback")
    public String naverLogin_callback(HttpServletRequest request, Model model, HttpSession session) {
        service.naverLogin(request, model, session);
        System.out.println("naverLogin =======");
        System.out.println("naver sessionId : " + session.getAttribute("memberId"));


        return "redirect:/";
    }
    @GetMapping("/kakaoLogin")
    public String kakaoLogin_Redirect(String code, Model model, HttpSession session){
        service.kakaoProcess(code, session);

        return "redirect:/";
    }

    @PostMapping("/loginAction")
    public  String login(MemberDto memberDto, HttpSession session, Model model) {
        MemberDto userdto = service.login(memberDto, session);


        AdminDto adminDto = service.adminLogin(memberDto.getMemberAccount());
        RiderDto riderDto = service.riderLogin(memberDto.getMemberAccount());
        QuickRiderDto quickRiderDto = service.quickRiderLogin(memberDto.getMemberAccount());

        if (userdto != null) {
            session.setAttribute(SessionConstant.LOGIN_MEMBER, userdto.getMemberId());
            return "redirect:/";
        }else if(adminDto != null){
            session.setAttribute(SessionConstant.LOGIN_MEMBER, adminDto.getAdminId());
            return "redirect:/admin";
        }else if(riderDto != null){
            session.setAttribute(SessionConstant.LOGIN_MEMBER, riderDto.getRiderId());
            return "redirect:/ride/routine";
        }else if (quickRiderDto != null) {
            session.setAttribute(SessionConstant.LOGIN_MEMBER, quickRiderDto.getQuickRiderId());
            return "redirect:/ride/wait";
        }else{
            model.addAttribute("memberAccount", memberDto.getMemberAccount());
            model.addAttribute("errorMsg", "아이디 또는 비밀번호를 잘못 입력했습니다.");
            return "project_login";
        }

    }

    @PostMapping("/find-account")
    public String confirmId(ConfirmIdDto confirmIdDto, Model model){
        System.out.println("memberAccount : " + confirmIdDto.getMemberAccount());
        List<MemberDto> list = memberServce.confirmId(confirmIdDto.getMemberName(), confirmIdDto.getMemberPhone(), confirmIdDto.getMemberAccount());
        model.addAttribute("list", list);
        return "project_search_id_result";
    }

    @ResponseBody
    @PostMapping("/find-pw")
    public Map<String, Object> confirmId(@RequestBody @Valid ConfirmIdDto confirmIdDto){
        List<MemberDto> list = memberServce.confirmId(confirmIdDto.getMemberName(), confirmIdDto.getMemberPhone(), confirmIdDto.getMemberAccount());
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return map;
    }

    @PostMapping("/find-pw/update")
    public String updatePassword(MemberDto memberDto){
        System.out.println("memberDto : " + memberDto);
        int res = memberServce.updatePassword(memberDto);
        System.out.println("비밀번호 변경 : " + res);
        return "redirect:/login";
    }


    @GetMapping("/find-pw/update")
    public String updatePasswordForm(@RequestParam("memberAccount") String memberAccount, Model model){
        System.out.println("memberAccount : " + memberAccount);
        model.addAttribute("memberAccount", memberAccount);
        return "project_change_password";
    }

    @GetMapping("/find-account")
    public String goFindAccount(){
        return "project_search_id";
    }

    @GetMapping("/find-pw")
    public String goFindPassword(){return "project_search_password";}

    @GetMapping
    public String goLogin(){
        return "project_login";
    }
}


















