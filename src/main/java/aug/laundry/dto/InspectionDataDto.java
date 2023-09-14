package aug.laundry.dto;

import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class InspectionDataDto {

    Long inspectionId;
    CommonLaundryDto commonLaundryDto ;
    private List<Drycleaning> drycleaningList;
    private List<Repair> repairList;
    private List<String> deleteFileList;

    public InspectionDataDto() {

    }

}
