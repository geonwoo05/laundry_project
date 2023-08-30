package aug.laundry.dao;

import aug.laundry.controller.LaundryController;
import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.OrderInfo;
import aug.laundry.service.LaundryServiceImpl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LaundryMapper {

    OrderInfo firstInfo(Long memberId);

    Integer isQuick(Long memberId);
    Integer isDry(Long memberId);
    Integer isCommon(Long memberId);
    Integer isRepair(Long memberId);

    List<CouponList> getCoupon(Long memberId);

    Address getAddress(Long memberId);
}
