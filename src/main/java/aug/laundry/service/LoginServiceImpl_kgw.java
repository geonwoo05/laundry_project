package aug.laundry.service;

import aug.laundry.dao.LoginMapper;
import aug.laundry.dto.KakaoOauthToken;
import aug.laundry.dto.KakaoProfile;
import aug.laundry.dto.MemberDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl_kgw implements LoginService_kgw{


    private final ApiExamMemberProfile apiExam;
    private final LoginMapper loginMapper;
    private final MemberDto memberDto;

    @Override
    public void naverLogin(HttpServletRequest request, Model model) {
        try {
            // callback처리 -> access_token
            Map<String, String> callbackRes = callback(request);

            String access_token = callbackRes.get("access_token");
            // access_token -> 사용자 정보 조회
            Map<String, Object> responseBody
                    = apiExam.getMemberProfile(access_token);

            Map<String, String> response
                    = (Map<String, String>) responseBody.get("response");
            System.out.println("================ naverLogin ");
            System.out.println(response.get("id"));
            System.out.println(response.get("name"));
            System.out.println(response.get("email"));
            System.out.println(response.get("mobile"));
            System.out.println("=============================");

            int checkRes = loginMapper.checkSocialId(response.get("id"));

            if(checkRes <= 0){
                System.out.println("naver 유저정보 db에 저장");
                // memberDto에 담기
                memberDto.setMemberAccount(response.get("email"));
                memberDto.setMemberName(response.get("name"));
                memberDto.setMemberSocial("naver");
                String formattedPhoneNumber = formatPhoneNumber(response.get("mobile"));
                memberDto.setMemberPhone(formattedPhoneNumber);

                int registerUserInfoRes = registerSocialUser(memberDto);
                int registerSocialNumRes = registerSocialNumber(response.get("id"));

                System.out.println(registerUserInfoRes);
                System.out.println(registerSocialNumRes);



            }

            // 세션에 저장
            model.addAttribute("id", response.get("id"));
            model.addAttribute("name", response.get("name"));
            model.addAttribute("genter", response.get("gender"));

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Map<String, String > callback(HttpServletRequest request) throws Exception{
        String clientId = "1zyMGeTLflNJkh2bmICN";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "6DzM78FFO9";//애플리케이션 클라이언트 시크릿값";
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        try {
            String redirectURI = URLEncoder.encode("http://localhost:8080/login/naver_callback", "UTF-8");
            String apiURL;
            apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
            apiURL += "client_id=" + clientId;
            apiURL += "&client_secret=" + clientSecret;
            apiURL += "&redirect_uri=" + redirectURI;
            apiURL += "&code=" + code;
            apiURL += "&state=" + state;
            String access_token = "";
            String refresh_token = "";
            System.out.println("apiURL="+apiURL);
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;
            System.out.print("responseCode="+responseCode);
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            if(responseCode==200) {
                System.out.println("token요청" + res.toString());
                // json문자열을 Map으로 변환
                Map<String, String> map = new HashMap<>();
                ObjectMapper objectMapper = new ObjectMapper();
                map = objectMapper.readValue(res.toString(), Map.class);
                return map;
            } else {
                throw new Exception("callback 반환코드 : " + responseCode);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("callback 처리중 예외사항이 발생 하였습니다. ");
        }
    }

    public void kakaoLogin(String code){
        String accessToken = getAccessToken(code);
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

    }

    public String getAccessToken(String code){
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

    public String encodeParameters(JSONObject params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/kakaoLogin").newBuilder();
        for (String key : params.keySet()) {
            urlBuilder.addQueryParameter(key, params.getString(key));
        }
        return urlBuilder.build().encodedQuery();
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

            //dto에 담기
            ObjectMapper obMapper = new ObjectMapper();
            //json 문자열에 있는 키값이 DTO등 객체에는 없어서 문제가 생기므로 이 코드로 해결
            obMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            KakaoProfile kakaoProfile = null;
            try {
                kakaoProfile = obMapper.readValue(body.string(), KakaoProfile.class);

            }catch(JsonMappingException e){
                e.printStackTrace();

            }catch (JsonProcessingException e){
                e.printStackTrace();
            }

            System.out.println("카카오 아이디(고유번호): " + kakaoProfile.getId());
            System.out.println("카카오 이메일: " + kakaoProfile.getKakao_account().getEmail());
            System.out.println("카카오 이름 : " + kakaoProfile.getKakao_account().getName());


            // Long타입을 String타입으로 바꾸기
            Long SocalId = kakaoProfile.getId();
            String memberSocalId = String.valueOf(SocalId);



            int checkRes = loginMapper.checkSocialId(memberSocalId);

            if(checkRes <= 0){
                System.out.println("kakao 유저정보 db에 저장");
                // memberDto에 담기
                memberDto.setMemberAccount(kakaoProfile.getKakao_account().getEmail());
                memberDto.setMemberName(kakaoProfile.getKakao_account().getName());
                memberDto.setMemberSocial("kakao");
                String formattedPhoneNumber = formatPhoneNumber(kakaoProfile.getKakao_account().getPhone_number());
                memberDto.setMemberPhone(formattedPhoneNumber);

                int registerUserInfoRes = registerSocialUser(memberDto);
                int registerSocialNumRes = registerSocialNumber(memberSocalId);

                System.out.println(registerUserInfoRes);
                System.out.println(registerSocialNumRes);



            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public int registerSocialUser(MemberDto memberDto){
        int res = loginMapper.registerSocialUser(memberDto);


        return res;
    }

    public int registerSocialNumber(String id){
        int res = loginMapper.registerSocialNumber(id);

        return res;
    }

    public static String formatPhoneNumber(String phoneNumber) {
        // 정규식을 사용하여 "-"와 공백을 제거
        String cleanNumber = phoneNumber.replaceAll("[\\-\\s]", "");

        // "+82"를 "0"으로 변경
        String formattedNumber = cleanNumber.replace("+82", "0");

        // 최종적으로 "010"으로 시작하도록 변환
        if (!formattedNumber.startsWith("010")) {
            formattedNumber = "010" + formattedNumber.substring(3);
        }

        return formattedNumber;
    }

}
