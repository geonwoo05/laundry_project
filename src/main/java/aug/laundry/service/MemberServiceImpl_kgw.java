package aug.laundry.service;

import aug.laundry.dao.MemberMapper;
import aug.laundry.dto.MemberDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl_kgw implements MemberService_kgw{

    private final MemberMapper memberMapper;

    private final ApiExamMemberProfile apiExam;

    public MemberDto selectOne(Long memberId){
        MemberDto memberDto = memberMapper.selectOne(memberId);
        return memberDto;
    }

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
            System.out.println(response.get("name"));
            System.out.println(response.get("email"));
            System.out.println(response.get("gender"));
            System.out.println("=============================");

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






}
