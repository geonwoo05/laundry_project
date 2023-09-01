package aug.laundry.dao;

import aug.laundry.dao.admin.AdminInspectionMapper;
import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.RepairInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class InspectionTest {

    @Autowired
    private AdminInspectionMapper inspection;

    @Test
    public void getList() {

        List<AdminInspectionDto> list = inspection.getInspectionList();

        Assertions.assertThat(1).isEqualTo(list.size());
    }

    @Test
    public void detailTest() {
        Long ordersId = 8L;
        AdminInspectionDto orderInfo = inspection.getOrderInfo(ordersId);

        Long orderDetailId = inspection.getOrderDetailId(ordersId);

        CommonLaundry commonLaundry = inspection.getCommonLaundryInfo(orderDetailId);
        List<Drycleaning> drycleanings = inspection.getDrycleaningInfo(orderDetailId);
        List<RepairInfoDto> repairInfoDtos = inspection.getRepairInfo(orderDetailId);

        Assertions.assertThat(3L).isEqualTo(orderDetailId);
        Assertions.assertThat(4L).isEqualTo(commonLaundry.getCommonLaundryId());
    }
}
