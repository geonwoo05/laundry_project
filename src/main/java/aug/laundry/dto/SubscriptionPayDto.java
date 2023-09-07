package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubscriptionPayDto {
    private Long subscriptionPayId;
    private Long member_id; // 로그인 아이디
    private int select_month;// 구독 개월 수
    private String merchant_uid;
    private String merchant_uid_r; // 다음번 예약 uid
    private String customer_uid;
    private int amount;
    private String pay_date;
    private char subscription_status;
    private String imp_uid;

    public SubscriptionPayDto() {

    }
}
