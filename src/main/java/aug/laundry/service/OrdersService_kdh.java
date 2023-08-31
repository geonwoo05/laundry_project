package aug.laundry.service;

import aug.laundry.dao.orders.OrdersDao;
import aug.laundry.dto.OrdersResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersService_kdh {

    private final OrdersDao ordersDao;

    public OrdersResponseDto findByOrdersId(Long orderId){
        return ordersDao.findByOrdersId(orderId);
    }

}
