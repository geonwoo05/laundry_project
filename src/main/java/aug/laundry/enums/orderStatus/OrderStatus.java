package aug.laundry.enums.orderStatus;

import aug.laundry.domain.Orders;
import aug.laundry.dto.OrdersEnum;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum OrderStatus {

    R1("주문취소"),
    R2("주문완료"),
    R3("수거전"),
    R4("수거완료"),
    R5("업체배송완료"), //검수전
    R6("검수완료"),
    R7("결제대기"),
    R8("결제완료"),
    R9("세탁중"),
    R10("세탁완료"),
    R11("수거전"),
    R12("수거완료"),
    R13("배달완료");

    private String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public static List<OrdersEnum> change(List<Orders> orders) {
        return orders.stream().map( x -> {
            return OrdersEnum.builder()
                    .ordersId(x.getOrdersId())
                    .memberId(x.getMemberId())
                    .ordersDate(x.getOrdersDate())
                    .ordersAddress(x.getOrdersAddress())
                    .ordersAddressDetails(x.getOrdersAddressDetails())
                    .ordersInfo(x.getOrdersInfo())
                    .ordersRequest(x.getOrdersRequest())
                    .ordersPay(x.getOrdersPay())
                    .ordersPickup(x.getOrdersPickup())
                    .ordersExpectedPrice(x.getOrdersExpectedPrice())
                    .ordersStatus(OrderStatus.valueOf("R" + x.getOrdersStatus()).getTitle())
                    .riderId(x.getRiderId())
                    .quickRiderId(x.getQuickRiderId())
                    .ordersPickupDate(x.getOrdersPickupDate())
                    .ordersReturnDate(x.getOrdersReturnDate())
                    .build();
        }).collect(Collectors.toList());
    }

}
