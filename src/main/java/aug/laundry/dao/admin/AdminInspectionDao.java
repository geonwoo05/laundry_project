package aug.laundry.dao.admin;

import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.RepairInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AdminInspectionDao {

    private final AdminInspectionMapper mapper;

    public List<AdminInspectionDto> getInspectionList() {
        return mapper.getInspectionList();
    }

    public List<AdminInspectionDto> getInspectedList() {
        return mapper.getInspectedList();
    }

    public AdminInspectionDto getOrderInfo(Long ordersId) {
        return mapper.getOrderInfo(ordersId);
    }

    public CommonLaundry getCommonLaundryInfo(Long orderDetailId) {
        return mapper.getCommonLaundryInfo(orderDetailId);
    }

    public List<Drycleaning> getDrycleaningInfo(Long orderDetailId) {
        return mapper.getDrycleaningInfo(orderDetailId);
    }

    public List<RepairInfoDto> getRepairInfo(Long orderDetailId) {
        return mapper.getRepairInfo(orderDetailId);
    }
}
