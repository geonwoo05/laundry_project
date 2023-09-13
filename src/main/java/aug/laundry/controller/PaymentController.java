package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dao.payment.PaymentDao;
import aug.laundry.domain.Paymentinfo;
import aug.laundry.dto.PaymentCheckRequestDto;
import aug.laundry.dto.WebHook;
import aug.laundry.service.OrdersService_kdh;
import aug.laundry.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Controller
@Slf4j
public class PaymentController {

    @Value("${import.rest-api}")
    private String restApi;

    @Value("${import.rest-api-secret}")
    private String restApiSecret;

    private IamportClient iamportClient;
    private PaymentService paymentService;
    private OrdersService_kdh ordersServiceKdh;
    private PaymentDao paymentDao;

    @Autowired
    public PaymentController(PaymentService paymentService, OrdersService_kdh ordersService_kdh,
                             PaymentDao paymentDao) {
        this.paymentService = paymentService;
        this.ordersServiceKdh = ordersService_kdh;
        this.paymentDao = paymentDao;
    }


    @GetMapping("/orders/{ordersId}/payment/complete")
    public String complete(@PathVariable Long ordersId, @ModelAttribute PaymentCheckRequestDto payment,
                           @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false)Long memberId) throws IamportResponseException, IOException {

        if(payment.isImp_success() == false){
            return "redirect:/orders/" + ordersId +"/payment";
        }


        Optional<Paymentinfo> paymentinfoFromDB = paymentDao.findPaymentinfoByImpUid(payment.getImp_uid());

        //모바일 리다이렉트로직이랑 웹훅에 의해 결제정보 2건저장 방지로직
        //DB에 결제내역이 없다면 저장
        if(!paymentinfoFromDB.isPresent()){
            log.info("결제후 리다이렉트 실행");

            String impUid = payment.getImp_uid();
            Paymentinfo paymentinfo = makePaymentinfoFromIamPort(iamportClient, impUid, restApi, restApiSecret, memberId);
            IamportResponse<Payment> irsp = makeIamPortResponse(iamportClient, impUid, restApi, restApiSecret);

            try{
                paymentService.savePaymentInfo(paymentinfo);
            } catch(DataIntegrityViolationException e){
                log.info("결제정보 리다이렉트/웹훅 중복저장 방지용으로 try catch로 잡음");
            }

            //검증
            paymentService.isValid(irsp, paymentinfo.getPaymentinfoId(), memberId, ordersId, payment);

            Long paymentinfoId = paymentinfo.getPaymentinfoId();
            Long couponListId = payment.getCouponListId();
            Long pointPrice = payment.getPointPrice();
            Long finalPrice = paymentinfo.getAmount();

            updateSeveralRegardingOrders(finalPrice, ordersId, memberId, paymentinfoId, couponListId, pointPrice);
        }
        return "redirect:/payment/complete";
    }
    
    @ResponseBody
    @PostMapping("/orders/{ordersId}/payment/webhook")
    public ResponseEntity<String> webhook(@RequestBody WebHook webHook, @PathVariable Long ordersId,
                                          @RequestParam(required = false) Long memberId, @RequestParam(required = false) Long couponListId,
                                          @RequestParam(required = false) Long couponPrice, @RequestParam(required = false) Long pointPrice) throws IamportResponseException, IOException {

        log.info("웹훅 수신성공={}", webHook);

        if(webHook.getStatus().equals("paid")){
            String webHookImpUid = webHook.getImp_uid();

            Optional<Paymentinfo> paymentinfoFromDB = paymentDao.findPaymentinfoByImpUid(webHookImpUid);

            //모바일 리다이렉트로직이랑 웹훅에 의해 결제정보 2건저장 방지로직
            //DB에 결제내역이 없다면 저장
            if(!paymentinfoFromDB.isPresent()){
                log.info("리다이렉트보다 먼저 실행");
                Paymentinfo paymentinfo = makePaymentinfoFromIamPort(iamportClient, webHookImpUid, restApi, restApiSecret, memberId);
                IamportResponse<Payment> irsp = makeIamPortResponse(iamportClient, webHookImpUid, restApi, restApiSecret);

                try{
                    paymentService.savePaymentInfo(paymentinfo);
                } catch(DataIntegrityViolationException e){
                    log.info("결제정보 리다이렉트/웹훅 중복저장 방지용으로 try catch로 잡음");
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                //검증
                paymentService.isValid(irsp, paymentinfo.getPaymentinfoId(), memberId, ordersId,
                        new PaymentCheckRequestDto(couponListId, couponPrice, pointPrice, irsp.getResponse().getImpUid(), irsp.getResponse().getMerchantUid(), true));

                Long paymentinfoId = paymentinfo.getPaymentinfoId();
                Long finalPrice = paymentinfo.getAmount();

                updateSeveralRegardingOrders(finalPrice, ordersId, memberId, paymentinfoId, couponListId, pointPrice);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateSeveralRegardingOrders(Long finalPrice, Long ordersId, Long memberId, Long paymentinfoId, Long couponListId, Long pointPrice) {

        ordersServiceKdh.updatePriceNStatusNPaymentinfo(finalPrice, paymentinfoId, ordersId);

        if(couponListId != null){
            log.info("쿠폰상태변경");
            ordersServiceKdh.updateCouponListStatusToUsedCoupon(couponListId, ordersId);
        }

        if(pointPrice != null){
            //음수로 변환
            Long pointValue = -pointPrice;
            ordersServiceKdh.addPoint(memberId, pointValue, "포인트 사용");
        }
    }

    private Paymentinfo makePaymentinfoFromIamPort(IamportClient iamportClient, String impUid, String restApi, String restApiSecret, Long memberId) throws IamportResponseException, IOException {
        IamportResponse<Payment> irsp = makeIamPortResponse(iamportClient, impUid, restApi, restApiSecret);
        Payment response = irsp.getResponse();

        return new Paymentinfo(
                memberId, response.getImpUid(), response.getPayMethod(), response.getMerchantUid(),
                response.getBuyerName(), response.getBuyerTel(), response.getAmount().longValue()
        );
    }

    private IamportResponse<Payment> makeIamPortResponse(IamportClient iamportClient, String impUid, String restApi, String restApiSecret) throws IamportResponseException, IOException {
        iamportClient = new IamportClient(restApi, restApiSecret);
        return iamportClient.paymentByImpUid(impUid);
    }

    @GetMapping("/payment/complete")
    public String completeForm(){
        return "project_payment_success";
    }


}
