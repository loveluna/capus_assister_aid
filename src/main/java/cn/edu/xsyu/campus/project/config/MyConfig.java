package cn.edu.xsyu.campus.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
public class MyConfig extends WebMvcConfigurerAdapter {
    @Resource
    private LoginHandlerInterceptor loginHandlerInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
    @Bean
    public WebMvcConfigurerAdapter WebMvcConfigurerAdapter() {
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            /**添加拦截器*/
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/admin/**")
                        .addPathPatterns("/user/center")
                        .excludePathPatterns("/admin").
                        excludePathPatterns("/admin/login");
            }

            /**静态资源处理*/
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/pic/**").addResourceLocations("file:D://campus_shop/pic/");
                super.addResourceHandlers(registry);
            }
        };
        return adapter;
    }
}