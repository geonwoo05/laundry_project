package aug.laundry.dao.admin;

import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.RepairInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminInspectionMapper {

    List<AdminInspectionDto> getInspectionList();

    List<AdminInspectionDto> getInspectedList();

    // 주문정보조회
    AdminInspectionDto getOrderInfo(Long ordersId);
    // 생활빨래
    CommonLaundry getCommonLaundryInfo(Long orderDetailId);
    // 드라이클리닝
    List<Drycleaning> getDrycleaningInfo(Long orderDetailId);
    //수선
    List<RepairInfoDto> getRepairInfo(Long orderDetailId);



}
