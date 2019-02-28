package com.cloud.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean filterRegistBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        HttpBasicAuthorizeFilter httpBasicFilter = new HttpBasicAuthorizeFilter();
        registrationBean.setFilter(httpBasicFilter);
        List<String> urlPatterns = new ArrayList<String>(1);
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        //过滤器顺序
        registrationBean.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
