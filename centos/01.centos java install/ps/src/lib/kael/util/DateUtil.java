package lib.kael.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	//时间格式
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Calendar cld1 = null;		//时间1
	private static Calendar cld2 = null;		//时间2
	
	/**
	 * 比较两个值是大小
	 * @param time1	如：2012-3-2 15:40:52
	 * @param time2	如：2012-3-2 15:40:52
	 * @return 0 ：相等; 1：time1 > time2; -1: time1 < time2
	 */
	public static int compare(String time1, String time2)
	{
		cld1 = Calendar.getInstance();		//时间1
		cld2 = Calendar.getInstance();		//时间2
		try  
		{   
			cld1.setTime(df.parse(time1));   
			cld2.setTime(df.parse(time2));   
		}catch(java.text.ParseException e){   
			System.err.println("格式不正确");
			e.printStackTrace();
		}   
		int result=time1.compareTo(time2);   
		if(result==0)   return 0;
		else if(result<0)   return -1;
		else	return 1;  
	}
	
	/**
	 * 将时间转换成date
	 * @param time
	 * @return
	 */
	public static Date getDate(String time){
		cld1 = Calendar.getInstance();		//时间1
		try {
			cld1.setTime(df.parse(time));
		} catch (ParseException e) {
			System.err.println("格式不正确");
			e.printStackTrace();
		}
		return cld1.getTime();
	}
	
	/**
	 * 返回系统年份
	 * @return 1~12
	 */
	public static int getYear(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.YEAR);
	}
	
	/**
	 * 系统月份
	 * @return 1~12
	 */
	public static int getMonth(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.MONTH)+1;
	}
	
	/**
	 * 系统日期
	 * @return 1~31
	 */
	public static int getDate(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.DATE);
	}
	
	/**
	 * 星期几
	 * @return 1~7
	 */
	public static int getDayOfWeek(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 当前小时
	 * @return
	 */
	public static int getHourOfDay(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 当前分钟
	 * @return
	 */
	public static int getMinute(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 取回系统当前时间
	 * @return 如：2012-3-2 15:40:52
	 */
	public static String getTime(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.YEAR) + "-" + 
			  (cld1.get(Calendar.MONTH)+1) + "-" + 
			   cld1.get(Calendar.DATE) + " " + 
			   cld1.get(Calendar.HOUR_OF_DAY) + ":" + 
			   cld1.get(Calendar.MINUTE) + ":" + 
			   cld1.get(Calendar.SECOND) ;
	}
	
	/**
	 * 获取当前系统年月日
	 * @return 如：2012-3-2
	 */
	public static String getYear_Month_Date(){
		cld1 = Calendar.getInstance();		//时间1
		return cld1.get(Calendar.YEAR) + "-" + 
			  (cld1.get(Calendar.MONTH)+1) + "-" + 
			   cld1.get(Calendar.DATE);
	}
}
