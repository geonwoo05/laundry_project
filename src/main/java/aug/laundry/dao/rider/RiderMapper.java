package aug.laundry.dao.rider;

import aug.laundry.domain.DeliveryImage;
import aug.laundry.domain.Rider;
import aug.laundry.dto.OrdersEnum;
import org.apache.ibatis.annotations.Mapper;
import aug.laundry.domain.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RiderMapper {

    List<Orders> orderList(String status);
    List<OrdersEnum> orderListEnum(String status);

    List<Map<String, Integer>> orderListCnt();
    Orders orderInfo(Long ordersId);

    int updateOrderRider(Orders orders);

    int updateOrderStatus(Orders orders);

    Rider riderInfo(String riderName);

    DeliveryImage finishImg(Long ordersId);

    List<Orders> routineOrderList(@Param("ordersAddress")String ordersAddress, @Param("status")String status);

    Rider routineRider(String riderName);

    List<Map<String, Integer>> routineOrderCnt();

    Map<String, Integer> routineTotalCnt(String zipCode);

//    Map<String, Integer> dongCnt(String ordersAddress);

    int dongCnt(@Param("ordersAddress")String ordersAddress, @Param("status")String status);
}
