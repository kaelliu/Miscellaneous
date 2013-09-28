package com.kael.hibernatejpa.dao;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * ʵ��bean������<br>
 * <b> ע�⣺�˹�����ֻ��Ӧ����BaseEntityBean�������࣡ </b><br>
 * ʵ�ֹ��ܣ� <li>ʵ��beanת��Ϊstring <li>ʵ��bean�Ƚ� <li>�õ�ʵ��bean��ϣֵ <li>���¡ʵ��bean
 * 
 */
@SuppressWarnings("unchecked")
public class EOUtility {

	// /** ��ǰbean�����е��ֶ�(���������Լ������еĹ��кͷǹ����ֶ�) <get������,�ֶ�> **/
	// private HashMap<String,Field> hm_Field;

	/** ��ǰbean���Լ������й��е�get���� <�ֶ���,get����> **/
	private HashMap<String, Method> hm_Geters;

	/** ��ǰbean���Լ������й��е�set���� <�ֶ���,get����> **/
	private HashMap<String, Method> hm_Seters;
	
	/** ��ǰbean���Լ��������ӳټ����ֶε�get���� <�ֶ���,get����> **/
	private HashMap<String, Method> hm_LazyGeters;
	/** ��ǰbean���Լ��������ӳټ����ֶε�get���� <�ֶ���,set����> **/
	private HashMap<String, Method> hm_LazySeters;

	/** Ӧ�ñ��������ʵ����� **/
	private BaseDto bean;

	/** Ӧ�ñ��������ʵ��������� **/
	private Class<? extends BaseDto> clazz;
	
	private String beanDispName;
	
	/** �洢�ֶζ�Ӧ������ **/
	private HashMap<String, String> hm_DispNames;

	public EOUtility(BaseDto bean) {
		init(bean);
	}

	private void init(BaseDto bean) {
		// ��ǰbean���´����bean��ͬһ��beanʱ���ؽ��г�ʼ��
		if (this.bean == bean)
			return;

		this.bean = bean;
		clazz = bean.getClass();

		initGetterAndSetters();
		// initField();
		// initGeters();
	}
	
	private void buildGetterANDSetters(Class beanclass) {
		// �õ���ǰ���ֶ�����
		Field[] fields = beanclass.getDeclaredFields();
		String fieldname = null;
		// ƴ���ֶζ�Ӧ�ķ�����
		for (Field field : fields) {
			// һ�Զ��ֶβ�Ҫ�μ�toString��hashcode��equals����������Ҫ���أ�һ�����ط������������ݼ�������ʱ������
			if (isLazyField(field.getAnnotations())) {
				continue;
			}
			fieldname = field.getName();
			for (PropertyDescriptor property : propertyDescriptors) {
				if (fieldname.equals(property.getName())) {
					Method reader = property.getReadMethod();
					Method writer = property.getWriteMethod();

					Transient t = reader.getAnnotation(Transient.class);
					if(t==null){
						if (reader != null
								&& !(isLazyField(reader.getAnnotations()))){
							hm_Geters.put(fieldname, reader);// ���ӳ�
							if (writer != null)
								hm_Seters.put(fieldname, writer);
						}else{
							hm_LazyGeters.put(fieldname, reader); //�ӳ�
							if (writer != null)
								hm_LazySeters.put(fieldname, writer);
						}
					}
				}
			}
		}
		// ��ǰ�಻�� BaseEntityBeanʱ���ݹ����
		if (!beanclass.equals(BaseDto.class)) {
			buildGetterANDSetters((Class<? extends BaseDto>) beanclass
					.getSuperclass());
		}
	}
	
	/**
	 * �õ�EOҪ��ʾ��������
	 */
	public String getEODisplayName(){
		if(beanDispName == null){
		EODisplayName ea = clazz.getAnnotation(EODisplayName.class);
			if(ea != null){
				beanDispName = ea.value();
			}else{
				beanDispName = clazz.getSimpleName();
			}
		}
		return beanDispName;
	}

	/**
	 * �õ��ֶ���ʾ������
	 */
	public String getFieldDisplayName(String fieldName){
		String dispName = hm_DispNames.get(fieldName);
		if(dispName == null){
			dispName = getFieldDisplayName(clazz,fieldName);
			hm_DispNames.put(fieldName,dispName );
		}
		
		return dispName;
	}
	
	private String getFieldDisplayName(Class clz ,String fieldName){
		String dispName = null;
		Field f;
		try {
			f = clz.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			return fieldName;
		} catch (NoSuchFieldException e) {
			if(!clz.getSuperclass().equals(BaseDto.class))
				return getFieldDisplayName(clz.getSuperclass(),fieldName);
			else
				return fieldName;
		}
		FieldDisplayName am = f.getAnnotation(FieldDisplayName.class);
		if(am!=null){
			dispName = am.value();
		}else{
			dispName = fieldName;
		}
		return dispName;
	}
	
