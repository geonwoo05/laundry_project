package aug.laundry.service;

import aug.laundry.dto.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

@Service
public interface LoginService_kgw {

    public void naverLogin(HttpServletRequest request, Model model);

    public void kakaoLogin(String code);

    public int registerSocialUser(MemberDto memberDto);

    public int registerSocialNumber(String id);

}
