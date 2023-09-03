package aug.laundry.service;

import aug.laundry.controller.LaundryController;
import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.MyCoupon;
import aug.laundry.dto.OrderInfo;
import aug.laundry.enums.category.Category;

import java.util.List;
import java.util.Map;

public interface LaundryService {

    OrderInfo firstInfo(Long memberId);

    List<MyCoupon> getCoupon(Long memberId);

    Address getAddress(Long memberId);

    List<Category> getDry(Long memberId);

    List<Category> getRepair(Long memberId);
}
