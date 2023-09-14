package aug.laundry.dao.admin;

import aug.laundry.domain.*;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.CommonLaundryDto;
import aug.laundry.dto.Criteria;
import aug.laundry.dto.RepairInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminInspectionMapper {

    List<AdminInspectionDto> getInspectionList(@Param("cri") Criteria cri, @Param("orderStatus") Long orderStatus);
    int getTotalCount(Long ordersStatus);
    // 주문정보조회
    AdminInspectionDto getOrderInfo(Long ordersId);
    // 생활빨래
    CommonLaundry getCommonLaundryInfo(Long orderDetailId);
    // 드라이클리닝
    List<Drycleaning> getDrycleaningInfo(Long orderDetailId);
    //수선
    List<Repair> getRepairInfo(Long orderDetailId);
    List<String> getRepairImage(Long repairId);
    AdminInspectionDto getOrderSearchInfo(@Param("ordersId") Long ordersId,@Param("ordersStatus") Long ordersStatus);
    List<InspectionImage> getInspectionImageList(Long ordersId);
    int updateCommon(CommonLaundryDto commonLaundryDto);
    int updateRepair(Repair repair);
    int updateDrycleaning(Drycleaning drycleaning);
    int updateInspectionStatus(@Param("ordersId") Long ordersId, @Param("adminId")Long adminId);
    int updateOrderStatus(Long ordersId);
    int deleteImage(String inspectionImageStoreName);
}
