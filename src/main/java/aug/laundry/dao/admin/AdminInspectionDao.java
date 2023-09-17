package aug.laundry.dao.admin;

import aug.laundry.domain.*;
import aug.laundry.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AdminInspectionDao {

    private final AdminInspectionMapper mapper;

    public List<AdminInspectionDto> getInspectionList(Criteria cri, Long orderStatus) {
        return mapper.getInspectionList(cri, orderStatus);
    }
    public int getTotalCount(Long ordersStatus){
        return mapper.getTotalCount(ordersStatus);
    }

    public AdminInspectionDto getOrderInfo(Long ordersId) {
        return mapper.getOrderInfo(ordersId);
    }

    public AdminCommonLoundryDto getCommonLaundryInfo(Long orderDetailId) {
        return mapper.getCommonLaundryInfo(orderDetailId);
    }

    public List<AdminDrycleaningDto> getDrycleaningInfo(Long orderDetailId) {
        return mapper.getDrycleaningInfo(orderDetailId);
    }

    public List<AdminRepairDto> getRepairInfo(Long orderDetailId) {
        return mapper.getRepairInfo(orderDetailId);
    }
    public List<String> getRepairImage(Long repairId){
        return mapper.getRepairImage(repairId);
    }
    public int updateCommon(AdminCommonLoundryDto commonLaundryDto){
        return mapper.updateCommon(commonLaundryDto);
    }
    public int updateRepair(AdminRepairDto repair){
        return mapper.updateRepair(repair);
    }
    public int updateDrycleaning(AdminDrycleaningDto drycleaning){
        return mapper.updateDrycleaning(drycleaning);
    }
    public int updateInspectionStatus(Long ordersId, Long adminId){
        return mapper.updateInspectionStatus(ordersId, adminId);
    }
    public int updateOrderStatus(Long ordersId){
        return mapper.updateOrderStatus(ordersId);
    }
    public AdminInspectionDto getOrderSearchInfo(Long ordersId, Long ordersStatus) {
        return mapper.getOrderSearchInfo(ordersId, ordersStatus);
    }
    public List<InspectionImage> getInspectionImageList(Long ordersId){
        return mapper.getInspectionImageList(ordersId);
    }

    public int deleteImage(String inspectionImageStoreName) {
        return mapper.deleteImage(inspectionImageStoreName);
    }
}
