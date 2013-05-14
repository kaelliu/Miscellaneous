package lib.kael.util;

import java.security.NoSuchAlgorithmException;

public class MD5Util
{
	public static String getMD5(String src)
	{
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		 try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(src.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {    // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i];  // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换, 
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];   // 取字节中低 4 位的数字转换
            }
            s = new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}
}