package aug.laundry.dao.rider;

import org.apache.ibatis.annotations.Mapper;
import aug.laundry.domain.Orders;

import java.util.List;

@Mapper
public interface RiderMapper {

    List<Orders> orderList();

    int orderListCnt();

    List<Orders> orderInfo(Orders orders);

    int updateOrderRider(Orders orders);

    int updateOrderStatus(Orders orders);


}
