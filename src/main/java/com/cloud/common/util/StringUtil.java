package com.cloud.common.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author yzh
 * @date 2017年9月2日
 */
public class StringUtil {
	// private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	public static boolean isNumeric(String str) {

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isEmpty(String string) {

		return string == null || string.trim().equals("");
	}

	public static String trim(String string) {

		return string == null ? "" : string.trim();
	}

	public static String truncateString(String string, int length) {

		StringBuilder stringBuilder = new StringBuilder(string);
		if (string.length() <= length) {
			return string;
		}
		stringBuilder.setLength(length);
		return stringBuilder.toString();
	}

	public static String getProductDescImages(String desc) {

		StringBuilder sb = new StringBuilder("");
		if (!isEmpty(desc)) {
			String[] arr = desc.split("src=\"");
			for (String img : arr) {
				if (img.startsWith("http")) {
					sb.append(img.substring(0, img.indexOf("\""))).append(";");
				}
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			return sb.toString();
		}
		return "";
	}

	public static boolean isInStringArrayIgnoreCase(String str, String[] array) {

		if (array.length == 0 || str == null) {
			return false;
		}
		for (String string : array) {
			if (str.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 编码导出文件名 * ie采用URLEncoder编码输出中文 * opera采用filename * safari采用iso-8859-1 *
	 * chrome采用base64或iso-8859-1 * firefox采用base64或iso-8859-1
	 * @param fileName
	 * @param userAgent
	 * @return
	 */
	public static String encodeFileName(String fileName, String userAgent) {

		try {
			if (userAgent != null && fileName != null) {
				userAgent = userAgent.toLowerCase();
				if (userAgent.indexOf("firefox") != -1) {
					fileName = new String(fileName.getBytes(), "iso8859-1");
				} else {
					fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
				}
			}
			return fileName;
		} catch (Exception e) {
			return fileName;
		}
	}

	public static String getDecimalFormat(BigDecimal dec) {

		DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00");
		return fmt.format(dec);
	}

	/**
	 * in查询语句构建
	 * @author yzh <br>
	 * @Date 2016年7月22日<br>
	 * @param size
	 * @return
	 */
	public static String sqlInAppend(int size) {

		if (size == 1) {
			return "=?";
		}
		StringBuffer sqlIn = new StringBuffer(30);
		sqlIn.append(" in (");
		for (int i = 0; i < size; i++) {
			if (i == size - 1) {
				sqlIn.append("?)");
			} else {
				sqlIn.append("?,");
			}
		}
		return sqlIn.toString();
	}

	/**
	 * in查询语句构建
	 * @author yzh <br>
	 * @Date 2016年7月22日<br>
	 * @param size
	 * @return
	 */
	public static String sqlNotInAppend(int size) {

		if (size == 1) {
			return "<>?";
		}
		StringBuffer sqlIn = new StringBuffer(30);
		sqlIn.append(" not in (");
		for (int i = 0; i < size; i++) {
			if (i == size - 1) {
				sqlIn.append("?)");
			} else {
				sqlIn.append("?,");
			}
		}
		return sqlIn.toString();
	}

	public static String leftPad(String str, String p, int length) {

		if (str.length() >= length) {
			return str;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = str.length(); i < length; i++) {
			stringBuilder.append(p);
		}
		stringBuilder.append(str);
		return stringBuilder.toString();
	}

	public static String rightPad(String str, String p, int length) {

		if (str.length() >= length) {
			return str;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(str);
		for (int i = str.length(); i < length; i++) {
			stringBuilder.append(p);
		}
		return stringBuilder.toString();
	}


	/**
	 * 关联字符串，保留null连接
	 */
	@SafeVarargs
	public static <T> String join2(String separator, T... ss) {

		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < ss.length; i++) {
			T t = ss[i];
			String s = "";
			if (t != null) {
				s = t.toString();
			}
			if (i != 0) {
				sb.append(separator);
			}
			sb.append(s);
		}
		return sb.toString();
	}

	public static boolean contains(String[] keys, String key) {

		boolean exist = false;
		for (String k : keys) {
			if (k.equals(key)) {
				exist = true;
				break;
			}
		}
		return exist;
	}

	/**
	 * 统计一个字符串中某个字符/字符串的数量
	 * @description
	 * @param origin 原始的字符串
	 * @param find 待检索的字符串
	 * @return int
	 * @since 2.11.1
	 * @Author DaiZM
	 * @Date 2017/11/23
	 */
	public static int getOccur(String origin, String find) {

		int count = 0;
		int index = -1;
		while ((index = origin.indexOf(find, index)) > -1) {
			++index;
			++count;
		}
		return count;
	}

	/*
	 * 分割字符串
	 */
	public static <T> List<T> split(Class<T> cls, String str, String sepretor) {

		String[] vs = str.split(sepretor);
		String typeStr = cls.getName();
		List<T> rs = new ArrayList<T>();
		for (String v : vs) {
			rs.add((T) convert(typeStr, v));
		}
		return rs;
	}

	public static Object convert(String typeStr, Object value) {

		if (value == null) {
			return null;
		}
		String v = value.toString().trim();
		if (StringUtils.isEmpty(v)) {
			return null;
		}
		Object r = null;
		if (typeStr.equals("int") || typeStr.contains(".Integer")) {
			// 字段为int
			r = Integer.parseInt(v);
		} else if (typeStr.equals("long") || typeStr.contains(".Long")) {
			// 字段为int
			// 1.50963028E12
			if (v.indexOf("E") > 0) {
				// 科学计数法处理
				v = new BigDecimal(v).toPlainString();
			}
			r = Long.parseLong(v);
		} else if (typeStr.equals("float") || typeStr.contains(".Float")) {
			// 字段为float
			r = Float.parseFloat(v);
		} else if (typeStr.equals("double") || typeStr.contains(".Double")) {
			// 字段为float
			r = Double.parseDouble(v);
		} else if (typeStr.equals("boolean") || typeStr.contains(".Boolean")) {
			// 字段为float
			r = Boolean.parseBoolean(v);
		} else if (typeStr.contains(".Timestamp")) {
			// {date=7, hours=18, seconds=51, month=4, nanos=411000000, timezoneOffset=-480,
			// year=118, minutes=10, time=1525687851411, day=1}
			JSONObject jt = (JSONObject) value;
			Timestamp t = new Timestamp(jt.getLong("time"));
			r = t;
		} else {
			// 字段为String
			r = v;
		}
		return r;
	}
}
