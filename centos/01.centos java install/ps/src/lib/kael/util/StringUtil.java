package lib.kael.util;

public class StringUtil {

	public static String joinArrayToString(String[] strs, String split){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<strs.length;i++){
			sb.append(strs[i]);
			if(i!=strs.length-1)sb.append(split);
		}
		return sb.toString();
		
	}
	public static String joinArrayToString(int[] ids, String split){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<ids.length;i++){
			sb.append(ids[i]);
			if(i!=ids.length-1)sb.append(split);
		}
		return sb.toString();
	}
	public static int[] splitIdArray(String ids, String split){
		String[] temp = ids.split(split);
		int[] result = new int[temp.length];
		for(int i=0;i<temp.length;i++){
			result[i] = new Integer(temp[i]);
		}
		return result;
	}
	public static String trimString(String str, String split){
		str = leftTrim(str, split);
		str = rightTrim(str, split);
		return str;
	}
	public static String leftTrim(String str, String split){
		try{
			while(str.indexOf(split)==0){
				str = str.substring(1);
			}
		}catch(Exception e){
			return str;
		}
		return str;
	}
	public static String rightTrim(String str, String split){
		try{
			while(str.lastIndexOf(split)==str.length()-1){
				str = str.substring(0,str.length()-1);
			}
		}catch(Exception e){
			return str;
		}
		return str;
	}
	public static String addZero(int x){
		if(x<10)return "00"+x;
		if(x<100)return "0"+x;
		return ""+x;
	}
}
