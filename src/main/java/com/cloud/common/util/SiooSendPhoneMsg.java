package com.zfec.common.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zfec.common.constants.SystemConstant;

/**
 * Created by zhangkai on 2018/5/31.
 */
public class SiooSendPhoneMsg {

	private static Logger logger = LoggerFactory.getLogger(SiooSendPhoneMsg.class);
	private static SiooSendPhoneMsg sendPhoneMsg = null;

	public static SiooSendPhoneMsg getInstall() {

		if (null == sendPhoneMsg) {
			synchronized (SiooSendPhoneMsg.class) {
				if (sendPhoneMsg == null) {
					sendPhoneMsg = new SiooSendPhoneMsg();
				}
			}
		}
		return sendPhoneMsg;
	}

	private static String SIOO_URL = "http://sms.10690221.com/hy/";
	private static String SIOO_UID = "81320";
	private static String SIOO_AUTH = new MD5().getMD5ofStr("zfds1TxTt!K");

	public static boolean sendMsg(String mobile, String content) {

		HttpClient httpClient = new HttpClient();
		if (StringUtil.isEmpty(mobile)) {
			logger.warn("mobile is null");
			return false;
		}
		if (StringUtil.isEmpty(content)) {
			logger.warn("content is null");
			return false;
		}
		logger.info("mobile={}，content={}", mobile, content);
		if (!SystemConstant.SMS_SEND_FLAG) {
			logger.warn("测试环境，不发送真实短信，返回boolean为true");
			return true;
		}
		try {
			String msgContent = java.net.URLEncoder.encode(content.trim(), "gbk");
			PostMethod postMethod = new PostMethod(SIOO_URL);
			NameValuePair[] data = { new NameValuePair("uid", SIOO_UID), new NameValuePair("auth", SIOO_AUTH),
					new NameValuePair("mobile", StringUtil.join(",", mobile.trim())), new NameValuePair("expid", "0"),
					new NameValuePair("msg", msgContent) };
			postMethod.setRequestBody(data);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				String sms = postMethod.getResponseBodyAsString();
				logger.info("send failed，result={}", sms);
				return false;
			}
			logger.info("send return statusCode={}", statusCode);
			return true;
		} catch (Exception e) {
			logger.error("failed={}", e);
			return false;
		}
	}
}
