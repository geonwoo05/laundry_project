package aug.laundry.intercept;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.MemberDto;
import aug.laundry.service.LoginService_kgw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class PathInterceptor implements HandlerInterceptor {

    private final LoginService_kgw loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();
        Cookie loginCookie = WebUtils.getCookie(request,"loginCookie");
        if (loginCookie != null) {
            String sessionId = loginCookie.getValue();
            MemberDto memberDto = loginService.checkUserWithSessionId(sessionId);
            if(memberDto != null){

                log.info("자동로그인 회원입니다.");
                session.setAttribute(SessionConstant.LOGIN_MEMBER, memberDto.getMemberId());
                StringBuilder sb = new StringBuilder(requestURI);
                sb.insert(sb.indexOf("//")+1, session.getAttribute(SessionConstant.LOGIN_MEMBER));
                requestURI = sb.toString();
                //log.info("경로 변경 = {}", sb.toString());
                // 응답 커밋을 방지하기 위해 다음 두 줄을 추가
                response.reset(); // 응답 리셋
                response.sendRedirect(requestURI);
                return false;
            }
        }
         log.info("비정상적인 경로입니다. = {}", requestURI);
//        response.sendRedirect("/login");
        return false;



    }
}
