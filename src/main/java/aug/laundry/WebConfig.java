package aug.laundry;

import aug.laundry.intercept.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        로그인 체크 인터셉터
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/laundry/**")
                .excludePathPatterns("/css/**", "/images/**", "/js/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 이미지
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/"+fileDir);
    }

}