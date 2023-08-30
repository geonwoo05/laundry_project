package aug.laundry.dao;

import aug.laundry.controller.LaundryController;
import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.OrderInfo;
import aug.laundry.enums.category.CategoryOption;
import aug.laundry.service.LaundryServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LaundryRepository {

    private final LaundryMapper laundryMapper;

    public OrderInfo firstInfo(Long memberId) {
//        Map<String, Integer> map = new ConcurrentHashMap<>();
//        if (Objects.nonNull(laundryMapper.isQuick(memberId))) map.put(CategoryOption.QUICK_LAUNDRY.getTitle(), 1);
//        if (Objects.nonNull(laundryMapper.isCommon(memberId))) map.put(CategoryOption.COMMON_LAUNDRY.getTitle(), 1);
//        if (Objects.nonNull(laundryMapper.isDry(memberId))) map.put(CategoryOption.DRYCLEANING.getTitle(), 1);
//        if (Objects.nonNull(laundryMapper.isRepair(memberId))) map.put(CategoryOption.REPAIR.getTitle(), 1);
        return laundryMapper.firstInfo(memberId);
    }

    public List<CouponList> getCoupon(Long memberId) {
        return laundryMapper.getCoupon(memberId);
    }


    public Address getAddress(Long memberId) {
        return laundryMapper.getAddress(memberId);
    }
}
