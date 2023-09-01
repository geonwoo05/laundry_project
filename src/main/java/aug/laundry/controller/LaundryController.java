package aug.laundry.controller;

import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.DateForm;
import aug.laundry.dto.OrderInfo;
import aug.laundry.enums.category.CategoryOption;
import aug.laundry.service.LaundryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LaundryController {

    private final LaundryService laundryService;

    @GetMapping("/laundry/order")
    public String order(Model model) {
        OrderInfo info = laundryService.firstInfo(63L); // 빠른세탁, 드라이클리닝, 생활빨래, 수선 선택여부
        List<CouponList> coupon = laundryService.getCoupon(63L); // 내가 보유한 쿠폰
        DateForm dateForm = new DateForm();

        Address address = laundryService.getAddress(63L);

        model.addAttribute("quickLaundry", info.getIsQuick());
        model.addAttribute("info", getJoin(info.getIsDry(), info.getIsCommon(), info.getIsRepair()));
        model.addAttribute("infoPrice", info);
        model.addAttribute("dateTime", dateForm);
        model.addAttribute("address", address);
        model.addAttribute("coupon", coupon);
        model.addAttribute("couponCount", coupon.size());
        System.out.println("address = " + address);
        System.out.println("dateForm = " + dateForm);
        System.out.println("coupon = " + coupon);
        System.out.println("coupon.size() = " + coupon.size());
        System.out.println("info = " + info);
        return "project_order_confirm";
    }

    @GetMapping("/members/{memberId}/coupons/select")
    public String selectCoupon(@PathVariable Long memberId) {

        return "project_use_coupon";
    }

    @NotNull
    private static String getJoin(Long isDry, Long isCommon, Long isRepair) {
        String[] strArr = new String[(int)(isDry + isCommon + isRepair)];
        int cnt = 0;
        if (isDry != 0) strArr[cnt++] = CategoryOption.DRYCLEANING.getTitle();
        if (isCommon != 0) strArr[cnt++] = CategoryOption.COMMON_LAUNDRY.getTitle();
        if (isRepair != 0) strArr[cnt++] = CategoryOption.REPAIR.getTitle();
        return String.join(",", strArr);
    }





}
