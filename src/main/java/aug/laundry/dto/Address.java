package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Address {

    private String memberAddress;
    private String memberAddressDetails;

    public Address(String memberAddress, String memberAddressDetails) {
        this.memberAddress = memberAddress;
        this.memberAddressDetails = memberAddressDetails;
    }
}
