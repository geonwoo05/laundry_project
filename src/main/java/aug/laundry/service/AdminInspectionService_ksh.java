package aug.laundry.service;

import aug.laundry.dto.AdminInspectionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminInspectionService_ksh {
    List<AdminInspectionDto> getInspectionList();

    List<AdminInspectionDto> getInspectedList();

    Map<String, Object> getInspectionDetail(Long ordersId);

}
