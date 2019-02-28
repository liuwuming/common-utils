package com.cloud.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseData {
	private int code = 200;
	private String message = "";
	private Object data;
	private String time = "";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	public static ResponseData ok(Object data) {
		return new ResponseData(data);
	}
	
	public static ResponseData fail() {
		return new ResponseData(null);
	}
	
	public static ResponseData fail(String message) {
		return new ResponseData(message);
	}
	
	public static ResponseData fail(String message, int code) {
		return new ResponseData(message, code);
	}

	public static ResponseData fail(ResponseCode responseCode) {
		return new ResponseData(responseCode.getMsg(),responseCode.getCode());
	}
	
	public static ResponseData failByParam(String message) {
		return new ResponseData(message, ResponseCode.PARAM_ERROR_CODE.getCode());
	}
	
	public ResponseData(Object data) {
		super();
		this.data = data;
		this.time = setNowTime();
	}
	
	public ResponseData(String message) {
		super();
		this.message = message;
		this.time = setNowTime();
	}
	
	public ResponseData(String message, int code) {
		super();
		this.message = message;
		this.code = code;
		this.time = setNowTime();
	}
	
	public ResponseData() {
		super();
		this.time = setNowTime();
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	private String setNowTime() {
		Date now = new Date();
		return dateFormat.format(now);
	}
}
