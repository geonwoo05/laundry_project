package aug.laundry.controller;

import aug.laundry.domain.CouponList;
import aug.laundry.service.LaundryService;
import aug.laundry.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ride/*")
public class RiderController {

    @GetMapping("wait")
    public String waitList(Model model) {
        return "project_rider_list_on_call";
    }

    @GetMapping("accept")
    public String acceptList(Model model) {
        return "project_rider_using_list";
    }

    @GetMapping("finish")
    public String finishList(Model model) {
        return "project_rider_complete";
    }
}
