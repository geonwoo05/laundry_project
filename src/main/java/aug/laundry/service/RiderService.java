package aug.laundry.service;

import aug.laundry.dao.rider.RiderMapper;
import aug.laundry.domain.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderService {

    private RiderMapper riderMapper;

    public List<Orders> OrderList(){
        System.out.println("Service");
        return riderMapper.orderList();
    }

}
