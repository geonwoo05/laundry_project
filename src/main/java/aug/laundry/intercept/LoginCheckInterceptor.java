package aug.laundry.intercept;

import aug.laundry.commom.SessionConstant;
<<<<<<< Updated upstream
import aug.laundry.dto.AdminDto;
import aug.laundry.dto.MemberDto;
import aug.laundry.dto.QuickRiderDto;
import aug.laundry.dto.RiderDto;
import aug.laundry.service.LoginServiceImpl_kgw;
import aug.laundry.service.LoginService_kgw;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
=======
import aug.laundry.dao.login.LoginMapper;
import aug.laundry.service.LoginService_kgw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

>>>>>>> Stashed changes
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
<<<<<<< Updated upstream
import java.sql.Date;
=======
import java.util.Date;
>>>>>>> Stashed changes

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

<<<<<<< Updated upstream
    private final LoginService_kgw loginService;
=======
    @Autowired
    LoginService_kgw loginService;
>>>>>>> Stashed changes

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession();


        if (session == null || session.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
            Cookie loginCookie = WebUtils.getCookie(request,"loginCookie");
            if(loginCookie != null){
                String sessionId = loginCookie.getValue();
                MemberDto memberDto = loginService.checkUserWithSessionId(sessionId);
                if(memberDto != null){
                    session.setAttribute(SessionConstant.LOGIN_MEMBER, memberDto.getMemberId());
                    return true;
                }
            }else{
                log.info("인증되지않은 사용자 요청");
                response.sendRedirect("/login?redirectURL=" + requestURI);
                return false;
            }
            log.info("인증되지않은 사용자 요청");
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        log.info("인증된 사용자");
        return true;
    }

<<<<<<< Updated upstream
=======
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // loginController에서 생성한 쿠키를 조회
        HttpSession session = request.getSession(false);
        String memberId = (String)session.getAttribute("memberId");

        // Session이 존재하고 자동로그인에 체크하면 쿠키 생성
        if(memberId != null && request.getAttribute("useCookie") != null){
            Cookie loginCookie = new Cookie("loginCookie", session.getId());
            loginCookie.setPath("/");

            // 쿠키 유효시간 7일
            int amount = 60*60*24*7;
            loginCookie.setMaxAge(amount);

            // 쿠키 적용
            response.addCookie(loginCookie);

            // currentTimeMills()가 1/1000초 단위임으로 1000곱해서 더해야함
            Date limit = new Date(System.currentTimeMillis() + (1000*amount));
            // 현재 세션 id와 유효시간을 user_session 테이블에 저장한다.
            loginService.keepLogin(memberId, limit, session.getId());


        }



    }
>>>>>>> Stashed changes
}


