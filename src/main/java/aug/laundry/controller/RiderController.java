package aug.laundry.controller;

import aug.laundry.domain.Orders;
import aug.laundry.domain.Rider;
import aug.laundry.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
//@RequestMapping("/ride/*")
public class RiderController {

    private final RiderService riderService;

    @GetMapping("/ride/wait")
    public String waitList(Model model) {
        List<Map<String, Integer>> cnt = riderService.orderListCnt();
        List<Orders> orderList = riderService.OrderList("대기중");
        Rider riderInfo = riderService.riderInfo("스크류바");

//        List<Orders> orderList = new ArrayList<>();

//        for(Orders area : list){
//            if(area.getOrdersAddress().contains(riderInfo.getWorkingArea())){
//                orderList.add(area);
//            }
//        }
//        System.out.println(orderList);

        model.addAttribute("orderList", orderList);
        model.addAttribute("cnt", cnt);
        model.addAttribute("riderInfo",riderInfo);

        return "project_rider_list_on_call";
    }

    @GetMapping("/ride/accept")
    public String acceptList(Model model) {
        List<Orders> orderList = riderService.OrderList("진행중");
        List<Map<String, Integer>> cnt = riderService.orderListCnt();

        model.addAttribute("orderList", orderList);
        model.addAttribute("cnt", cnt);

        return "project_rider_using_list";
    }

    @GetMapping("/ride/finish")
    public String finishList(Model model) {
        List<Orders> orderList = riderService.OrderList("완료");
        List<Map<String, Integer>> cnt = riderService.orderListCnt();

        model.addAttribute("orderList", orderList);
        model.addAttribute("cnt", cnt);

        return "project_rider_complete";
    }

    @MessageMapping("/order-complete-message/{storeId}")
    @SendTo("/topic/order-complete/{storeId}")
    public Map<String, Object> message(@DestinationVariable long storeId, String message) {
        System.out.println("가게번호 : " + storeId);
        System.out.println("메세지 도착 :" + message);

        Map<String, Object> map = new HashMap<>();

        Orders orders = new Orders();
        orders.setOrdersId(1234L);
        orders.setOrdersDate("2023/08/31");
//        orders.setOrdersAddress("서울시 서대문구 홍은동 454");
        orders.setOrdersAddress("서울시 강북구 번동 657");
        orders.setOrdersAddressDetails("108동 505호");

        map.put("a",orders);
        map.put("storeId", storeId);
        return map;
    }

    @GetMapping("/oo/kk/{storeId}")
    public String http33(@PathVariable Long storeId, HttpSession session, Model model) {
        model.addAttribute("storeId", storeId);
        return "redirect:/oo/63";
    }

    @GetMapping("/oo/63")
    public String h(){
        return "oo";
    }

    @GetMapping("/ride/orders/{ordersId}")
    public String orderInfo(@PathVariable("ordersId") Long ordersId, Model model) {
        Orders info = riderService.orderInfo(ordersId);
        System.out.println(info);
        model.addAttribute("info", info);
        return "project_rider_read_more";
    }

    @PostMapping("/ride/assign/{ordersId}")
    public String orderCheck(@PathVariable("ordersId") Long ordersId, Model model){
        Orders orders = new Orders();
        orders.setOrdersId(ordersId);

        int res = riderService.updateOrderRider(orders);
        int res2 = riderService.updateOrderStatus(orders);
        System.out.println(res);
        System.out.println(res2);

        return "redirect:/ride/accept";
    }



}
