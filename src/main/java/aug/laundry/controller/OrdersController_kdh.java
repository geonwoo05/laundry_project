package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.DrycleaningResponseDto;
import aug.laundry.dto.OrdersResponseDto;
import aug.laundry.dto.QuickLaundryResponseDto;
import aug.laundry.dto.RepairResponseDto;
import aug.laundry.enums.category.CategoryOption;
import aug.laundry.enums.category.Delivery;
import aug.laundry.service.OrdersService_kdh;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/orders")
public class OrdersController_kdh {

    private final OrdersService_kdh ordersServiceKdh;

    @GetMapping("/orders")
    public String orders(){


        return "";
    }

    @GetMapping("/{ordersId}/payment")
    public String payOrder(@PathVariable Long ordersId,
                           @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false)Long memberId,
                           Model model){


        OrdersResponseDto ordersResponseDto = ordersServiceKdh.findByOrdersId(ordersId);

//        if(memberId != ordersResponseDto.getMemberId()){
//            throw new IllegalStateException("비정상적인 요청입니다. (결제회원과 로그인회원이 일치하지않음)");
//        }

        Map<String, Object> dryMap = ordersServiceKdh.findDryCleaningByOrdersId(ordersId);
        Map<String, Object> repairMap = ordersServiceKdh.findRepairByOrdersId(ordersId);
        boolean isQuickLaundry = ordersServiceKdh.isQuickLaundry(ordersId);
        Integer point = ordersServiceKdh.findPointByMemberId(1L);  //2L수정해야함

        //배송금액 로직 (생활빨래, 드라이클리닝, 수선만 포함)(배송비X 빠른세탁 X)
        Long totalPrice = ordersResponseDto.getCommonLaundryPrice() +
                (Long)dryMap.get("totalDryPrice") +
                (Long)repairMap.get("totalRepairPrice");

        if(totalPrice >= 30000){

        }



        log.info("dryMap={}", dryMap);
        log.info("repairMap={}", repairMap);
        model.addAttribute("order", ordersResponseDto);
        model.addAttribute("dryMap", dryMap);
        model.addAttribute("repairMap", repairMap);
        model.addAttribute("point", point);
        model.addAttribute("memberId", 1L);
        model.addAttribute("quickLaundry",
                new QuickLaundryResponseDto(Delivery.QUICK_DELIVERY, isQuickLaundry));

        return "project_order_view";
    }

    @GetMapping("/members/{memberId}/coupons")
    public String coupons(@PathVariable Long memberId){

        return "project_order_view_coupon";
    }

}

