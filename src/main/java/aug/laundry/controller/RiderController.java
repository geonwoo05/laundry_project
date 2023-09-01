package aug.laundry.controller;

import aug.laundry.domain.CouponList;
import aug.laundry.domain.Orders;
import aug.laundry.service.LaundryService;
import aug.laundry.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/ride/*")
public class RiderController {

    @GetMapping("/ride/wait")
    public String waitList(Model model) {
        return "project_rider_list_on_call";
    }

    @GetMapping("/ride/accept")
    public String acceptList(Model model) {
        List<Long> list = Arrays.asList(1L,2L,3L,4L,5L);
        model.addAttribute("orderList", list);
        return "project_rider_using_list";
    }

    @GetMapping("/ride/finish")
    public String finishList(Model model) {
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
        orders.setOrdersAddress("제주도");
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
    public String orderInfo(Model model) {
        return "project_rider_read_more";
    }



}
