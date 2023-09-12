package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Rider;
import aug.laundry.dto.OrdersEnum;
import aug.laundry.service.FileUploadService_ksh;
import aug.laundry.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RiderRestController {

    private final RiderService riderService;
    private final FileUploadService_ksh fileUpload;

    @GetMapping("/region/list/{ordersAddress}/{presentStatus}")
    public Map<String, Object> list(@PathVariable("ordersAddress")String ordersAddress, @PathVariable("presentStatus")String presentStatus, @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId){
        Map<String, Object> map =new HashMap<>();

        List<OrdersEnum> list = riderService.routineOrderList(ordersAddress, presentStatus, 2L);
        Rider rider = riderService.routineRider(memberId);
        List<Map<String, Integer>> cnt = riderService.routineOrderCnt(memberId);

        System.out.println(cnt);
        System.out.println(list);
        System.out.println(rider);

        map.put("list", list);
        map.put("rider", rider);
        map.put("cnt", cnt);
        return map;
    }
}
