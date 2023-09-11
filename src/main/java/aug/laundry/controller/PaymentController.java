package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.domain.Paymentinfo;
import aug.laundry.dto.PaymentCheckRequestDto;
import aug.laundry.service.OrdersService_kdh;
import aug.laundry.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.IOException;

@Controller
public class PaymentController {

    @Value("${import.rest-api}")
    private String restApi;

    @Value("${import.rest-api-secret}")
    private String restApiSecret;

    private IamportClient iamportClient;
    private PaymentService paymentService;
    private OrdersService_kdh ordersServiceKdh;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping("/orders/{ordersId}/payment/complete")
    public String complete(@PathVariable Long ordersId, @ModelAttribute PaymentCheckRequestDto payment,
                           @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false)Long memberId) throws IamportResponseException, IOException {

        if(payment.isImp_success() == false){
            return "redirect:/orders/" + ordersId +"/payment";
        }


        iamportClient = new IamportClient(restApi, restApiSecret);
        IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(payment.getImp_uid());
        Payment response = irsp.getResponse();

        Paymentinfo paymentinfo = new Paymentinfo(
                memberId, response.getImpUid(), response.getPayMethod(), response.getMerchantUid(),
                response.getBuyerName(), response.getBuyerTel(), response.getAmount().longValue()
                );//memberId 세션에서 가져온 값으로 바꿔야함

        try {
            paymentService.savePaymentInfo(paymentinfo);
        } catch(IllegalStateException e){

            iamportClient.cancelPaymentByImpUid(
                    new CancelData(
                            irsp.getResponse().getImpUid(), true, irsp.getResponse().getAmount()));
            
            return ""; // 결제정보가 db에 저장되지않아서 취소후 결제시스템 오류있다고 안내페이지 보여줘야함
        }

        paymentService.isValid(irsp, paymentinfo.getPaymentinfoId(), memberId, ordersId, payment);


//        ordersServiceKdh.updateOrdersStatusToCompletePayment(ordersId);  //검증하지 못함. 나중에 실행되는지 확인

        //검증하지 못함 나중에 실행되는 지 확인 (쿠폰 사용하면 쿠폰상태 사용완료로 변경)
//        if(payment.getCouponListId() != null){
//            ordersServiceKdh.updateCouponListStatusToUsedCoupon(payment.getCouponListId());
//        }
        //검증하지 못함 나중에 실행되는지확인 포인트 차감
//        if(payment.getPointPrice() != null){
//            //음수로 변환
//            Long pointPrice = -payment.getPointPrice();
//            ordersServiceKdh.addPoint(memberId, pointPrice, "포인트 사용");
//        }

        //redirect로 바꿔야함
        return "project_search_id_result";
    }
}
