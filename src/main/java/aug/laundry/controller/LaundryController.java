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
        Map<String, Integer> info = laundryService.firstInfo(1L); // 빠른세탁, 드라이클리닝, 생활빨래, 수선 선택여부
        List<CouponList> coupon = laundryService.getCoupon(1L); // 내가 보유한 쿠폰
        model.addAttribute("info", info);
        model.addAttribute("coupon", coupon);
        model.addAttribute("couponCount", coupon.size());
        System.out.println("coupon = " + coupon);
        System.out.println("coupon.size() = " + coupon.size());
        System.out.println("info = " + info);
        return "project_order_confirm";
    }
}
