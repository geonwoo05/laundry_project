package aug.laundry.service;

import aug.laundry.dao.subscribe.SubscribeDao;
import aug.laundry.dto.SubscriptionPayDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeServiceImpl_ksh implements SubscribeService_ksh{

    private final SubscribeDao subscribeDao;

    @Value("${import.rest-api}")
    private String restApi;

    @Value("${import.rest-api-secret}")
    private String restApiSecret;

    @Transactional
    @Override
    public int saveSubscribe(SubscriptionPayDto subData) {
        // 결제정보 DB저장
        int res = subscribeDao.insertJoinSubscribe(subData);
        res += subscribeDao.updateMemberSubscribe(subData.getSelect_month(), subData.getMember_id());
        return res;
    }

    @Override
    public String getAccessToken() {

        String accessToken = "";

        try {
            String requestUrl = "https://api.iamport.kr/users/getToken";
            String jsonBody = String.format("{\"imp_key\": \"%s\", \"imp_secret\": \"%s\"}", restApi, restApiSecret);
            StringEntity json = new StringEntity(jsonBody, "UTF-8");
            json.setContentType("application/json");

            CloseableHttpClient httpClient = HttpClients.createDefault(); // http 요청하기위한 객체 생성
            HttpPost postRequest = new HttpPost(requestUrl);
            postRequest.setEntity(json);

            // 요청 후 데이터 받기
            HttpResponse response = httpClient.execute(postRequest);
            HttpEntity getEntity = response.getEntity();

            // 응답데이터를 String으로 변환
            String responseEntity = EntityUtils.toString(getEntity);

            // responseEntity데이터를 JsonObject객체로 파싱
            JsonObject jsonObject = new Gson().fromJson(responseEntity, JsonObject.class);

            // accessToken추출
            accessToken = jsonObject.get("response").getAsJsonObject().get("access_token").getAsString();

            httpClient.close();

            return accessToken;
        } catch (IOException e) {
            throw new RuntimeException("Unable to get access token",e);
        }
    }

    @Override
    public JsonObject postData(String requestUrl, String jsonBody) throws IOException {
        StringEntity json = new StringEntity(jsonBody, "UTF-8");
        json.setContentType("application/json");

        CloseableHttpClient httpClient = HttpClients.createDefault(); // http 요청하기위한 객체 생성
        HttpPost postRequest = new HttpPost(requestUrl);
        postRequest.setHeader("Authorization", getAccessToken());
        postRequest.setEntity(json);

        // 요청 후 데이터 받기
        HttpResponse response = httpClient.execute(postRequest);
        HttpEntity getEntity = response.getEntity();

        // 응답데이터를 String으로 변환
        String responseEntity = EntityUtils.toString(getEntity);

        // responseEntity데이터를 JsonObject객체로 파싱
        JsonObject data = new Gson().fromJson(responseEntity, JsonObject.class);
        log.info("postData={}", data);

        httpClient.close();

        return data;
    }

    @Override
    public JsonObject getData(String requestUrl) throws IOException {
        String token = getAccessToken();
        CloseableHttpClient httpClient = HttpClients.createDefault(); // http 요청하기위한 객체 생성
        HttpGet getRequest = new HttpGet(requestUrl);
        getRequest.setHeader("Authorization", token);

        // 요청 후 데이터 받기
        HttpResponse response = httpClient.execute(getRequest);
        HttpEntity getEntity = response.getEntity();

        //responseEntity 널처리 하기
        String responseEntity = EntityUtils.toString(getEntity);
        JsonObject data = new Gson().fromJson(responseEntity, JsonObject.class);

        log.info("getData={}", data);

        httpClient.close();

        return data;
    }

    @Override
    public int updateNextMerchantId(String merchant_uid, String merchant_uid_r) {
        return subscribeDao.updateNextMerchantId(merchant_uid, merchant_uid_r);
    }
}
