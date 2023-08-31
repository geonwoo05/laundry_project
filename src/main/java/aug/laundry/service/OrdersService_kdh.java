package aug.laundry.service;

import aug.laundry.dao.orders.OrdersDao;
import aug.laundry.domain.Drycleaning;
import aug.laundry.dto.DrycleaningResponseDto;
import aug.laundry.dto.OrdersResponseDto;
import aug.laundry.enums.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService_kdh {

    private final OrdersDao ordersDao;

    public OrdersResponseDto findByOrdersId(Long ordersId){
        return ordersDao.findByOrdersId(ordersId);
    }

    public List<DrycleaningResponseDto> findDryCleaningByOrdersId(Long ordersId){

        List<Drycleaning> dryList = ordersDao.findDryCleaningByOrdersId(ordersId);

        if(dryList.isEmpty())               {
            return Collections.EMPTY_LIST;
        } else{
            return dryList.stream()
                    .map(dry -> new DrycleaningResponseDto(
                            Category.valueOf(dry.getDrycleaningCategory()),
                            dry.getDrycleaningPossibility(),
                            dry.getDrycleaningNotReason(),
                            Category.valueOf(dry.getDrycleaningCategory()).getPrice()
                    )).collect(Collectors.toList());
        }

    }

}
