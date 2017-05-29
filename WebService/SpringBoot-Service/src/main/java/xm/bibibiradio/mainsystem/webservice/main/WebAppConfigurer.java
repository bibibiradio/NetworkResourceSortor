package xm.bibibiradio.mainsystem.webservice.main;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import xm.bibibiradio.mainsystem.webservice.security.AuthInterceptor;
import xm.bibibiradio.mainsystem.webservice.security.CtokenInterceptor;
import xm.bibibiradio.mainsystem.webservice.session.SessionInterceptor;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        //registry.addInterceptor(new MyInterceptor1()).addPathPatterns("/**");
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
        //registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
        //registry.addInterceptor(new CtokenInterceptor()).addPathPatterns("/api/**");
        super.addInterceptors(registry);
    }
}
