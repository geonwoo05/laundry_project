package aug.laundry.dao.orders;

import aug.laundry.dto.OrdersResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrdersDao {

    private final OrdersMapper ordersMapper;

    public OrdersResponseDto findByOrdersId(Long ordersId){
        OrdersResponseDto findOrder = ordersMapper.findByOrdersId(ordersId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        log.info("orderResponseDto={}", findOrder);
        return findOrder;
    }

}
