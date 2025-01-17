package aug.laundry.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonLaundry {

    private Long commonLaundryId;
    private Integer commonLaundryWeight;
    private Long ordersDetailId;

    public CommonLaundry() {
    }
}
