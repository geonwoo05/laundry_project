package aug.laundry.service;

import aug.laundry.dao.rider.RiderMapper;
import aug.laundry.domain.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService{

    private final RiderMapper riderMapper;

    @Override
    public List<Orders> OrderList(String status) {
        return riderMapper.orderList(status);
    }

    @Override
    public List<Map<String, Integer>> orderListCnt() {
        return riderMapper.orderListCnt();
    }

    @Override
    public List<Orders> orderInfo(Orders orders) {
        return riderMapper.orderInfo(orders);
    }
}
