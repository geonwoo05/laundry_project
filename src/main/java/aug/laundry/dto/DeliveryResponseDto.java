package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeliveryResponseDto {


    private boolean deliveryStatus;  //Lombok과 타임리프에서의 문법 차이점 때문에 boolean 타입이지만 Status를 붙임

}
