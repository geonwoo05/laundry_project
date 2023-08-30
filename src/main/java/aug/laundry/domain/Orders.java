package aug.laundry.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Orders {

    private Long ordersId;
    private Long memberId;
    private String ordersDate;
    private String ordersAddress;
    private String ordersAddressDetails;
    private String ordersInfo;
    private String ordersRequest;
    private Integer ordersPay;
    private Long quickLaundryId;
    private String ordersPickup;
    private Integer ordersExpectedPrice;
    private Integer ordersFinalPrice;
    private Integer ordersPayStatus;
    private Long riderId;
    private Long quickRiderId;

    public Orders() {
    }
}
