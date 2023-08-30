package aug.laundry.controller;


import aug.laundry.dto.KakaoOauthToken;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService_kgw service;

    @GetMapping("/login/naver_callback")
    public String naverLogin_callback(HttpServletRequest request, Model model) {
        service.naverLogin(request, model);

        return "/project_mypage";
    }
    @RequestMapping(value = "/kakaoLogin", method = RequestMethod.GET)
    public String kakaoLogin_Redirect(String code, Model model){
        String accessToken = getAccessToken(code, model);



        model.addAttribute("body", accessToken );


        ObjectMapper obMapper = new ObjectMapper();

        //json 문자열에 있는 키값이 DTO등 객체에는 없어서 문제가 생기므로 이 코드로 해결
        obMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        KakaoOauthToken kOauthToken = null;

        try {
            kOauthToken = obMapper.readValue(accessToken, KakaoOauthToken.class);
            getKakaoUserInfo(accessToken, kOauthToken);
        }catch(JsonMappingException e){
            e.printStackTrace();

        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        System.out.println("카카오 access토큰 : " + kOauthToken.getAccess_token());

       return "/test";


    }


    public String getAccessToken(String code, Model model){
        try {
            // okhttp객체 생성
            OkHttpClient client = new OkHttpClient();

            MediaType jsonMediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


            // requestBody 생성
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("grant_type", "authorization_code");
            jsonBody.put("client_id", "6ceec1b5aece169e4582fd82601abd44");
            jsonBody.put("redirect_uri", "http://localhost:8080/kakaoLogin");
            jsonBody.put("code",code);

            String queryString = encodeParameters(jsonBody);

            RequestBody requestBody = RequestBody.create(queryString, jsonMediaType);
            Request request = new Request.Builder().url("https://kauth.kakao.com/oauth/token").post(requestBody).build();

            
            // 요청 전송
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            return body.string();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    public void getKakaoUserInfo(String accessToken, KakaoOauthToken kOauthToken){
        OkHttpClient client = new OkHttpClient();

        MediaType jsonMediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


        Request request = new Request.Builder().url("https://kapi.kakao.com/v2/user/me").addHeader("Authorization", "Bearer " + kOauthToken.getAccess_token()).post(RequestBody.create(null, new byte[0])).build();

        // 요청 전송
        Response response = null;
        try {
            response = client.newCall(request).execute();
            ResponseBody body = response.body();

            System.out.println("유저 정보 : " + body.string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public String encodeParameters(JSONObject params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/kakaoLogin").newBuilder();
        for (String key : params.keySet()) {
            urlBuilder.addQueryParameter(key, params.getString(key));
        }
        return urlBuilder.build().encodedQuery();
    }



}


















