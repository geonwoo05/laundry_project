package aug.laundry.service;

import aug.laundry.dao.admin.AdminInspectionDao;
import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.RepairInfoDto;
import aug.laundry.enums.category.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminInspectionServiceImpl_ksh implements AdminInspectionService_ksh{

    private final AdminInspectionDao adminInspectionDao;

    @Override
    public List<AdminInspectionDto> getInspectionList() {
        return adminInspectionDao.getInspectionList();
    }

    @Override
    public List<AdminInspectionDto> getInspectedList() {
        return adminInspectionDao.getInspectedList();
    }

    @Override
    public Map<String, Object> getInspectionDetail(Long ordersId) {

        Map<String, Object> detailInfo = new HashMap<>();

        Long orderDetailId = adminInspectionDao.getOrderInfo(ordersId).getOrdersDetailId();

        detailInfo.put("orderDetailId", orderDetailId);

        // 나중에 오류처리 추가해두기
        detailInfo.put("orderInfo", adminInspectionDao.getOrderInfo(ordersId));

        CommonLaundry commonLaundry = adminInspectionDao.getCommonLaundryInfo(orderDetailId);
        if(commonLaundry == null){
            detailInfo.put("commonLaundryInfo", null);
        } else {
            detailInfo.put("commonLaundryInfo", commonLaundry);
        }

        List<Drycleaning> drycleaningInfo = adminInspectionDao.getDrycleaningInfo(orderDetailId);
        if(drycleaningInfo.size() == 0){
            detailInfo.put("drycleaningInfo", null);
        } else {
            for (Drycleaning drycleaning : drycleaningInfo) {
                drycleaning.setDrycleaningCategory(Category.valueOf(drycleaning.getDrycleaningCategory()).getTitle());
            }
            detailInfo.put("drycleaningInfo", drycleaningInfo);
        }

        List<RepairInfoDto> repairInfo = adminInspectionDao.getRepairInfo(orderDetailId);
        if(repairInfo.size() == 0){
            detailInfo.put("repairInfo", null);
        } else {
            for (RepairInfoDto repairInfoDto : repairInfo) {
                repairInfoDto.setRepairCategory(Category.valueOf(repairInfoDto.getRepairCategory()).getTitle());
            }
            detailInfo.put("repairInfo", repairInfo);
        }

        return detailInfo;
    }
}
