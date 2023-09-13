package aug.laundry;

import aug.laundry.intercept.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
        multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 파일당 업로드 크기 제한 (5MB)
        return multipartResolver;
    }

}