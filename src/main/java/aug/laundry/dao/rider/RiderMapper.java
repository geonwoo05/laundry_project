package aug.laundry.dao.rider;

import org.apache.ibatis.annotations.Mapper;
import aug.laundry.domain.Orders;

import java.util.List;
import java.util.Map;

@Mapper
public interface RiderMapper {

    List<Orders> orderList(String status);

    List<Map<String, Integer>> orderListCnt();
    Orders orderInfo(Orders orders);

    int updateOrderRider(Orders orders);

    int updateOrderStatus(Orders orders);


}
