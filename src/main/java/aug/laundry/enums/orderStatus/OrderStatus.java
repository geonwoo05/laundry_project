package aug.laundry.enums.orderStatus;

import aug.laundry.domain.Orders;
import aug.laundry.dto.OrdersEnum;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum OrderStatus {

    R1("주문취소", "님의 주문이 취소되었어요.", "다음에 다시 이용해주세요."),
    R2("주문완료", "님의 주문이 접수되었어요.", "기사님이 배정되면 알려드릴께요."),
    R3("수거전", "님의 세탁물을 가지러가고 있어요.", "지정하신 위치에 세탁물을 놓아주세요."),
    R4("수거완료", "님의 세탁물을 수거했어요.", "?"),
    R5("업체배송완료", "님의 세탁물이 저희에게 도착했어요.", "검수가 끝나면 담당자가 연락드릴꺼에요."), //검수전
    R6("검수완료", "님의 세탁물이 검수가 끝났어요.", "주문내역에서 결과를 확인해주세요."),
    R7("결제대기", "님의 결제를 기다리고있어요.", "저희의 전문가들을 믿고 맡겨주세요."),
    R8("결제완료", "님의 결제가 확인되었어요.", "?"),
    R9("세탁중", "님의 세탁물이 새롭게 태어나고 있어요.", "?"),
    R10("세탁완료", "님의 세탁물이 새로워졌어요.", "받아보시면 깜짝 놀라실꺼에요."),
    R11("수거전", "님의 세탁물이 집에 돌아갈 준비를 하고있어요.", "얼른 보내드릴께요."),
    R12("수거완료", "님의 세탁물이 집에 가는중이에요.", "도착하면 알려드릴께요."),
    R13("배달완료", "님의 소중한 세탁물이 도착했어요", "이용해주셔서 감사합니다."),
    R14("종료", null, null); // 메인페이지에서 R13이 표시되고 메인페이지에서 나가면 13 -> 14로 변경후 표시 x

    private String title;
    private String content;
    private String subContent;

    OrderStatus(String title, String content, String subContent) {
        this.title = title;
        this.content = content;
        this.subContent = subContent;
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