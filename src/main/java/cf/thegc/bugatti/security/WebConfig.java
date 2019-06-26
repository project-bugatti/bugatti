package cf.thegc.bugatti.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterFilterRegistrationBean() {
        FilterRegistrationBean<JWTFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new JWTFilter());
        filterFilterRegistrationBean.addUrlPatterns("/api/v1/*");
        return filterFilterRegistrationBean;
    }
}
