package aug.laundry.dao;

import aug.laundry.domain.CouponList;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Repair;
import aug.laundry.dto.*;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.Pass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LaundryRepository {

    private final LaundryMapper laundryMapper;

    public OrderInfo firstInfo(Long memberId) {
        return laundryMapper.firstInfo(memberId);
    }

    public List<MyCoupon> getCoupon(Long memberId) {
        return laundryMapper.getCoupon(memberId);
    }


    public Address getAddress(Long memberId) {
        return laundryMapper.getAddress(memberId);
    }

    public List<Category> getDry(Long memberId) {
        return laundryMapper.getDry(memberId).stream().map(x -> Category.valueOf(x)).collect(Collectors.toList());
    }

    public List<Category> getRepair(Long memberId) {
        return laundryMapper.getRepair(memberId).stream().map(x -> Category.valueOf(x)).collect(Collectors.toList());
    }

    public Pass isPass(Long memberId) {
        return laundryMapper.isPass(memberId) == null ? Pass.COMMON : Pass.PASS;
    }

    public boolean validCoupon(Long memberId, Long couponListId) {
        CouponList couponList = laundryMapper.validCoupon(memberId, couponListId);
        if (couponList == null || couponList.getMemberId() != memberId || couponList.getCouponListId() != couponListId || couponList.getCouponListStatus() != 1) {
            return false;
        }
        return true;
    }

    public Integer useCoupon(Long memberId, Long couponListId) {
        return laundryMapper.useCoupon(memberId, couponListId);
    }


    public Integer insert(Orders orders) {
        return laundryMapper.insert(orders);
    }

    public Long getCouponDiscount(Long memberId, Long couponListId) {
        return laundryMapper.getCouponDiscount(memberId, couponListId);
    }

    public Long check(Long memberId, Long ordersDetailId) {
        return laundryMapper.check(memberId, ordersDetailId);
    }

    @Transactional
    public void removeOrdersDetail(Long ordersDetailId) {
        laundryMapper.removeDrycleaning(ordersDetailId); // 드라이클리닝 장바구니 삭제
        log.info("remove Drycleaning");
        laundryMapper.removeCommon(ordersDetailId); // 생활빨래 장바구니 삭제
        log.info("remove Common");
        laundryMapper.removeRepair(ordersDetailId); // 수선 장바구니 삭제
        log.info("remove Repair");
        laundryMapper.removeOrdersDetail(ordersDetailId); // 장바구니 삭제
        log.info("remove OrdersDetail");
    }

    public void createOrdersDetail(Long memberId) {
        laundryMapper.createOrdersDetail(memberId);
    }

    public void insertDryCleaning(Long ordersDetailId, Category category) {
        laundryMapper.insertDryCleaning(ordersDetailId, category.name());
    }

    public void removeDryCleaning(Long ordersDetailId) {
        laundryMapper.removeDrycleaning(ordersDetailId);
    }

    public void removeRepair(Long ordersDetailId) {
        laundryMapper.removeRepair(ordersDetailId);
    }
    public void removeCommon(Long ordersDetailId) {
        laundryMapper.removeCommon(ordersDetailId);
    }

    public List<OrderDrycleaning> reloadDrycleaning(Long orderDetailId) {
        List<OrderDrycleaning> result = laundryMapper.reloadDrycleaning(orderDetailId);
        if (result == null || result.isEmpty()) return null;
        return result;
    }

    public List<OrderRepair> reloadRepair(Long orderDetailId) {
        return laundryMapper.reloadRepair(orderDetailId);
    }

    public List<String> getRepairImage(Long repairId) {
        return laundryMapper.getRepairImage(repairId);
    }
}
