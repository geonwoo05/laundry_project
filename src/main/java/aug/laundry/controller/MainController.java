package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dao.EnumDao;
import aug.laundry.domain.Orders;
import aug.laundry.dto.MemberDto;
import aug.laundry.dto.OrdersEnum;
import aug.laundry.dto.OrdersEnum2;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.CategoryPriceCalculator;
import aug.laundry.enums.category.MemberShip;
import aug.laundry.enums.category.Pass;
import aug.laundry.enums.orderStatus.OrderStatus;
import aug.laundry.enums.repair.Repair;
import aug.laundry.service.LoginService_kgw;
import aug.laundry.service.MainService;
import aug.laundry.service.MemberService_kgw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService_kgw memberServiceKgw;
    private final MainService mainService;
    private final EnumDao enumDao;

    @GetMapping
    public String mainPage(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId, Model model) {
        if (memberId != null){
            MemberDto memberDto = memberServiceKgw.selectOne(memberId);
            List<OrdersEnum2> orders = mainService.getOrders(memberId);
            model.addAttribute("name", memberDto.getMemberName());
            model.addAttribute("orders", orders);
        }


        return "project_main";
    }

    @GetMapping("price")
    public String priceTag(Model model) {
        Map<String, Map<String, Long>> priceTag = mainService.getCategory();
        Float percent = CategoryPriceCalculator.PASS.percent();
        Repair[] repair = Repair.values();


        model.addAttribute("percent", percent);
        model.addAttribute("drycleaning", priceTag);
        model.addAttribute("repair", repair);
        return "project_price_tag";
    }
}
