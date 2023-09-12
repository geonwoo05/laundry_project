package aug.laundry.dao.payment;

import aug.laundry.dao.orders.OrdersMapper;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Paymentinfo;
import aug.laundry.domain.Repair;
import aug.laundry.dto.CouponCheckDto;
import aug.laundry.dto.OrdersResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PaymentDao {

    private final PaymentMapper paymentMapper;

    public CouponCheckDto findCouponByCouponListId(Long couponListId) {
        CouponCheckDto coupon = paymentMapper.findCouponByCouponListId(couponListId);
        log.info("coupon={}", coupon);
        return coupon;
    }

    public void savePaymentInfo(Paymentinfo paymentinfo) {
        try{
            paymentMapper.savePaymentInfo(paymentinfo);
        } catch(DataIntegrityViolationException e){
            log.info("결제정보 리다이렉트/웹훅 중복저장 방지용으로 try catch로 잡음");
        }
    }

    public Paymentinfo findPaymentinfoByPaymentinfoId(Long paymentinfoId){
        Paymentinfo paymentinfo = paymentMapper.findPaymentinfoByPaymentinfoId(paymentinfoId);
        log.info("paymentinfo={}", paymentinfo);
        return paymentinfo;
    }

    public int updateRefundInfoBypaymentinfoId(Long paymentinfoId, String errorMessage){
        return paymentMapper.updateRefundInfoBypaymentinfoId(paymentinfoId, errorMessage);
    }

    public Optional<Paymentinfo> findPaymentinfoByImpUid(String impUid){
        return paymentMapper.findPaymentinfoByImpUid(impUid);
    }
}
