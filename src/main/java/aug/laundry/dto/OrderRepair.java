package aug.laundry.dto;

import aug.laundry.enums.repair.Repair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRepair {

    private Long repairId;
    private Long ordersDetailId;
    private String repairRequest;
    private Repair repairCategory;
    private char repairPossibility;
    private String repairNotReason;

    public OrderRepair(Long repairId, Long ordersDetailId, String repairRequest, Repair repairCategory, char repairPossibility, String repairNotReason) {
        this.repairId = repairId;
        this.ordersDetailId = ordersDetailId;
        this.repairRequest = repairRequest;
        this.repairCategory = repairCategory;
        this.repairPossibility = repairPossibility;
        this.repairNotReason = repairNotReason;
    }
}