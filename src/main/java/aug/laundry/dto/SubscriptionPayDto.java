package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubscriptionPayDto {
    private Long subscriptionPayId;
    private Long memberId; // 로그인 아이디
    private int selectMonth;// 구독 개월 수
    private String merchantUid;
    private String merchantUidRe; // 다음번 예약 uid
    private String customerUid;
    private int amount;
    private String payDate;
    private char subscriptionStatus;
    private String impUid;

    public SubscriptionPayDto() {

    }
}
