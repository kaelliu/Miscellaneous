package com.kael.hibernatejpa.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

public abstract class BaseDto implements Serializable{
	private static final long serialVersionUID = 1962905939086138888L;
	private transient EOUtility eoutil ;
	protected boolean selected;
	@Transient
    public boolean isSelected() {
        return selected;
    }
	
	public void setSelected(boolean selected) {
        this.selected = selected;
    }
 
    // property which not persist to database
    @Transient
    protected EOUtility getBeanUtility() {
        if (eoutil == null) {
            eoutil = new EOUtility(this);
        }
        return eoutil;
    }

    @Override
    public String toString() {
        return getBeanUtility().beanToString();
    }
 
    @Override
    public boolean equals(Object obj) {
        return getBeanUtility().equalsBean(obj);
    }
 
    @Override
    public int hashCode() {
        return getBeanUtility().hashCodeBean();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object obj = null;
        try {
            obj = getBeanUtility().cloneBean();
        } catch (Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
 
        return obj;
    }

    /**
     * �õ����пɳ־û��ֶε�����
     * @return
     *      �����б�
     */
    @Transient
    public String[] getAttributeNames(){
        return getBeanUtility().getAttributeNames();
    }
     
    /**
     * �õ�ĳ���ֶε�ֵ
     * @param attributeName
     *      �ֶ���
     * @return
     *      ֵ
     */
    @Transient
    public Object getAttributeValue(String attributeName) {
        return getBeanUtility().getAttributeValue(attributeName);
    }
     
    /**
     * ����ĳ���ֶε�ֵ
     * @param attributeName
     *      �ֶ���
     * @param value
     *      ֵ
     */
    @Transient
    public void setAttributeValue(String attributeName , Object value){
        getBeanUtility().setAttributeValue(attributeName,value);
    }
     
    @SuppressWarnings("static-access")
    @Transient
    public String getEnumDescription(String enumAttributeName){
        Object value = getAttributeValue(enumAttributeName);
         
        return getBeanUtility().getEnumDescription(value);
    }
 
    /**
     * ���ʵ���Ӧ�ı���
     */
    @Transient
    public String getTableName() {
        return getBeanUtility().getTableName();
    }
 
    /**
     * �Ƚϴ˶�������һ������Ĳ�𣬲�����ֵ��ͬ���ֶε����ơ�
     * @param antherBean
     *            ��Ҫ�ȽϵĶ���
     * @return ֵ��ͬ���ֶ���
     */
    @Transient
    public List<String> getDifferentField(BaseDto anotherBean) {
        return getBeanUtility().getDifferentField(anotherBean);
    }
 
    /**
     * ��ȡ����ֵ
     * @return ����ֵ
     */
    @Transient
    public abstract Object grabPrimaryKey();
 
    /**
     * �Ƚ�����ֵ�Ƿ���ͬ
     */
    @Transient
    public boolean equalsPK(Object obj) {
        if (obj == null)// ����Ϊ�ղ��Ƚ�
            return false;
        // ���Ͳ�ͬ���ؽ��бȽ�
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
 
        // ����BaseEO�����رȽ�
        if (!(obj instanceof BaseDto)) {
            return false;
        }
 
        BaseDto eo = (BaseDto) obj;
 
        if (grabPrimaryKey()!=null
                && eo.grabPrimaryKey()!=null) {
            if (grabPrimaryKey().equals(eo.grabPrimaryKey()))
                return true;
            return false;
        } else {
            return false;
        }
 
    }
 
    /**
     * ������һ��eo�����е��ֶ�ֵ����ǰ������
     * @param fromEO            ��������
     * @param copyAttributes    ������Щ�ֶ�
     */
    public void copyAttributeValue(BaseDto fromEO , String[] copyAttributes){
        if(copyAttributes == null)
            return ;
         
        for (String attr : copyAttributes) {
            this.setAttributeValue(attr, fromEO.getAttributeValue(attr));
        }
    }
     
    /**
     * ���������ӳټ����ֶ�
     */
    public void loadLazyAttributes(){
        getBeanUtility().loadLazyField();
    }

}
