package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UpdateAddressDto {

  @NotBlank
  private String memberZipcode;

  @NotBlank
  private String memberAddress;

  @NotBlank
  private String memberAddressDetails;

  public UpdateAddressDto() {
  }
}
