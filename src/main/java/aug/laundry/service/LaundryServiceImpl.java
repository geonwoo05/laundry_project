package aug.laundry.service;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dao.LaundryRepository;
import aug.laundry.domain.Orders;
import aug.laundry.dto.*;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.Delivery;
import aug.laundry.enums.category.MemberShip;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaundryServiceImpl implements LaundryService{

    private final LaundryRepository laundryRepository;

    @Override
    public OrderInfo firstInfo(Long memberId) {
        return laundryRepository.firstInfo(memberId);
    }

    @Override
    public List<MyCoupon> getCoupon(Long memberId) {
        return laundryRepository.getCoupon(memberId);
    }

    @Override
    public Address getAddress(Long memberId) {
        return laundryRepository.getAddress(memberId);
    }

    @Override
    public List<Category> getDry(Long memberId) {
        return laundryRepository.getDry(memberId);
    }

    @Override
    public List<Category> getRepair(Long memberId) {
        return laundryRepository.getRepair(memberId);
    }

    @Override
    public MemberShip isPass(Long memberId) {
        return new MemberShip(laundryRepository.isPass(memberId));
    }

    @Transactional
    @Override
    public void update(Long memberId, Long couponListId, OrderPost orderPost) {
        boolean validCoupon = laundryRepository.validCoupon(memberId, couponListId); // 쿠폰 유효성 검사
        Long expectedPrice = 0L;
        if (validCoupon) {
            expectedPrice -= laundryRepository.getCouponDiscount(memberId, couponListId);
            laundryRepository.useCoupon(memberId, couponListId); // 쿠폰 업데이트
        }
        System.out.println("orderPost = " + orderPost);
        Orders orders = getOrders(memberId, orderPost);


        MemberShip memberShip = new MemberShip(laundryRepository.isPass(memberId));
        OrderInfo orderInfo = laundryRepository.firstInfo(memberId); // 빠른세탁, 드라이클리닝, 생활빨래, 수선

        // 빠른배송 or 일반배송
        expectedPrice += orderInfo.getIsQuick() != null && orderInfo.getIsQuick() != 0 ? Delivery.QUICK_DELIVERY.getPrice() : Delivery.COMMON_DELIVERY.getPrice();
        if (orderInfo.getIsCommon() != 0) expectedPrice += memberShip.apply(Category.BASIC.getPrice());
        if (orderInfo.getIsDry() != 0) expectedPrice += memberShip.apply(laundryRepository.getDry(memberId).stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get());
        if (orderInfo.getIsRepair() != 0) expectedPrice += memberShip.apply(laundryRepository.getRepair(memberId).stream().map(x -> x.getPrice()).reduce((a,b) -> a + b).get());


        orders.setOrdersExpectedPrice(Math.round(expectedPrice / 100) * 100);
        orders.setOrdersStatus(2);
        System.out.println("최종 orders = " + orders);
        Integer result = laundryRepository.insert(orders);
        System.out.println("성공!");
    }

    @Transactional
    @Override
    public void check(Long memberId, HttpSession session) {
        Long orders_detail_id =  laundryRepository.check(memberId, null);
        if (orders_detail_id != null && orders_detail_id != 0){ // 장바구니가 존재한다면
            laundryRepository.removeOrdersDetail(orders_detail_id.longValue()); // 삭제
            log.info("removeAll OrdersDetail");
        }
        laundryRepository.createOrdersDetail(memberId); // 장바구니 생성
        log.info("Create OrdersDetail");
        orders_detail_id =  laundryRepository.check(memberId, null);
        session.setAttribute(SessionConstant.ORDERS_CONFIRM, orders_detail_id); // session 발급 (경로 우회로 다음페이지로 이동하는것을 막기 위해 session 사용)
        log.info("Create Session (ORDERS_DETAIL_ID) = {}", orders_detail_id);
        log.info("ORDERS_CONFIRM Session = {}", session.getAttribute(SessionConstant.ORDERS_CONFIRM));
    }

    @Override
    public boolean insertDrycleaning(Long memberId, Long ordersDetailId, Map<String, Integer> result, HashMap<String, Boolean> resultMap) {
        Long check = laundryRepository.check(memberId, ordersDetailId);
        if (check == null || check == 0L) return false;

        for (String category : result.keySet()) {
            if (Category.findByTitle(category).isEmpty()) return false; // 해당하는 카테고리가 없으면 false 반환
        }
        laundryRepository.removeDryCleaning(ordersDetailId); // 기존에 존재하던 드라이클리닝 장바구니 삭제

        if (result.isEmpty()){ // 기존에 있던 드라이클리닝 목록을 다 지우고 빈 장바구니일경우 resultMap에 empty값 추가 후 true 반환
            resultMap.put("empty", true);
            return true;
        }

        for (String category : result.keySet()) {
            Category category1 = Category.findByTitle(category).get();
            int amount = result.get(category1.getTitle());
            for (int i=0;i<amount;i++){
                laundryRepository.insertDryCleaning(ordersDetailId, category1);
                log.info("Category = {}, amount = {}", category1, amount);
            }
        }
        return true;
    }

    @Override
    public List<OrderDrycleaning> reloadDrycleaning(Long orderDetailId) {
        return laundryRepository.reloadDrycleaning(orderDetailId);
    }

    @Override
    public List<OrderRepair> reloadRepair(Long orderDetailId) {
        return laundryRepository.reloadRepair(orderDetailId);
    }

    @Override
    public Map<Long, List<String>> getRepairImage(List<OrderRepair> reload) {

        Map<Long, List<String>> result = new ConcurrentHashMap<>();
        for (OrderRepair orderRepair : reload) {
            List<String> storeImageName = laundryRepository.getRepairImage(orderRepair.getRepairId());
            result.put(orderRepair.getRepairId(), storeImageName);
        }
        return result;
    }

    @NotNull
    private Orders getOrders(Long memberId, OrderPost orderPost) {
        Orders orders = new Orders();
        orders.setMemberId(memberId);
        orders.setOrdersAddress(orderPost.getAddress());
        orders.setOrdersAddressDetails(orderPost.getAddressDetails());
        orders.setOrdersPickup(orderPost.getLocation());
        orders.setOrdersPickupDate(getDateString(orderPost.getTakeDate()));
        orders.setOrdersReturnDate(getDateString(orderPost.getDeliveryDate()));
        orders.setOrdersInfo(orderPost.getPassword() == null || orderPost.getPassword().equals("") ? null : orderPost.getPassword());
        orders.setOrdersRequest(orderPost.getRequest());
        return orders;
    }

    public String getDateString(LocalDateTime dateTime) {
        String date = dateTime.toString();
        return date.substring(0, date.indexOf(":") + 3).replaceAll("T", " ");
    }


}
