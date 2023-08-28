package aug.laundry.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HtmlConnectTestController {

    @GetMapping("/test/1")
    public String http1() {
        return "project_login";
    }

    @GetMapping("/test/2")
    public String http2() {
        return "project_change_password";
    }

    @GetMapping("/test/3")
    public String http3() {
        return "project_coupon";
    }

    @GetMapping("/test/4")
    public String http4() {
        return "project_invite";
    }

    @GetMapping("/test/5")
    public String http5() {
        return "project_register";
    }

    @GetMapping("/test/6")
    public String http6() {
        return "project_search_id";
    }

    @GetMapping("/test/7")
    public String http7() {
        return "project_search_id_result";
    }

    @GetMapping("/test/8")
    public String http8() {
        return "project_search_password";
    }

    @GetMapping("/test/9")
    public String http9() {
        return "project_order_confirm";
    }

    @GetMapping("/test/10")
    public String http10() {
        return "project_update_address";
    }

    @GetMapping("/test/11")
    public String http11() {
        return "project_update_phone";
    }

    @GetMapping("/test/12")
    public String http12() {
        return "project_order_1";
    }

    @GetMapping("/test/13")
    public String http13() {
        return "project_order_2_1";
    }
    @GetMapping("/test/14")
    public String http14() {
        return "project_order_2_2";
    }
    @GetMapping("/test/15")
    public String http15() {
        return "project_point";
    }
    @GetMapping("/test/16")
    public String http16() {
        return "project_use_point";
    }
    @GetMapping("/test/17")
    public String http17() {
        return "project_use_coupon";
    }
    @GetMapping("/test/18")
    public String http18() {
        return "project_order_view";
    }

    @GetMapping("/test/19")
    public String http19() {
        return "project_pickup_location";
    }
}
