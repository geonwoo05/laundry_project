package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.domain.CouponList;
import aug.laundry.dto.*;
import aug.laundry.enums.category.*;
import aug.laundry.service.LaundryService;
import aug.laundry.validator.OrderPostValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/laundry")
public class LaundryController {

    private final LaundryService laundryService;
    private final OrderPostValidator orderPostValidator;

    @InitBinder("orderPost")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(orderPostValidator);
    }

    @GetMapping("/order")
    public String order(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId, Model model) {
        MemberShip memberShip = laundryService.isPass(memberId); // 패스 여부

        OrderInfo info = laundryService.orderInfo(model);
//        laundryService.firstInfo(memberId); // 빠른세탁, 드라이클리닝, 생활빨래, 수선 선택여부

        Long totalPrice = 0L;
        Long discount = 0L;

        totalPrice += info.isQuick() ? Delivery.QUICK_DELIVERY.getPrice() : Delivery.COMMON_DELIVERY.getPrice(); // 빠른배송이면 적용되는 가격

        List<MyCoupon> coupon = laundryService.getCoupon(memberId); // 내가 보유한 쿠폰
        Address address = laundryService.getAddress(memberId); // 주소 가져오기
        DateForm dateForm = new DateForm(); // 날짜 가져오기

        if (info.isCommon()) {
            model.addAttribute("common", Category.BASIC);
            totalPrice += memberShip.apply(Category.BASIC.getPrice()); // 생활빨래 기본금액
            discount += Category.BASIC.getPrice() - memberShip.apply(Category.BASIC.getPrice()); // 할인금액
        }
        if (info.isDry()) {
            List<Category> getDry = laundryService.getDry(memberId);
            model.addAttribute("dry", getDry);
            Long dryTotalPrice = getDry.stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get();
            model.addAttribute("dryTotalPrice", dryTotalPrice);
            totalPrice += memberShip.apply(dryTotalPrice); // 드라이클리닝 총 금액
            discount += dryTotalPrice - memberShip.apply(dryTotalPrice); // 할인금액
        }
        if (info.isRepair()) {
            List<Category> getRepair = laundryService.getRepair(memberId);
            model.addAttribute("repair", getRepair);
            Long repairTotalPrice = getRepair.stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get();
            model.addAttribute("repairTotalPrice", repairTotalPrice);
            totalPrice += memberShip.apply(repairTotalPrice); // 수선 총 금액
            discount += repairTotalPrice - memberShip.apply(repairTotalPrice); // 할인금액
        }


        model.addAttribute("delivery", info.isQuick() ? Delivery.COMMON_DELIVERY : Delivery.QUICK_DELIVERY);
        model.addAttribute("memberShip", memberShip.getCheck() == Pass.PASS ? 1 : null);
        model.addAttribute("totalPrice", Math.round(totalPrice / 100) * 100);
        model.addAttribute("discount", discount);
        model.addAttribute("quickLaundry", info.isQuick());
        model.addAttribute("info", getJoin(info.isDry(), info.isCommon(), info.isRepair()));
        model.addAttribute("infoPrice", info);
        model.addAttribute("dateTime", dateForm);
        model.addAttribute("address", address);
        model.addAttribute("coupon", coupon);
        model.addAttribute("couponCount", coupon.size());
        return "project_order_confirm";
    }

    @PostMapping("/order")
    public String orderPost(@Validated @ModelAttribute OrderPost orderPost, BindingResult bindingResult, Model model, @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId) {

        System.out.println("orderPost = " + orderPost);
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult in Controller = " + bindingResult);
            System.out.println(bindingResult.getAllErrors());
            return "redirect:/laundry/order";
        }
        laundryService.update(memberId, orderPost.getCoupon(), orderPost); // 쿠폰 유효성 검사


        return "redirect:/";
    }

    @GetMapping("/order/pickup")
    public String pickupLocation() {
        return "project_pickup_location";
    }

    @GetMapping("/order/coupons/select")
    public String selectCoupon(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId, Model model) {
        if (memberId == null) return "redirect:/login";

        List<MyCoupon> getCoupon = laundryService.getCoupon(memberId);
        model.addAttribute("memberId", memberId);
        model.addAttribute("coupon", getCoupon);

        return "project_use_coupon";
    }

    @NotNull
    private static String getJoin(boolean isDry, boolean isCommon, boolean isRepair) {
        int arrLength = 0;
        if (isDry) arrLength++;
        if (isCommon) arrLength++;
        if (isRepair) arrLength++;

        String[] strArr = new String[arrLength];
        int cnt = 0;
        if (isDry) strArr[cnt++] = CategoryOption.DRYCLEANING.getTitle();
        if (isCommon) strArr[cnt++] = CategoryOption.COMMON_LAUNDRY.getTitle();
        if (isRepair) strArr[cnt++] = CategoryOption.REPAIR.getTitle();
        return String.join(",", strArr);
    }




}
