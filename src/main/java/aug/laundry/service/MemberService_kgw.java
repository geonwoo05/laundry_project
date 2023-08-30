package aug.laundry.service;

import aug.laundry.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Service
public interface MemberService_kgw {
    public MemberDto selectOne(Long memberId);

    public void naverLogin(HttpServletRequest request, Model model);




}