	/*
	 * ����ע���ж��Ƿ����ӳټ��ص��ֶ�
	 */
	public static boolean isLazyField(Annotation[] annotations) {
		// ����ѭ�����κ�һ��������Ϊ�ӳټ����ֶ�
		for (Annotation annotation : annotations) {
			if (annotation instanceof OneToOne) {
				if (FetchType.LAZY.equals(((OneToOne) annotation).fetch())) {
					return true;
				}
			}
			if (annotation instanceof ManyToOne) {
				if (FetchType.LAZY.equals(((ManyToOne) annotation).fetch())) {
					return true;
				}
			}
			// OneToMany Ĭ��Ϊ�ӳټ���,���û�б�ע�������������ӳټ���
			if (annotation instanceof OneToMany) {
				if (!FetchType.EAGER.equals(((OneToMany) annotation).fetch())) {
					return true;
				}
			}
			// ManyToMany ͬ��
			if (annotation instanceof ManyToMany) {
				if (!FetchType.EAGER.equals(((ManyToMany) annotation).fetch())) {
					return true;
				}
			}

			// Lob�ֶ������Ӵ�,�����ǲ����ӳټ���,ȫ�������д���
			if (annotation instanceof Lob) {
				return true;
			}

			// �ǳ־��ֶβ�����
			if (annotation instanceof Transient) {
				return true;
			}
		}

		return false;
	}

	PropertyDescriptor[] propertyDescriptors = null;

	/**
	 * ��ʼ��get��set����
	 */
	private void initGetterAndSetters() {
		try {
			propertyDescriptors = Introspector.getBeanInfo(clazz)
					.getPropertyDescriptors();
			if (hm_Geters == null)
				hm_Geters = new HashMap<String, Method>();
			hm_Geters.clear();
			if (hm_LazyGeters == null)
				hm_LazyGeters = new HashMap<String, Method>();
			hm_LazyGeters.clear();
			if (hm_Seters == null)
				hm_Seters = new HashMap<String, Method>();
			hm_Seters.clear();
			if (hm_LazySeters == null)
				hm_LazySeters = new HashMap<String, Method>();
			hm_LazySeters.clear();
			if (hm_DispNames == null)
				hm_DispNames = new HashMap<String, String>();
			hm_DispNames.clear();
			buildGetterANDSetters(clazz);
		} catch (IntrospectionException e) {
		}
	}

	/**
	 * ����ָ�����Ե�ֵ
	 * @param attName
	 *            ������
	 * @param value
	 *            ֵ
	 */
	public void setAttributeValue(String attName, Object value) {
		try {
			Method m = hm_Seters.get(attName);
			if(m == null) m = hm_LazySeters.get(attName);
				m.invoke(bean, new Object[] { value });
		} catch (Exception e) {
		}
	}

	/**
	 * �õ�ָ�����Ե�ֵ
	 * @param attName
	 *            ������
	 * @return ֵ
	 */
	public Object getAttributeValue(String attName) {
		Object o = null;
		try {
			Object[] os = null;
			Method m = hm_Geters.get(attName);
			if(m == null) m = hm_LazyGeters.get(attName);
				o = m.invoke(bean, os);
		} catch (Exception e) {
		}
		return o;
	}

