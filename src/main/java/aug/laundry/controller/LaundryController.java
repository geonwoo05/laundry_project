package aug.laundry.controller;

import aug.laundry.domain.CouponList;
import aug.laundry.service.LaundryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/laundry")
public class LaundryController {

    private final LaundryService laundryService;

    @GetMapping("/order")
    public String order(Model model) {
        Map<String, Integer> info = laundryService.firstInfo(1L);
        List<CouponList> coupon = laundryService.getCoupon(1L);
        System.out.println("coupon = " + coupon);
        System.out.println("coupon.size() = " + coupon.size());
        System.out.println("info = " + info);
        return "project_order_confirm";
    }
}
