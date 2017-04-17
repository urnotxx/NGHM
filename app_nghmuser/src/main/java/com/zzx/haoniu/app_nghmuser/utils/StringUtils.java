package com.zzx.haoniu.app_nghmuser.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具集
 * 
 * @autor:Bin
 * @version:1.0
 * @created:2015-4-29 上午8:36:38
 **/
public class StringUtils {
	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符串不为 null 而且不为 "" 时返回 true
	 */
	public static boolean notBlank(String str) {
		return str == null || "".equals(str.trim()) ? false : true;
	}

	/**
	 * 判断是否为空，填充数据
	 * 
	 * @param input
	 * @return boolean
	 */
	public static String fillEmpty(String input, String valuse) {
		if (input == null || "".equals(input))
			return valuse;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return input;
			}
		}
		return valuse;
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 正则表达式判断是否是手机号 是返回true，否则：false
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean checkPwd(String pwd){
		Pattern p = Pattern.compile(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	/**
	 * 获得单个汉字的Ascii.
	 * 
	 * @param cn
	 *            char 汉字字符
	 * @return int 错误返回 0,否则返回ascii
	 */
	public static int getCnAscii(char cn) {
		try {
			byte[] bytes = (String.valueOf(cn)).getBytes("gb2312");
			System.out.println(cn);
			if (bytes == null || bytes.length > 2 || bytes.length <= 0) { // 错误
				return 0;
			}
			if (bytes.length == 1) { // 英文字符
				return bytes[0];
			}
			if (bytes.length == 2) { // 中文字符
				int hightByte = 256 + bytes[0];
				int lowByte = 256 + bytes[1];
				int ascii = (256 * hightByte + lowByte) - 256 * 256;
				return ascii;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0; // 错误
	}

	private final static int[] li_SecPosValue = { 1601, 1637, 1833, 2078, 2274,
			2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
			4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590 };
	private final static String[] lc_FirstLetter = { "a", "b", "c", "d", "e",
			"f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "w", "x", "y", "z" };

	/**
	 * 取得给定汉字串的首字母串,即声母串
	 * 
	 * @param str
	 *            给定汉字串
	 * @return 声母串
	 */
	public static String getAllFirstLetter(String str) {
		if (str == null || str.trim().length() == 0) {
			return "";
		}

		String _str = "";
		for (int i = 0; i < str.length(); i++) {
			_str = _str + getFirstLetter(str.substring(i, i + 1));
		}

		return _str;
	}

	/**
	 * 取得给定汉字的首字母,即声母
	 * 
	 * @param chinese
	 *            给定的汉字
	 * @return 给定汉字的声母
	 */
	public static String getFirstLetter(String chinese) {
		if (chinese == null || chinese.trim().length() == 0) {
			return "";
		}
		chinese = conversionStr(chinese, "GB2312", "ISO8859-1");

		if (chinese.length() > 1) // 判断是不是汉字
		{
			int li_SectorCode = (int) chinese.charAt(0); // 汉字区码
			int li_PositionCode = (int) chinese.charAt(1); // 汉字位码
			li_SectorCode = li_SectorCode - 160;
			li_PositionCode = li_PositionCode - 160;
			int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
			if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
				for (int i = 0; i < 23; i++) {
					if (li_SecPosCode >= li_SecPosValue[i]
							&& li_SecPosCode < li_SecPosValue[i + 1]) {
						chinese = lc_FirstLetter[i];
						break;
					}
				}
			} else // 非汉字字符,如图形符号或ASCII码
			{
				chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
				chinese = chinese.substring(0, 1);
			}
		}

		return chinese;
	}

	/**
	 * 字符串编码转换
	 * 
	 * @param str
	 *            要转换编码的字符串
	 * @param charsetName
	 *            原来的编码
	 * @param toCharsetName
	 *            转换后的编码
	 * @return 经过编码转换后的字符串
	 */
	private static String conversionStr(String str, String charsetName,
			String toCharsetName) {
		try {
			str = new String(str.getBytes(charsetName), toCharsetName);
		} catch (UnsupportedEncodingException ex) {
			System.out.println("字符串编码转换异常：" + ex.getMessage());
		}

		return str;
	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String firstCharToUpper(String str) {
		if (Character.isLowerCase(str.charAt(0))) {
			char[] array = str.toCharArray();
			array[0] -= 32;
			return String.valueOf(array);
		}
		return str;
	}

	/**
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String firstCharToLower(String str) {
		if (Character.isUpperCase(str.charAt(0))) {
			char[] array = str.toCharArray();
			array[0] += 32;
			return String.valueOf(array);
		}
		return str;
	}
}
