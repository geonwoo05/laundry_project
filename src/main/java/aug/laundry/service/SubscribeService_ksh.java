package aug.laundry.service;

import aug.laundry.dto.SubscriptionPayDto;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface SubscribeService_ksh {

    int saveSubscribe(SubscriptionPayDto subData);
    String getAccessToken();
    JsonObject postData(String requestUrl, String jsonBody) throws IOException;
    JsonObject getData(String requestUrl) throws IOException;
    int updateNextMerchantId(String merchantUid, String merchantUidRe);
    SubscriptionPayDto getScheduleInfo(Long memberId);

}
