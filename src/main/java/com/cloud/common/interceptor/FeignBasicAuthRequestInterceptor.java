package com.zfec.common.interceptor;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.zfec.common.util.JWTUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign请求拦截器
 **/
@Configuration
public class FeignBasicAuthRequestInterceptor  implements RequestInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(FeignBasicAuthRequestInterceptor.class);
    public FeignBasicAuthRequestInterceptor() {

    }

    public void apply(RequestTemplate template) {
    	String uuid = UUID.randomUUID().toString();
    	JWTUtils jwtUtils = JWTUtils.getInstance();
    	
    	//token有效时间是10min
    	String token = jwtUtils.getToken(uuid, 10);
    	template.header("token", token);
    }
}
