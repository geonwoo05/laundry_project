package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.DateForm;
import aug.laundry.enums.category.CategoryPriceCalculator;
import aug.laundry.enums.category.MemberShip;
import aug.laundry.enums.category.Pass;
import aug.laundry.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/laundry")
public class LaundryOrderController {

    private final CommonLaundryService_sdj commonLaundryService;
    private final DrycleaningService_sdj drycleaningService;
    private final MainService mainService;
    private final LaundryService laundryService;
    private final RepairService_sdj repairService;

    @GetMapping
    public String first(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId, Model model) {
        DateForm dateForm = new DateForm(); // 수거, 배송에 대한 정보
        model.addAttribute("dateTime", dateForm);
        return "project_order_1";
    }

    @GetMapping("/dry")
    public String drycleaning(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId, Model model) {
        Map<String, Map<String, Long>> category = mainService.getCategory();
        MemberShip memberShip = laundryService.isPass(memberId);
        model.addAttribute("category", category);
        if (memberShip.getCheck() == Pass.PASS) {
            Float percent = CategoryPriceCalculator.PASS.percent();
            model.addAttribute("percent", percent);
        }
        return "project_order_2_1";
    }

    @GetMapping("/repair")
    public String repair() {

        return "project_order_repair";
    }
}
