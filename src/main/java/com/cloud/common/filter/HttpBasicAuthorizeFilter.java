package com.zfec.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.zfec.common.util.JWTUtils;
import com.zfec.common.util.JsonUtils;
import com.zfec.common.util.ResponseCode;
import com.zfec.common.util.ResponseData;
import com.zfec.common.util.StringUtil;

@WebFilter(filterName = "HttpBasicAuthorizeFilter", urlPatterns = { "/*" })
@Order(FilterRegistrationBean.LOWEST_PRECEDENCE)
public class HttpBasicAuthorizeFilter implements Filter {
	private static Logger log = LoggerFactory.getLogger(HttpBasicAuthorizeFilter.class);
	JWTUtils jwtUtils = JWTUtils.getInstance();
	ServletContext context = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		context = filterConfig.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("text/html;charset=UTF-8");
		String token = httpRequest.getHeader("token");
		// 健康检查控制
		String uri = httpRequest.getRequestURI();

		log.debug("----------------------------------------------------> {}", token);
		if (uri.equalsIgnoreCase("/getNoAuthSmsVerify.do") || uri.equalsIgnoreCase("/me/login.do")) {
			chain.doFilter(httpRequest, response);
		} else {
			// 验证TOKEN
			if (null == token || "".equals(token.trim())) {
				log.info("----------------------------------------------------> {}---no token", uri);
				
//				PrintWriter print = httpResponse.getWriter();
//				print.write(JsonUtils
//						.toJson(ResponseData.fail("非法请求【缺少Authorization信息】", ResponseCode.NO_AUTH_CODE.getCode())));
				
				String errStr = JsonUtils.toJson(ResponseData.fail("PATH: " + uri + " 非法请求【缺少Authorization信息】", ResponseCode.NO_AUTH_CODE.getCode()));
				try {
					throw new RuntimeException(errStr);
				} catch (RuntimeException e) {
					log.error(errStr);
//					e.printStackTrace();
				}

				return;
			}
			String id = httpRequest.getParameter("id");
			String orgId=httpRequest.getParameter("orgId");
			if(StringUtil.isEmpty(id) || StringUtil.isEmpty(orgId)) {	
				log.warn("id or orgId is null");
			String paramEerrStr = JsonUtils.toJson(ResponseData.fail("PATH: " + uri + " 非法请求【缺少参数】", ResponseCode.PARAM_ERROR_CODE.getCode()));             
			try {
				throw new RuntimeException(paramEerrStr);
			} catch (RuntimeException e) {
				log.error(paramEerrStr);
			}
			return;
		}	
			
			JWTUtils.JWTResult jwt = jwtUtils.checkToken(token,id);
			if (!jwt.isStatus()) {
				log.info("----------------------------------------------------> {}---check token failed", uri);
				
//				PrintWriter print = httpResponse.getWriter();
//				print.write(JsonUtils.toJson(ResponseData.fail(jwt.getMsg(), jwt.getCode())));
				
				String errStr = JsonUtils.toJson("PATH: " + uri + " " + ResponseData.fail(jwt.getMsg(), jwt.getCode()));
				try {
					throw new RuntimeException(errStr);
				} catch (RuntimeException e) {
					log.error(errStr);
//					e.printStackTrace();
				}
				
				return;
			}
			
			chain.doFilter(httpRequest, response);
		}
	}

	public void destroy() {

	}

}
