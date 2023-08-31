package aug.laundry.dto;


import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class MemberDto {

    private Long memberId;
    private String memberAccount;
    private String memberPassword;
    private String memberName;
    private String memberPhone;
    private Integer memberZipcode;
    private String memberAddress;
    private String memberCreateDate;
    private String memberSocial;
    private Long subscriptionId;
    private String subscriptionExpireDate;
    private Long gradeId;
    private String memberRecentlyDate;
    private char memberDeleteStatus;
    private String memberMyInviteCode;
    private String memberAddressDetails;
    private String memberInviteCode;

    // 기본 생성자 추가
    public MemberDto() {
    }

    public MemberDto(Long memberId, String memberAccount, String memberPassword, String memberName, String memberPhone, Integer memberZipcode, String memberAddress, String memberCreateDate, String memberSocial, Long subscriptionId, String subscriptionExpireDate, Long gradeId, String memberRecentlyDate, char memberDeleteStatus, String memberMyInviteCode, String memberAddressDetails, String memberInviteCode) {
        this.memberId = memberId;
        this.memberAccount = memberAccount;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.memberZipcode = memberZipcode;
        this.memberAddress = memberAddress;
        this.memberCreateDate = memberCreateDate;
        this.memberSocial = memberSocial;
        this.subscriptionId = subscriptionId;
        this.subscriptionExpireDate = subscriptionExpireDate;
        this.gradeId = gradeId;
        this.memberRecentlyDate = memberRecentlyDate;
        this.memberDeleteStatus = memberDeleteStatus;
        this.memberMyInviteCode = memberMyInviteCode;
        this.memberAddressDetails = memberAddressDetails;
        this.memberInviteCode = memberInviteCode;
    }







}
