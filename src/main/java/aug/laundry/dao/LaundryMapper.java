package aug.laundry.dao;

import aug.laundry.controller.LaundryController;
import aug.laundry.domain.CouponList;
import aug.laundry.domain.Orders;
import aug.laundry.dto.*;
import aug.laundry.enums.category.Category;
import aug.laundry.service.LaundryServiceImpl;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

@Mapper
public interface LaundryMapper {

    OrderInfo firstInfo(Long memberId);

    Integer isQuick(Long memberId);
    Integer isDry(Long memberId);
    Integer isCommon(Long memberId);
    Integer isRepair(Long memberId);

    List<MyCoupon> getCoupon(Long memberId);

    Address getAddress(Long memberId);

    List<String> getDry(Long memberId);

    List<String> getRepair(Long memberId);

    Integer isPass(Long memberId);

    CouponList validCoupon(@Param("memberId") Long memberId, @Param("couponListId") Long couponListId);

    Integer useCoupon(@Param("memberId") Long memberId, @Param("couponListId") Long couponListId);

    Integer insert(Orders orders);

    Long getCouponDiscount(@Param("memberId") Long memberId, @Param("couponListId") Long couponListId);

    Long check(@Param("memberId") Long memberId, @Param("ordersDetailId") Long ordersDetailId);

    void removeOrdersDetail(Long memberId);

    void createOrdersDetail(Long memberId);

    void removeDrycleaning(Long ordersDetailId);

    void removeCommon(Long ordersDetailId);

    void removeRepair(Long ordersDetailId);

    void insertDryCleaning(@Param("ordersDetailId") Long ordersDetailId, @Param("category") String title);

    List<OrderDrycleaning> reloadDrycleaning(Long orderDetailId);

    List<OrderRepair> reloadRepair(Long orderDetailId);

    List<String> getRepairImage(Long repairId);
}
