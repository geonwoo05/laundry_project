package aug.laundry;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        //로그인 체크 인터셉터
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(1)
//                .addPathPatterns("")
//                .excludePathPatterns("");
    }





}