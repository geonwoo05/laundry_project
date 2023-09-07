package aug.laundry.controller;

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
    public Map<String, Object> list(@PathVariable("ordersAddress")String ordersAddress, @PathVariable("presentStatus")String presentStatus){
        Map<String, Object> map =new HashMap<>();

        List<Orders> list = riderService.routineOrderList(ordersAddress, presentStatus);
        Rider rider = riderService.routineRider("홀란드");
        List<Map<String, Integer>> cnt = riderService.routineOrderCnt();

        System.out.println(cnt);
        System.out.println(list);
        System.out.println(rider);

        map.put("list", list);
        map.put("rider", rider);
        map.put("cnt", cnt);
        return map;
    }
}
