package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.DateForm;
import aug.laundry.dto.OrderDrycleaning;
import aug.laundry.dto.OrderRepair;
import aug.laundry.dto.RepairFormData;
import aug.laundry.enums.category.CategoryPriceCalculator;
import aug.laundry.enums.category.MemberShip;
import aug.laundry.enums.category.Pass;
import aug.laundry.enums.repair.RepairCategory;
import aug.laundry.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
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
    public String drycleaning(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId,
                              @SessionAttribute(name = SessionConstant.ORDERS_CONFIRM, required = false) Long orderDetailId,
                              Model model) {

        // 카테고리
        Map<String, Map<String, Long>> category = mainService.getCategory();
        MemberShip memberShip = laundryService.isPass(memberId);
        model.addAttribute("category", category);
        if (memberShip.getCheck() == Pass.PASS) {
            model.addAttribute("percent", CategoryPriceCalculator.PASS.percent());
        }

        // 현재 장바구니에서 기록이 있는지 확인후 장바구니 가져오기
        List<OrderDrycleaning> reload = laundryService.reloadDrycleaning(orderDetailId); // null 이면 이전에 저장된값 없음
        System.out.println("reload = " + reload);
        model.addAttribute("reload", reload);
        return "project_order_2_1";
    }

    @GetMapping("/repair")
    public String repair(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId,
                         @SessionAttribute(name = SessionConstant.ORDERS_CONFIRM, required = false) Long orderDetailId,
                         Model model) {
        MemberShip memberShip = laundryService.isPass(memberId);
        if (memberShip.getCheck() == Pass.PASS){
            model.addAttribute("percent", CategoryPriceCalculator.PASS.percent());
        }
        RepairCategory[] repairCategory = RepairCategory.values();

        List<OrderRepair> reload = laundryService.reloadRepair(orderDetailId);
        if (reload != null && !reload.isEmpty()) {
            Map<Long, List<String>> uploadImage = laundryService.getRepairImage(reload);
            model.addAttribute("uploadImage", uploadImage);
        }
        model.addAttribute("reload", reload);
        model.addAttribute("repairCategory", repairCategory);

        return "project_order_2_2";
    }

    @Transactional
    @PostMapping("/dry/order")
    public @ResponseBody Map<String, Boolean> dryOrder(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId,
                                                         @SessionAttribute(name = SessionConstant.ORDERS_CONFIRM, required = false) Long ordersDetailId,
                                                         @RequestBody Map<String, Integer> result) {
        HashMap<String, Boolean> resultMap = new HashMap<>();
        boolean status = laundryService.insertDrycleaning(memberId, ordersDetailId, result, resultMap);

        System.out.println("status = " + status);

        resultMap.put("result", status);
        return resultMap;
    }

    @Transactional
    @PostMapping(value = "/repair/order")
    public @ResponseBody Map<String, Boolean> repairOrder(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId,
                                                          @SessionAttribute(name = SessionConstant.ORDERS_CONFIRM, required = false) Long ordersDetailId,
                                                          @RequestPart("repairData") Map<String, RepairFormData> repairData,
                                                          @RequestParam List<MultipartFile> files) {
        HashMap<String, Boolean> resultMap = new HashMap<>();

        boolean status = laundryService.insertRepair(memberId, ordersDetailId, resultMap, repairData, files);


        log.info("repairData = {}", repairData);
        log.info("files = {}", files);

        return resultMap;
    }
}
