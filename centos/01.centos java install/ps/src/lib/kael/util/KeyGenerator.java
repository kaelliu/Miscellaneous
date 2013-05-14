package lib.kael.util;

public class KeyGenerator
{
	public static String key_gen(String addon_info)
	{
		return MD5Util.getMD5(System.currentTimeMillis()+"_"+addon_info);
	}
}