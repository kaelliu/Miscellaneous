package lib.kael.util;

import java.util.Random;

public class RandomUtil
{
	public static byte ALPHA = 8;
	public static int  SIZE  = 1<<ALPHA;
	public static int ind(int x)
	{
		return (x & (SIZE - 1));
	}
	public static int barrel(int a)
	{
		return ((a<<19)^(a>>13));
	}
	public static void rc4(int []m,int []r,int[] aa)
	{
		int a,x,y,i;
		a = aa[0];
		for(i = 0;i<SIZE;++i)
		{
			x = m[i];
			a = ind(a+x);
			y = m[a];
			m[i] = y;
			m[a] = x;
			r[i] = m[ind(x+y)];
		}
		aa[0]=a;
	}
	public static void ia(int []m,int []r,int [] bb)
	{
		int b,x,y,i;
		b = bb[0];
		for(i=0;i<SIZE;++i)
		{
			x=m[i];
			m[i]=y=m[ind(x)]+b;
			r[i]=b=m[ind(y>>ALPHA)]+x;
		}
		bb[0]=b;
	}
	
	public static void ibaa(int []m,int []r,int []aa,int []bb)
	{
		int a,b,x,y,i;
		 
		a = aa[0]; b = bb[0];
		for (i=0; i<SIZE; ++i)
		{
		    x = m[i];  
		    a = barrel(a) + m[ind(i+(SIZE/2))];    /* set a */
		    m[i] = y = m[ind(x)] + a + b;          /* set m */
		    r[i] = b = m[ind(y>>ALPHA)] + x;       /* set r */
		}
		bb[0] = b; aa[0] = a;
	}
	
	public static int r_between(int min,int max)
	{
		return new Random().nextInt((max-min))+min;
	}
	public static int r_between_(int min,int max)
	{
		Random r = new Random();
		int timestamp = (int) (System.currentTimeMillis()/1000);
		return (r.nextInt(timestamp)%(max-min))+min;
	}
}