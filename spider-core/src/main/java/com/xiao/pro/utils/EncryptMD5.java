/**
 *
 */
package com.xiao.pro.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * MD5加密工具。
 * @author xiaoliang
 * @version 4.1.0
 * @since 4.0.0 2014-11-7
 */
public final class EncryptMD5 {
	private final static String CHARACTER_SET = "UTF-8";

    public enum Md5Mode{
        MD5_LOWERCASE,
//        MD5_LOWERCASE_16,
//        MD5_CAPITAL_16,
        MD5_CAPITAL
        }

	/**
	 * 使用UTF-8字符集对源串进行加密。
	 * @param s
	 * @return md5加密后的数据
     * @see #md5(String, String)
	 */
	public final static String md5(String s) {
		return EncryptMD5.md5(s, CHARACTER_SET);
	}

	/**
	 *
	 * 使用指定字符集对源串进行加密。
	 * @param
	 * @return md5加密后的数据
	 */
	public final static String md5(String s, String characterSet){
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f'};
		byte[] strTemp;
		MessageDigest mdTemp = null;
		try {
			strTemp = s.getBytes(characterSet);
			mdTemp = MessageDigest.getInstance("MD5");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		mdTemp.update(strTemp);
		byte[] md = mdTemp.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

    /**
     * @param s
     * @param characterSet
     * @param mode
     * @return
     */
    public final static String md5(String s, String characterSet, Md5Mode mode){
        String md5 = md5(s,characterSet);
        if(Md5Mode.MD5_CAPITAL.equals(mode)){
            return md5.toUpperCase();
        }else {
            return md5.toLowerCase();
        }
    }

    public final static String md5(String s, Md5Mode mode){
        String md5 = md5(s,CHARACTER_SET);
        if(Md5Mode.MD5_CAPITAL.equals(mode)){
            return md5.toUpperCase();
        }else {
            return md5.toLowerCase();
        }
    }

}
