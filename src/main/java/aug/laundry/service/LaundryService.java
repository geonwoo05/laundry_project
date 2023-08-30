package aug.laundry.service;

import aug.laundry.domain.CouponList;

import java.util.List;
import java.util.Map;

public interface LaundryService {

    Map<String,Integer> firstInfo(Long memberId);

    List<CouponList> getCoupon(Long memberId);
}
