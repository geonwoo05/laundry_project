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
        int payPrice = 0;
        try {
            // imp_uid 또는 merchant_uid로 아임포트 서버에서 결제 정보 조회
            String requestUrl = "https://api.iamport.kr/payments/" + imp_uid;
            JsonObject jsonObj = subscribeService_ksh.getData(requestUrl);
            JsonObject data = jsonObj.get("response").getAsJsonObject();
            String customerUid = data.get("customer_uid").getAsString();
            String merchantUid = data.get("merchant_uid").getAsString();
            String impUid = data.get("imp_uid").getAsString();
            int amountToBePaid = 0;
            int selectMonth = Integer.parseInt(data.get("name").getAsString().substring(0, 1));

            if (selectMonth == 1) {
                amountToBePaid = Subscribe.ONE.getPrice();
            } else if (selectMonth == 3) {
                amountToBePaid = Subscribe.THREE.getPrice();
            } else if (selectMonth == 6) {
                amountToBePaid = Subscribe.SIX.getPrice();
            } else if (selectMonth == 12) {
                amountToBePaid = Subscribe.TWELVE.getPrice();
            } else {
                amountToBePaid = -1;
            }

            payPrice = Integer.parseInt(data.get("amount").toString());
            // 결제금액 일치. 결제 된 금액 === 결제 되어야 하는 금액
            if (payPrice == amountToBePaid) {
                SubscriptionPayDto subDto = new SubscriptionPayDto();
                subDto.setMemberId(Long.parseLong(customerUid));
                subDto.setSelectMonth(selectMonth);
                subDto.setMerchantUid(merchantUid);
                subDto.setCustomerUid(customerUid);
                subDto.setAmount(payPrice);
                subDto.setImpUid(impUid);
                if (data.get("status").getAsString().equals("paid")) {
                    // DB에 결제 정보 저장 <- DB에 이미 있으면 저장X 예외메세지 처리하기
                    subscribeService_ksh.saveSubscribe(subDto);
                    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusMonths(1);
                    Instant instant = date.toInstant();
                    long timeStamp = instant.getEpochSecond();

                    // 다음결제 예약
                    result = subscribeService_ksh.schedulePay(subDto, timeStamp);

                    if (!"success".equals(result)) {
                        // 예약 실패일 경우 구독 해지 처리 subscription_pay 테이블에 status N으로 변경 -> 관리자 문의
                        subscribeService_ksh.updateCancel(subDto.getMerchantUid());
                        return "redirect:/subscription/order?result=" + result;
                    }
                } else {
                    // 잔액 부족등과 같은 결제 오류가 났을경우 다음날로 결제 예약, 결제 시도 3번 후 더이상 예약 X
                    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusDays(1);
                    Instant instant = date.toInstant();
                    long timeStamp = instant.getEpochSecond();

                    int count = subscribeService_ksh.getRepayCount(merchantUid);
                    if (count < 3) {
                        result = subscribeService_ksh.schedulePay(subDto, timeStamp);
                        subscribeService_ksh.updateRepayCount(merchantUid);
                        if (!"success".equals(result)) {
                            subscribeService_ksh.updateCancel(subDto.getMerchantUid());
                        }
                    } else {
                        subscribeService_ksh.updateCancel(subDto.getMerchantUid());
                    }
                }
            } else {
                // 결제금액 불일치. 위/변조 된 결제
                String refundRes = subscribeService_ksh.refund(imp_uid, payPrice);
                result = "Fraudulent payment attempt refund "+refundRes;
                return "redirect:/subscription/order?result=" + result;
            }
        }
        catch (Exception e) {
            // 오류 -> 관리자 문의
            return "redirect:/subscription/order?result=" + e.getMessage();
        }
        return "redirect:/subscription/confirm?result=" + result;
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

        return map;
    }

    @PostMapping("/payments/pc")
    public @ResponseBody Map<String, String> paymentsComplete(@RequestBody SubscriptionPayDto subData) {
        // pc결제
        String result = "";
        Map<String, String> map = new HashMap<>();
        int payPrice = 0;
        try {
            // imp_uid 또는 merchant_uid로 아임포트 서버에서 결제 정보 조회
            String requestUrl = "https://api.iamport.kr/payments/" + subData.getImpUid();
            JsonObject jsonObj = subscribeService_ksh.getData(requestUrl);
            JsonObject data = jsonObj.get("response").getAsJsonObject();
            String customerUid = data.get("customer_uid").getAsString();
            String merchantUid = data.get("merchant_uid").getAsString();
            String impUid = data.get("imp_uid").getAsString();
            int amountToBePaid = 0;
            int selectMonth = Integer.parseInt(data.get("name").getAsString().substring(0, 1));

            if (selectMonth == 1) {
                amountToBePaid = Subscribe.ONE.getPrice();
            } else if (selectMonth == 3) {
                amountToBePaid = Subscribe.THREE.getPrice();
            } else if (selectMonth == 6) {
                amountToBePaid = Subscribe.SIX.getPrice();
            } else if (selectMonth == 12) {
                amountToBePaid = Subscribe.TWELVE.getPrice();
            } else {
                amountToBePaid = -1;
            }

            payPrice = Integer.parseInt(data.get("amount").toString());
            // 결제금액 일치. 결제 된 금액 === 결제 되어야 하는 금액
            if (payPrice == amountToBePaid) {
                SubscriptionPayDto subDto = new SubscriptionPayDto();
                subDto.setMemberId(Long.parseLong(customerUid));
                subDto.setSelectMonth(selectMonth);
                subDto.setMerchantUid(merchantUid);
                subDto.setCustomerUid(customerUid);
                subDto.setAmount(payPrice);
                subDto.setImpUid(impUid);
                if (data.get("status").getAsString().equals("paid")) {
                    // DB에 결제 정보 저장 <- DB에 이미 있으면 저장X 예외메세지 처리하기
                    subscribeService_ksh.saveSubscribe(subDto);
                    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusMonths(1);
                    Instant instant = date.toInstant();
                    long timeStamp = instant.getEpochSecond();

                    // 다음결제 예약
                    result = subscribeService_ksh.schedulePay(subDto, timeStamp);

                    if (!"success".equals(result)) {
                        // 예약 실패일 경우 구독 해지 처리 subscription_pay 테이블에 status N으로 변경 -> 관리자 문의
                        subscribeService_ksh.updateCancel(subDto.getMerchantUid());
                        map.put("result", result);
                    }
                } else {
                    // 잔액 부족등과 같은 결제 오류가 났을경우 다음날로 결제 예약, 결제 시도 3번 후 더이상 예약 X
                    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusDays(1);
                    Instant instant = date.toInstant();
                    long timeStamp = instant.getEpochSecond();

                    int count = subscribeService_ksh.getRepayCount(merchantUid);
                    if (count < 3) {
                        result = subscribeService_ksh.schedulePay(subDto, timeStamp);
                        subscribeService_ksh.updateRepayCount(merchantUid);
                        if (!"success".equals(result)) {
                            subscribeService_ksh.updateCancel(subDto.getMerchantUid());
                        }
                    } else {
                        subscribeService_ksh.updateCancel(subDto.getMerchantUid());
                    }
                }
            } else {
                // 결제금액 불일치. 위/변조 된 결제
                String refundRes = subscribeService_ksh.refund(subData.getImpUid(), payPrice);
                result = "Fraudulent payment attempt refund "+refundRes;
                map.put("result", result);
            }
        }
        catch (Exception e) {
            // 오류 -> 관리자 문의
            map.put("result", e.getMessage());

        }

        map.put("result", result);
        return map;
    }

    @GetMapping("/subscribe/unschedule/{memberId}")
    public @ResponseBody Map<String, String> cancleTest(@PathVariable("memberId") Long memberId) {
        // 구독 취소
        Map<String, String> map = new HashMap<>();

        try {
            SubscriptionPayDto info = subscribeService_ksh.getScheduleInfo(memberId);
            String cancelUrl =  "https://api.iamport.kr/subscribe/payments/unschedule";
            String jsonBody = String.format("{\"customer_uid\": \"%s\", \"merchant_uid\": \"%s\"}", info.getCustomerUid(), info.getMerchantUidRe());
            String scheduleStatus = subscribeService_ksh.postData(cancelUrl, jsonBody).getAsJsonArray("response").get(0).getAsJsonObject().get("schedule_status").getAsString();

            if("revoked".equals(scheduleStatus)) {
                subscribeService_ksh.updateCancel(info.getMerchantUid());
                map.put("result", "success");
            } else {
                map.put("result", "fail");
            }

        } catch (IOException e) {
            map.put("error", e.getMessage());
        }
        return map;
    }
}



