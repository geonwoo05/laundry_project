package aug.laundry.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Repair {

    private Long repairId;
    private Long ordersDetailId;
    private String repairRequest;
    private String repairCategory;

    public Repair() {
    }
}
