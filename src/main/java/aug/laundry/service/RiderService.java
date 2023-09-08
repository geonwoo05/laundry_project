package aug.laundry.service;

import aug.laundry.domain.DeliveryImage;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Rider;
import aug.laundry.dto.OrdersEnum;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RiderService {

    List<OrdersEnum> OrderList(String status);
    List<OrdersEnum> OrderListEnum(String status);

    List<Map<String, Integer>> orderListCnt();

    Orders orderInfo(Long ordersId);

    int updateOrderRider(Orders orders);

    int updateOrderStatus(Orders orders);

    Rider riderInfo(String riderName);

    DeliveryImage finishImg(Long ordersId);

    List<OrdersEnum> routineOrderList(@Param("ordersAddress")String ordersAddress, @Param("status")String status);

    Rider routineRider(String riderName);

    List<Map<String, Integer>> routineOrderCnt();

    Map<String, Integer> routineTotalCnt(String RIDER_POSSIBLE_ZIPCODE);

//    List<Map<String, Integer>> dongCnt(String ordersAddress);

    List<Integer> dongCnt(String ordersAddress, String status);
}
