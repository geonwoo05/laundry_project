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

}
