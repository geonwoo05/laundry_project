package aug.laundry.dto;

import aug.laundry.enums.orderStatus.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrdersListResponseDto {


    private String ordersDate;
    private Integer ordersStatus;
    private OrderStatus statusEnum;
    private String ordersPickup;
    private String ordersPickupDate;
    private String ordersReturnDate;

    public OrdersListResponseDto() {
    }
}
