package aug.laundry.service;

import aug.laundry.domain.Orders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RiderService {

    List<Orders> OrderList(String status);

    List<Map<String, Integer>> orderListCnt();

    List<Orders> orderInfo(Orders orders);


}
