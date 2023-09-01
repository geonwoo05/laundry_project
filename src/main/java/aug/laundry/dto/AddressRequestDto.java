package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddressRequestDto {

  @NotBlank
  private String memberZipcode;
  @NotBlank
  private String memberAddress;
  @NotBlank
  private String memberAddressDetails;
}
