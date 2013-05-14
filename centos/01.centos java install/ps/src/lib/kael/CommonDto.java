package lib.kael;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CommonDto {
	/**
     * @param att
     *            attribute
     * @param value
     *            value for set
     * @param type
     *            type of value
     * */
	public void setter(String att, Object value,
            Class<?> type) {
        try {
            Method method = this.getClass().getMethod("set" + capitalize(att), type);
            method.invoke(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void setterFromMap(String key, Map<?, ?> container) {
        try {
            Method method = null;
            invokeFunc(method,key,container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private void invokeFunc(Method method,String key,Map<?, ?> container) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Field field = this.getClass().getDeclaredField(key);
		Class type = field.getType();
		method = this.getClass().getMethod("set" + capitalize(key), type);
		if(type == Long.TYPE){
			method.invoke(this, (Long)container.get(key));
		}else if(type == Short.TYPE){
			method.invoke(this, ((Integer)container.get(key)).shortValue());
		}else if(type == Byte.TYPE){
			method.invoke(this, ((Integer)container.get(key)).byteValue());
		}else{
			method.invoke(this, container.get(key));
		}
	}
	
	public void setterFromMap(Map<?, ?> container) {
		
		Set entrys = container.entrySet();
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				String key = (String) entry.getKey();
				Method method = null;
				try {
					invokeFunc(method,key,container);
				} catch (NoSuchFieldException e) {
//					e.printStackTrace();
					continue;
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
	
	/**
     * @param att
     *            attribute for get
     * */
	public Object getter(String att) {
        try {
            Method method = this.getClass().getMethod("get" + capitalize(att));
            return method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
}
