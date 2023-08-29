package aug.laundry.controller;

import aug.laundry.service.LaundryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/laundry")
public class LaundryController {

    private final LaundryService laundryService;

    @GetMapping("/order")
    public String order() {

        return "project_order_confirm";
    }
}