	/**
	 * �õ�������
	 * @return ����
	 */
	public String getTableName() {
		javax.persistence.Table table = clazz
				.getAnnotation(javax.persistence.Table.class);
		String tablename = null;
		if (table == null) {
			Class clazzp = clazz.getSuperclass();
			while (!clazzp.equals(BaseDto.class)) {
				table = (Table) clazzp
						.getAnnotation(javax.persistence.Table.class);
				if (table != null) {
					tablename = table.name();
					break;
				} else {
					clazzp = clazzp.getSuperclass();
				}
			}
		} else {
			tablename = table.name();
		}
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getSimpleName().toUpperCase();
		}
		return tablename;
	}

	private String[] fieldNames;

	/**
	 * �õ�ʵ�������г־û��ֶ���
	 * @return �ֶ���������
	 */
	public String[] getAttributeNames() {
		if (fieldNames == null) {
			Set<String> set_fieldnames = hm_Geters.keySet();
			fieldNames = new String[0];
			fieldNames = set_fieldnames.toArray(fieldNames);
		}
		return fieldNames;
	}

	/**
	 * ��һ������String�� <br>
	 * ��ʽ���£� <br>
	 * TABLE_NAME::���� <br>
	 * �ֶ���::�ֶ�ֵ �ֶ���::�ֶ�ֵ
	 * 
	 * @param bean
	 * @return
	 */
	public String beanToString() {
		StringBuffer sb_tostring = new StringBuffer();
		sb_tostring.append("����:[").append(getEODisplayName())
				.append("] ");

		String[] fieldnames = getAttributeNames();

		for (String fieldname : fieldnames) {
			Object obj_value = null;
			try {
				obj_value = getAttributeValue(fieldname);
				if (obj_value instanceof Date) {
					obj_value = DateUtil.formatDate((Date) obj_value, null);
				} else if (obj_value instanceof BaseDto) {
					obj_value = ((BaseDto) obj_value).grabPrimaryKey();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb_tostring.append(getFieldDisplayName(fieldname)).append(":[").append(obj_value)
					.append("]\t");
		}

		return sb_tostring.toString();
	}

	/**
	 * �жϵ�ǰbean�Ƿ������������ͬ
	 * 
	 * @param obj
	 * @return
	 */
	public boolean equalsBean(Object obj) {
		if (obj == null)// ����Ϊ�ղ��Ƚ�
			return false;

		// ����BaseEO�����رȽ�
		if (!(obj instanceof BaseDto)) {
			return false;
		}

		// ���Ͳ�ͬ���ؽ��бȽ�
		if (!clazz.equals(obj.getClass())) {
			return false;
		}

		// ���αȽ��ֶ�ֵ��������ͬ���򷵻�false
		String[] fieldnames = getAttributeNames();
		for (String fieldname : fieldnames) {
			boolean same = equalsField(fieldname, bean, obj);
			if (!same) {
				return false;
			}
		}

		return true;
	}

	/**
	 * �Ƚϵ�ǰ��������һ������Ĳ�𣬲�����ֵ��ͬ���ֶε����ơ�
	 * @param antherBean
	 *            ��Ҫ�ȽϵĶ���
	 * @return ֵ��ͬ���ֶ���
	 */
	public List<String> getDifferentField(BaseDto anotherBean) {
		// ���Ͳ�ͬ���ؽ��бȽ�
		if (!clazz.equals(anotherBean.getClass())) {
			throw new ClassCastException(anotherBean.getClass().getName()
					+ "Cann't Cast to " + clazz.getName());
		}
		List<String> differents = new ArrayList<String>();
		String[] fieldnames = getAttributeNames();
		for (String fieldname : fieldnames) {
			boolean same = equalsField(fieldname, bean, anotherBean);
			if (!same) {
				differents.add(fieldname);
			}
		}
		return differents;
	}

	/**
	 * �Ƚ���������ָ�����ֶ�ֵ�Ƿ���ͬ
	 * @param fieldName
	 *            ��Ҫ�Ƚϵ��ֶ�
	 * @param obj1
	 *            ����1
	 * @param obj2
	 *            ����2
	 * @return ֵ��ͬ��Ϊtrue
	 */
	private boolean equalsField(String fieldName, Object obj1, Object obj2) {
		try {
			Object obj_value = null;
			Object current_value = null;
			Method getter = hm_Geters.get(fieldName);
			Object[] os = null;
			current_value = getter.invoke(obj1, os);
			obj_value = getter.invoke(obj2, os);

			if (current_value == null && obj_value == null) {
				return true;
			}else if(current_value != null && obj_value != null){
				if(current_value instanceof BaseDto && obj_value instanceof BaseDto){// ����ݹ�Ƚ�,�ڲ��ֶ������baseeo������ֻ�Ƚ�pk
					return ((BaseDto)current_value).equalsPK(obj_value);
				}
				if(current_value instanceof Date && obj_value instanceof Date){ // �������ͱȽ����⴦��
					return DateUtil.equalsDate((Date)current_value, (Date)obj_value);
				}
				
				return obj_value.equals(current_value);
			}else if (current_value != null) {
				return current_value.equals(obj_value);
			} else if (obj_value != null) {
				return obj_value.equals(current_value);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	/**
	 * ���ظö���Ĺ�ϣ��ֵ
	 */
	public int hashCodeBean() {

		// ���ɼ򵥵�λ����hashɢ����
		String key = bean.toString();
		int prime = key.hashCode();
		int hash = prime;
		for (int i = 0; i < key.length(); i++) {
			hash ^= (hash << 23 >> 17) ^ key.charAt(i) * 13131;
		}
		// ���ؽ��
		return (hash % prime) * 33;
	}

	/**
	 * ��������ȿ�¡ʵ����
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object cloneBean() throws IOException, ClassNotFoundException {
		return cloneObject(bean);
	}

	/**
	 * ȡ��һ��ö��ֵ�ϵ�����ע��.
	 * @param emumValue
	 *            ö��ֵ
	 * @return �������Ĳ���ö��ֵ���򷵻ؿմ�,����ö��û�б�עע��,�򷵻�ö��toString.
	 */
	public static String getEnumDescription(Object emumValue) {
		String desValue = "";
		if (emumValue != null) {
			try {
				String enumName = ((Enum) emumValue).name();
				desValue = emumValue.getClass().getField(enumName)
						.getAnnotation(EnumDescription.class).value();
			} catch (Exception e) {
				return emumValue.toString();
			}
		}
		return desValue;
	}

	public static Object cloneObject(Object obj) throws IOException,
			ClassNotFoundException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		Object cloneObj = (oi.readObject());
		bo.close();
		oo.close();
		bi.close();
		oi.close();
		return cloneObj;
	}
	
	/**
	 * ���������ӳټ��ص��ֶ�.
	 */
	void loadLazyField(){
		Iterator<Method> i_mds = hm_LazyGeters.values().iterator();
		while(i_mds.hasNext()){
			Method m = i_mds.next();
			try {
				Object[] os = null;
				Object o = m.invoke(bean, os);
				if(o!=null)
					o.toString();
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
}
