package aug.laundry.dao;

import aug.laundry.domain.CouponList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LaundryMapper {

    Integer isQuick(Long memberId);
    Integer isDry(Long memberId);
    Integer isCommon(Long memberId);
    Integer isRepair(Long memberId);

    List<CouponList> getCoupon(Long memberId);
}
