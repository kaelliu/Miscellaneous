package lib.kael.util;

public class KaelBit {
	
	private static int getBitNumberAo(int from,int to)
	{
		if(from > 32 || to > 32 || from ==0 || to == 0)return -1;
		if(from > to) return -1;
		int result=0;
		for(int i=from;i <= to;++i)
		{
			result = result | (1<<(i-1));
		}
		return result;
	}
	
	public static int getNumberInBits(int from,int to,int number)
	{
		int result = (number & getBitNumberAo(from, to)) >> (from - 1);
		return result;
	}
	
	public static int setNumberInBits(int from,int to,int number,int oriNumber)
	{
		int sectionLow = getNumberInBits(1,from - 1,oriNumber);
		int sectionHight = (oriNumber >> to) << to; 
		int result = (number << (from-1)) | sectionLow | sectionHight;
		return result;
	}
}
