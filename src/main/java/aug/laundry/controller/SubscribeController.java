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

import java.io.IOException;
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
                    subDto.setMember_id(Long.parseLong(customerUid));
                    subDto.setSelect_month(selectMonth);
                    subDto.setMerchant_uid(merchantUid);
                    subDto.setCustomer_uid(customerUid);
                    subDto.setAmount(payPrice);
                    subDto.setImp_uid(impUid);
                    subscribeService_ksh.saveSubscribe(subDto);

                    // 재결제 예약
                    String scheduleUrl =  "https://api.iamport.kr/subscribe/payments/schedule";
                    CloseableHttpClient httpClient = HttpClients.createDefault(); // http 요청하기위한 객체 생성
                    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusMonths(1);
                    Instant instant = date.toInstant();
                    long timeStamp = instant.getEpochSecond();

                    ScheduleDto schedule = new ScheduleDto();
                    schedule.setMerchant_uid(merchantUid+"_r");
                    schedule.setSchedule_at(timeStamp);
                    schedule.setAmount(payPrice);
                    schedule.setName(selectMonth+"개월 정기결제예약");

                    Gson gson = new Gson();
                    String scheduleJson = gson.toJson(schedule);

                    String jsonBody = String.format("{\"customer_uid\": \"%s\", \"schedules\": [%s]}", customerUid, scheduleJson);
                    log.info("postData호출");
                    JsonObject jsonObjS = subscribeService_ksh.postData(scheduleUrl, jsonBody);
                    log.info("jsonObjS={}", jsonObjS);

                    // 리스폰스 데이터 배열이므로 JsonArray로 받음
                    JsonArray response = jsonObjS.get("response").getAsJsonArray();
                    log.info("response={}", response);

//                    subscribeService_ksh.updateNextMerchantId(merchantUid, merchantUid+"_r");

//                    if("scheduled".equals(jsonObjS.get("response").getAsJsonObject().get("schedule_status").getAsString())){
//                        // 예약 성공하면 테이블에 merchant_uid_r 업데이트
//                        subscribeService_ksh.updateNextMerchantId(merchantUid, merchantUid+"_r");
//                        result = "구독 신청 완료";
//                    } else {
//                        // 예약 실패일 경우 결제 취소하고 다시 결제하라고 메세지
//                        result = "구독 신청 실패. 관리자에게 문의해주세요.";
//                        log.info("url={}", "redirect:/subscription/confirm?result="+result);
//                        return "redirect:/subscription/order?result="+result;
//                    }
                }
            } else {
                // 결제금액 불일치. 위/변조 된 결제
                // 결제 취소
//                result = "위조된 결제시도. 관리자에게 문의해주세요.";
//                log.info("url={}", "redirect:/subscription/confirm?result="+result);
//                return "redirect:/subscription/order?result="+result;
            }
        } catch (IOException e) {
            log.info("e={}", e);
            //throw new RuntimeException(e);
        }

        log.info("url={}", "redirect:/subscription/confirm?result="+result);
        return "project_subscribe_success";
    }

    @PostMapping("/payments/prepare")
    public @ResponseBody Map<String, String> paymentPrepare(@RequestBody SubscriptionPayDto subData) {
        Map<String, String> map = new HashMap<>();
        // 결제 전 검증
        String result = "fail";

        switch (subData.getSelect_month()) {
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

    @PostMapping("/payments")
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
}
