package aug.laundry.service;

import aug.laundry.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
public interface MemberService_kgw {
    public MemberDto selectOne(Long memberId);
}
