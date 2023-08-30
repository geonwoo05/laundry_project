package aug.laundry.service;

import aug.laundry.controller.LaundryController;
import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.OrderInfo;

import java.util.List;
import java.util.Map;

public interface LaundryService {

    OrderInfo firstInfo(Long memberId);

    List<CouponList> getCoupon(Long memberId);

    Address getAddress(Long memberId);
}
