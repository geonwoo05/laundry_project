package aug.laundry.service;

import aug.laundry.dao.LaundryRepository;
import aug.laundry.domain.CouponList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaundryServiceImpl implements LaundryService{

    private final LaundryRepository laundryRepository;

    @Override
    public Map<String, Integer> firstInfo(Long memberId) {
        return laundryRepository.firstInfo(memberId);
    }

    @Override
    public List<CouponList> getCoupon(Long memberId) {
        return laundryRepository.getCoupon(memberId);
    }
}
