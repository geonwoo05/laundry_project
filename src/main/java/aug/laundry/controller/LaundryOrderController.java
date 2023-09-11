package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.DateForm;
import aug.laundry.enums.category.CategoryPriceCalculator;
import aug.laundry.enums.category.MemberShip;
import aug.laundry.enums.category.Pass;
import aug.laundry.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/laundry")
public class LaundryOrderController {

    private final CommonLaundryService_sdj commonLaundryService;
    private final DrycleaningService drycleaningService;
    private final RepairService repairService;
    private final MainService mainService;
    private final LaundryService laundryService;

    @GetMapping
    public String first(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId, HttpSession session, Model model) {

        laundryService.check(memberId, session); // 이전에 존재하던 장바구니 테이블이 있으면 삭제하고 새로 생성 , 없으면 그냥 생성, 생성후에 session 발급

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

    @Transactional
    @PostMapping("/dry/order")
    public @ResponseBody ResponseEntity<String> dryOrder(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId,
                                                         @SessionAttribute(name = SessionConstant.ORDERS_CONFIRM, required = false) Long ordersDetailId,
                                                         @RequestBody Map<String, Integer> result) {

        boolean status = laundryService.insertDrycleaning(memberId, ordersDetailId, result);


        return status ? new ResponseEntity<String>("success", HttpStatus.OK) : new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
    }
}
