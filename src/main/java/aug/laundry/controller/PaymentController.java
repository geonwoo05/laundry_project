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
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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

        memberId=4L;

        Optional<Paymentinfo> paymentinfoFromDB = paymentDao.findPaymentinfoByImpUid(payment.getImp_uid());

        //모바일 리다이렉트로직이랑 웹훅에 의해 결제정보 2건저장 방지로직
        //DB에 결제내역이 없다면 저장
        if(!paymentinfoFromDB.isPresent()){
            log.info("웹훅보다 먼저 실행");
            iamportClient = new IamportClient(restApi, restApiSecret);
            IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(payment.getImp_uid());
            Payment response = irsp.getResponse();

            Paymentinfo paymentinfo = new Paymentinfo(
                    memberId, response.getImpUid(), response.getPayMethod(), response.getMerchantUid(),
                    response.getBuyerName(), response.getBuyerTel(), response.getAmount().longValue()
            );

            paymentService.savePaymentInfo(paymentinfo);
            paymentService.isValid(irsp, paymentinfo.getPaymentinfoId(), memberId, ordersId, payment);
            ordersServiceKdh.updateOrdersStatusToCompletePayment(ordersId);

            if(payment.getCouponListId() != null){
                log.info("쿠폰상태변경");
                ordersServiceKdh.updateCouponListStatusToUsedCoupon(payment.getCouponListId(), ordersId);
            }

        if(payment.getPointPrice() != null){
            //음수로 변환
            Long pointPrice = -payment.getPointPrice();
            ordersServiceKdh.addPoint(memberId, pointPrice, "포인트 사용");
        }
        }

        //redirect로 바꿔야함
        return "project_search_id_result";
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
                iamportClient = new IamportClient(restApi, restApiSecret);
                IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(webHookImpUid);
                Payment response = irsp.getResponse();

                Paymentinfo paymentinfo = new Paymentinfo(
                        memberId, response.getImpUid(), response.getPayMethod(), response.getMerchantUid(),
                        response.getBuyerName(), response.getBuyerTel(), response.getAmount().longValue()
                );

                paymentService.savePaymentInfo(paymentinfo);


                paymentService.isValid(irsp, paymentinfo.getPaymentinfoId(), memberId, ordersId,
                        new PaymentCheckRequestDto(couponListId, couponPrice, pointPrice, response.getImpUid(), response.getMerchantUid(), true));
                // 주문상태 결제완료로 변경
                ordersServiceKdh.updateOrdersStatusToCompletePayment(ordersId);

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
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
