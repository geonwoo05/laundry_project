package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.PaymentCheckRequestDto;
import aug.laundry.service.OrdersService_kdh;
import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;
import retrofit2.http.Path;

@Controller
public class PaymentController {


    @Value("${import.rest-api}")
    private String restApi;

    @Value("${import.rest-api-secret}")
    private String restApiSecret;

    private IamportClient iamportClient;

    @Autowired
    public PaymentController() {
        this.iamportClient = new IamportClient(restApi, restApiSecret);
    }

    @GetMapping("/orders/{ordersId}/payment/complete")
    public String complete(@PathVariable Long ordersId, @ModelAttribute PaymentCheckRequestDto payment,
                           @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false)Long memberId) {



        return "project_search_id_result";
    }
}
