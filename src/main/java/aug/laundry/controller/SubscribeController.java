package aug.laundry.controller;

import aug.laundry.dto.ScheduleDto;
import aug.laundry.dto.SubscriptionPayDto;
import aug.laundry.enums.subscribe.Subscribe;
import aug.laundry.service.SubscribeService_ksh;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.response.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService_ksh subscribeService_ksh;
    @GetMapping("/subscription")
    public String subscribeInfo() {
        return "project_subscribe";
    }

    @GetMapping("/subscription/order")
    public String subscribeOrder(@RequestParam(name = "result", required = false) String result, Model model) {
        model.addAttribute("result", result);
        return "project_subscribe_choice";
    }

    @GetMapping("/subscription/confirm")
    public String confirmView(@RequestParam(name = "result", required = false) String result, Model model) {
        model.addAttribute("result", result);
        return "project_subscribe_success";
    }

    @GetMapping("/payments/mobile")
    public String subscribeOrderConfirm(String imp_uid, String merchant_uid, String imp_success) {
        // 모바일 결제
        String result = "";
        try {
            // imp_uid 또는 merchant_uid로 아임포트 서버에서 결제 정보 조회
            String requestUrl =  "https://api.iamport.kr/payments/" + imp_uid;
            JsonObject jsonObj = subscribeService_ksh.getData(requestUrl);
            JsonObject data = jsonObj.get("response").getAsJsonObject();
            String customerUid = data.get("customer_uid").getAsString();
            String merchantUid = data.get("merchant_uid").getAsString();
            String impUid = data.get("imp_uid").getAsString();
            int amountToBePaid = 0;
            int selectMonth = Integer.parseInt(data.get("name").getAsString().substring(0,1));

            if(selectMonth == 1) {
                amountToBePaid = Subscribe.ONE.getPrice();
            } else if(selectMonth == 3) {
                amountToBePaid = Subscribe.THREE.getPrice();
            } else if(selectMonth == 6) {
                amountToBePaid = Subscribe.SIX.getPrice();
            } else if(selectMonth == 12) {
                amountToBePaid = Subscribe.TWELVE.getPrice();
            } else {
                amountToBePaid = -1;
            }

            int payPrice = Integer.parseInt(data.get("amount").toString());
            // 결제금액 일치. 결제 된 금액 === 결제 되어야 하는 금액
            if(payPrice == amountToBePaid ) {
                if(data.get("status").getAsString().equals("paid")) {
                    // DB에 결제 정보 저장 <- DB에 이미 있으면 저장X 예외메세지 처리하기
                    SubscriptionPayDto subDto = new SubscriptionPayDto();
                    subDto.setMemberId(Long.parseLong(customerUid));
                    subDto.setSelectMonth(selectMonth);
                    subDto.setMerchantUid(merchantUid);
                    subDto.setCustomerUid(customerUid);
                    subDto.setAmount(payPrice);
                    subDto.setImpUid(impUid);
                    subscribeService_ksh.saveSubscribe(subDto);

                    // 재결제 예약 메서드로 빼기
                    String scheduleUrl =  "https://api.iamport.kr/subscribe/payments/schedule";
                    CloseableHttpClient httpClient = HttpClients.createDefault(); // http 요청하기위한 객체 생성
                    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusMonths(1);
                    Instant instant = date.toInstant();
                    long timeStamp = instant.getEpochSecond();

                    ScheduleDto schedule = new ScheduleDto();
                    schedule.setMerchant_uid(merchantUid+"_re");
                    schedule.setSchedule_at(timeStamp);
                    schedule.setAmount(payPrice);
                    schedule.setName(selectMonth+"개월 정기결제예약");

                    Gson gson = new Gson();
                    String scheduleJson = gson.toJson(schedule);

                    String jsonBody = String.format("{\"customer_uid\": \"%s\", \"schedules\": [%s]}", customerUid, scheduleJson);
                    JsonObject jsonObjS = subscribeService_ksh.postData(scheduleUrl, jsonBody);

                    // 리스폰스 데이터 배열이므로 JsonArray로 받음
                    JsonArray responseArr = jsonObjS.get("response").getAsJsonArray();
                    JsonObject response = responseArr.get(0).getAsJsonObject();

                    if("scheduled".equals(response.get("schedule_status").getAsString())){
                        // 예약 성공하면 테이블에 merchant_uid_r 업데이트
                        subscribeService_ksh.updateNextMerchantId(merchantUid, merchantUid+"_re");
                        result = "success";
                    } else {
                        // 예약 실패일 경우 구독 해지 처리 subscription_pay 테이블에 status N으로하고
                        result = "fail";
                        return "redirect:/subscription/order?result="+result;
                    }
                } else {
                    // 잔액 부족등과 같은 결제 오류가 났을경우
                    // 다음날로 결제 예약, 결제 시도 3번 후 더이상 예약 X
                }
            } else {
                // 결제금액 불일치. 위/변조 된 결제
                // 결제 취소 데이터 베이스 삭제
                String cancelUrl =  "https://api.iamport.kr/payments/cancel";
                String jsonBody = String.format("{\"reason\": \"%s\", \"imp_uid\": \"%s\", \"amount\": %d, \"checksum\": %d}", "잘못된 결제", impUid, payPrice, payPrice);

                JsonObject cancleData  = subscribeService_ksh.postData(cancelUrl, jsonBody);
//postData={"code":0,"message":null,"response":{"amount":5900,"apply_num":"","bank_code":null,"bank_name":null,"buyer_addr":null,"buyer_email":null,"buyer_name":null,"buyer_postcode":null,"buyer_tel":null,"cancel_amount":5900,"cancel_history":[{"pg_tid":"T4fab0787514397709d3","amount":5900,"cancelled_at":1694156059,"reason":"잘못된 결제","receipt_url":"https://mockup-pg-web.kakao.com/v1/confirmation/m/T4fab0787514397709d3/7dbad607b6f039150f99f398ac0de522875546bcbdb6aa6bbcf29264f245aa53"}],"cancel_reason":"잘못된 결제","cancel_receipt_urls":["https://mockup-pg-web.kakao.com/v1/confirmation/m/T4fab0787514397709d3/7dbad607b6f039150f99f398ac0de522875546bcbdb6aa6bbcf29264f245aa53"],"cancelled_at":1694156059,"card_code":null,"card_name":null,"card_number":null,"card_quota":0,"card_type":null,"cash_receipt_issued":false,"channel":"mobile","currency":"KRW","custom_data":null,"customer_uid":"4","customer_uid_usage":"issue","emb_pg_provider":null,"escrow":false,"fail_reason":null,"failed_at":0,"imp_uid":"imp_809776678494","merchant_uid":"4_subscribe_2023_09_08_142616185","name":"1개월 구독권","paid_at":1694150787,"pay_method":"point","pg_id":"TCSUBSCRIP","pg_provider":"kakaopay","pg_tid":"T4fab0787514397709d3","receipt_url":"https://mockup-pg-web.kakao.com/v1/confirmation/m/T4fab0787514397709d3/7dbad607b6f039150f99f398ac0de522875546bcbdb6aa6bbcf29264f245aa53","started_at":1694150776,"status":"cancelled","user_agent":"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1","vbank_code":null,"vbank_date":0,"vbank_holder":null,"vbank_issued_at":0,"vbank_name":null,"vbank_num":null}}
//                if() {
//                    // 환불 성공
//                } else {
//                    // 환불 실패
//                }

                result = "Fraudulent payment attempt";
                return "redirect:/subscription/order?result="+result;
            }
        } catch (Exception e) {
            log.info("e={}", e);
            result = "Pay error";
            return "redirect:/subscription/order?result="+result;
        }
        return "redirect:/subscription/confirm?result="+result;
    }

    @PostMapping("/payments/prepare")
    public @ResponseBody Map<String, String> paymentPrepare(@RequestBody SubscriptionPayDto subData) {
        Map<String, String> map = new HashMap<>();
        // 결제 전 검증
        String result = "fail";

        switch (subData.getSelectMonth()) {
            case 1:
                if (subData.getAmount() == Subscribe.ONE.getPrice()) result = "success";
                break;
            case 3:
                if (subData.getAmount() == Subscribe.THREE.getPrice()) result = "success";
                break;
            case 6:
                if(subData.getAmount() == Subscribe.SIX.getPrice()) result = "success";
                break;
            case 12:
                if (subData.getAmount() == Subscribe.TWELVE.getPrice())  result = "success";
                break;
        }
        map.put("result", result);

        log.info("prepare");

        return map;
    }

    @PostMapping("/payments/pc")
    public @ResponseBody Map<String, String> paymentsComplete(@RequestBody SubscriptionPayDto subData) {
        // pc결제
        try {
            // access_token 받아오기
            String token = subscribeService_ksh.getAccessToken();

            // 결제 후 검증
        /*

    // imp_uid로 포트원 서버에서 결제 정보 조회
    // 코드 생략

    const paymentData = getPaymentData.data.response; // 조회한 결제 정보
    // ...
    // DB에서 결제되어야 하는 금액 조회
    const order = await Orders.findById(paymentData.merchant_uid);
    const amountToBePaid = order.amount; // 결제 되어야 하는 금액
    // ...
    // 결제 검증하기
    const { amount, status } = paymentData;
    // 결제금액 일치. 결제 된 금액 === 결제 되어야 하는 금액
    if (amount === amountToBePaid) {
      await Orders.findByIdAndUpdate(merchant_uid, { $set: paymentData }); // DB에 결제 정보 저장
        case "paid": // 결제 완료
                res.send({ status: "success", message: "일반 결제 성공" });
          break;
}
    } else {
            // 결제금액 불일치. 위/변조 된 결제
            throw { status: "forgery", message: "위조된 결제시도" };
            }
            } catch (e) {
            res.status(400).send(e);
            }
        * */

            // 결제성공해서 DB저장하고 재결제예약

        } catch (Exception e) {
            log.info("Unable to get access token");
        }
        Map<String, String> data = new HashMap<>();
        data.put("result", "success");

        return data;
    }

    @GetMapping("/subscribe/unschedule/{memberId}")
    public @ResponseBody Map<String, String> cancleTest(@PathVariable("memberId") Long memberId) {
        // 취소버튼 누르면 fetch
        // 구독 취소 누르면 구독패이 테이블에 스테이터스 N으로 변경
        Map<String, String> map = new HashMap<>();

        try {
            SubscriptionPayDto info = subscribeService_ksh.getScheduleInfo(memberId);
            String cancelUrl =  "https://api.iamport.kr/subscribe/payments/unschedule";
            String jsonBody = String.format("{\"customer_uid\": \"%s\", \"merchant_uid\": \"%s\"}", info.getCustomerUid(), info.getMerchantUidRe());

            JsonObject unscheduleData = subscribeService_ksh.postData(cancelUrl, jsonBody);

            log.info("info={}", info);
            log.info("unscheduleData={}",unscheduleData);

            //unscheduleData={"code":0,"message":null,"response":[{"customer_uid":"4","merchant_uid":"4_subscribe_2023_09_08_142616185_r","imp_uid":null,"customer_id":null,"schedule_at":1696742786,"executed_at":0,"revoked_at":1694160397,"amount":5900,"name":"1개월 정기결제예약","buyer_name":null,"buyer_tel":null,"buyer_email":null,"buyer_addr":null,"buyer_postcode":null,"schedule_status":"revoked","payment_status":null,"fail_reason":null,"custom_data":null,"currency":"KRW"}]}
            // 구독 취소 성공하면 데이터베이스 업데이트 subscription_pay 테이블에 status N으로하고

            // 실패하면 관리자 연락하라고 메세지

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        map.put("test", "unschedule");

        return map;
    }
}



