package aug.laundry.dao.Mapper;

import org.apache.ibatis.annotations.Mapper;
import aug.laundry.domain.Orders;

import java.util.List;

@Mapper
public interface RiderMapper {

    List<Orders> riderList();

    List<Orders> acceptRiderList();

    List<Orders> finishRiderList();
}
