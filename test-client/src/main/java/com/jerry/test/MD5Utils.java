package com.jerry.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MD5共通类
 * 
 * <p>此类是Bruce整合其它Server中的Util而成</p>
 * 
 * @author Bruce
 *
 */
public class MD5Utils {
	private static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 得到字符串的MD5形式
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private static String md5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] b = data.getBytes("UTF8");
		md5.update(b, 0, b.length);
		return byteArrayToHexString(md5.digest());
	}
	/**
	 * 得到字符串的MD5形式
	 * @param data byte[]
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5(byte[] data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
//		byte[] b = data.getBytes("UTF8");
		md5.update(data, 0, data.length);
		return byteArrayToHexString(md5.digest());
	}
    /**
     * 字节数组转化为十六进制字符串
     * @param b 字节数组
     * @return
     */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
			sb.append(byteToHexString(b[i]));
		}
		return sb.toString();
	}

    /**
     * 字节数据转化为十六进制字符串
     * @param b 字节
     * @return
     */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 得到消息的MD5形式
	 * @param msg
	 * @return String MD5
	 */
	public static String genMd5(String msg) {
		String messageDigest = null;
		try {
			messageDigest = md5(msg);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Md5 Error. Cause: " + e);
		}

		return messageDigest;
	}

	/**
	 * 将Map转换为Md5字符串
	 * <p>
	 * 处理过程：<br>
	 * 1. 数据转换为List并排序<br>
	 * 2.List转换为字符串，各项之间用【:】分隔开来<br>
	 * 3.将字符串转换为Md5字符串<br>
	 * </p>
	 * @param map 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String genB2bMd5(Map<String, String> map) {
		List md5List = new ArrayList();
		String md5Str = "";
		String separation = ":";

		for (Map.Entry entry : map.entrySet()) {
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (("transactionType".equalsIgnoreCase(name)) || ("checksum".equalsIgnoreCase(name)))
				continue;
			if (("multiplierFlag".equalsIgnoreCase(name)) && (!("1".equalsIgnoreCase(value)))) {
				continue;
			}
			md5List.add(name + "=" + value);
		}

		Object[] md5Array = md5List.toArray();
		Arrays.sort(md5Array);
		for (int i = 0; i < md5Array.length; ++i) {
			md5Str = md5Str + md5Array[i];
			if (i != md5Array.length - 1) {
				md5Str = md5Str + separation;
			}
		}
		return genMd5(md5Str);
	}

	public static void main(String[] args) {
		System.out.println(genMd5("12345"));
	}
}