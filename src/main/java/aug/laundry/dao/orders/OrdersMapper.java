package aug.laundry.dao.orders;

import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Repair;
import aug.laundry.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrdersMapper {

    Optional<OrdersResponseDto> findByOrdersId(Long ordersId);

    List<Drycleaning> findDryCleaningByOrdersId(Long ordersId);

    List<Repair> findRepairByOrdersId(Long ordersId);

    int findQuickLaundryByOrdersId(Long ordersId);

    int updateExpectedNDiscountPriceByOrdersId(@Param("ordersId") Long ordersId,
                                      @Param("expectedPrice") Long expectedPrice,
                                      @Param("subscriptionDiscountPrice") Long subscriptionDiscountPrice);

    Optional<Long> findExpectedPriceByOrdersId(Long ordersId);

    int updateOrdersStatusToCompletePayment(Long ordersId, int constPaySuccess);

    int updateCouponListStatusToUsedCoupon(@Param("couponListId") Long couponListId,
                                           @Param("ordersId") Long ordersId, @Param("constCouponListStatus") int constCouponListStatus);

    int addPoint(@Param("pointDto") AddPointResponseDto pointDto);

    List<OrdersListResponseDto> findOrdersByMemberIdAndCri(@Param("cri") Criteria cri, @Param("memberId") Long memberId);

    List<OrdersListResponseDto> findOrdersFinishedByMemberIdAndCri(@Param("cri") Criteria cri, @Param("memberId") Long memberId);

    int getTotalCount(@Param("memberId") Long memberId, @Param("constOrderStatus") int constOrderStatus);

    int updatePaymentinfoIdByOrdersId(@Param("paymentinfoId") Long paymentinfoId, @Param("ordersId") Long ordersId);

    int updatePriceNStatusNPaymentinfo(
            @Param("ordersFinalPrice") Long ordersFinalPrice, @Param("paymentinfoId") Long paymentinfoId,
            @Param("ordersId") Long ordersId, @Param("constPaySuccess") int constPaySuccess);

    Optional<PriceResponseDto> findPricesByOrdersId(Long ordersId);

    int updatePointIdByOrdersId(@Param("pointId") Long pointId, @Param("ordersId") Long ordersId);

    int updateCouponStatusNOrdersId(@Param("couponListStatus") int couponListStatus, @Param("ordersId") Long ordersId, @Param("couponListId") Long couponListId);
}
