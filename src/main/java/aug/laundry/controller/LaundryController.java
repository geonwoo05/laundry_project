package aug.laundry.controller;

import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.DateForm;
import aug.laundry.dto.MyCoupon;
import aug.laundry.dto.OrderInfo;
import aug.laundry.enums.category.Category;
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
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.ModuleElement;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LaundryController {

    private final LaundryService laundryService;

    @GetMapping("/laundry/order")
    public String order(Model model, HttpServletRequest request) {
        OrderInfo info = laundryService.firstInfo(1L); // 빠른세탁, 드라이클리닝, 생활빨래, 수선 선택여부
        List<MyCoupon> coupon = laundryService.getCoupon(1L); // 내가 보유한 쿠폰
        Address address = laundryService.getAddress(1L); // 주소 가져오기
        DateForm dateForm = new DateForm();
        if (info.getIsDry() != 0) {
            List<Category> getDry = laundryService.getDry(1L);
            model.addAttribute("dry", getDry);
            Long dryTotalPrice = getDry.stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get();
            model.addAttribute("dryTotalPrice", dryTotalPrice);
        }
        if (info.getIsRepair() != 0) {
            List<Category> getRepair = laundryService.getRepair(1L);
            model.addAttribute("repair", getRepair);
            Long repairTotalPrice = getRepair.stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get();
            model.addAttribute("repairTotalPrice", repairTotalPrice);
        }

        model.addAttribute("quickLaundry", info.getIsQuick());
        model.addAttribute("info", getJoin(info.getIsDry(), info.getIsCommon(), info.getIsRepair()));
        model.addAttribute("infoPrice", info);
        model.addAttribute("dateTime", dateForm);
        model.addAttribute("address", address);
        model.addAttribute("coupon", coupon);
        model.addAttribute("couponCount", coupon.size());


        return "project_order_confirm";
    }

    @GetMapping("/members/{memberId}/coupons/select")
    public String selectCoupon(@PathVariable Long memberId, Model model, String takeDate) {
        List<MyCoupon> getCoupon = laundryService.getCoupon(memberId);
        model.addAttribute("memberId", memberId);
        model.addAttribute("coupon", getCoupon);

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
