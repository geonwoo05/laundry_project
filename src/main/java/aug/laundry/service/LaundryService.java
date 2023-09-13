package aug.laundry.service;

import aug.laundry.dto.*;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.MemberShip;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LaundryService {

    OrderInfo firstInfo(Long memberId);

    List<MyCoupon> getCoupon(Long memberId);

    Address getAddress(Long memberId);

    List<Category> getDry(Long memberId);

    List<Category> getRepair(Long memberId);

    MemberShip isPass(Long memberId);

    void update(Long memberId, Long couponListId, OrderPost orderPost);

    void check(Long memberId, HttpSession session);

    boolean insertDrycleaning(Long memberId, Long ordersDetailId, Map<String, Integer> result, HashMap<String, Boolean> resultMap);

    List<OrderDrycleaning> reloadDrycleaning(Long orderDetailId);

    List<OrderRepair> reloadRepair(Long orderDetailId);

    Map<Long, List<String>> getRepairImage(List<OrderRepair> reload);

    boolean insertRepair(Long memberId, Long ordersDetailId, HashMap<String, Boolean> resultMap, Map<String, RepairFormData> repairData, List<MultipartFile> files);

}
