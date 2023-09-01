package aug.laundry.controller;


import aug.laundry.dto.KakaoOauthToken;
import aug.laundry.dto.KakaoProfile;
import aug.laundry.service.LoginService_kgw;
import aug.laundry.service.MemberService_kgw;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService_kgw service;

    @GetMapping("/login/naver_callback")
    public String naverLogin_callback(HttpServletRequest request, Model model) {
        service.naverLogin(request, model);

        return "/project_mypage";
    }
    @RequestMapping(value = "/kakaoLogin", method = RequestMethod.GET)
    public String kakaoLogin_Redirect(String code, Model model){
        service.kakaoLogin(code);

        return "/test";


    }




}


















