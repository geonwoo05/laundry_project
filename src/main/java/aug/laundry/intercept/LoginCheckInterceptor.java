package aug.laundry.intercept;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.MemberDto;
import aug.laundry.service.LoginService_kgw;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    LoginService_kgw loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession();

        // controller에서 생성되었을 지 모를 쿠키를 꺼내온다.
        Cookie loginCookie = WebUtils.getCookie(request,"loginCookie");
//        if(loginCookie != null){
//            String sessionId = loginCookie.getValue();
//            MemberDto memberDto = loginService.checkUserWithSessionId(sessionId);
//            if(memberDto != null){
//                session.setAttribute(SessionConstant.LOGIN_MEMBER, memberDto.getMemberId());
//                return true;
//            }
//        }
        if (session == null || session.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {

            log.info("인증되지않은 사용자 요청");
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        log.info("인증된 사용자");
        return true;
    }

}