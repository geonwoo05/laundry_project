package aug.laundry.controller.api;

import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PaymentApiController {

    @Value("${import.rest-api}")
    private String restApi;

    @Value("${import.rest-api-secret}")
    private String restApiSecret;

    private final IamportClient iamportClient;

    @Autowired
    public PaymentApiController() {
        this.iamportClient = new IamportClient(restApi, restApiSecret);
    }

        public void verifyIamPort(@RequestBody Map<String, String> map){ //void 나중에 수정필요하면 수정



    }


}
