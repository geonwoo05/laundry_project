package aug.laundry.service;

import aug.laundry.controller.LaundryController;
import aug.laundry.dao.LaundryRepository;
import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.MyCoupon;
import aug.laundry.dto.OrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
