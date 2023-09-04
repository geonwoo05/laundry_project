package aug.laundry.service;

import aug.laundry.domain.Orders;
import aug.laundry.domain.Rider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RiderService {

    List<Orders> OrderList(String status);

    List<Map<String, Integer>> orderListCnt();

    Orders orderInfo(Long ordersId);

    int updateOrderRider(Orders orders);

    int updateOrderStatus(Orders orders);

    Rider riderInfo(String riderName);


}
