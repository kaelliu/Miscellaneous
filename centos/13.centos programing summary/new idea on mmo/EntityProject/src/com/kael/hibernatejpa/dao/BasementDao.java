package com.kael.hibernatejpa.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
/**
 * Dao����
 */
public interface BasementDao {
	/**
     * ���һ�����������
     */
    public void clear();
    
    /**
     * ����ʵ��
	 * @param entity ʵ��
     */
    public <T extends BaseDto> void create(T entity) throws DaoException;
    
    /**
     * ��������ʵ��
	 * @param entity ʵ��
     */
    public <T extends BaseDto> void createBatch(List<T> entitys) throws DaoException;

    /**
     * ����ʵ��
	 * @param entity ʵ��
     */
    public <T extends BaseDto> void update(T entity) throws DaoException;

    /**
     * ɾ��ʵ��
	 * @param entityClass ʵ����
	 * @param entityid ʵ��id
     */
    public <T extends BaseDto> void delete(Class<T> entityClass, Object entityid) throws DaoException ;
    
    public <T extends BaseDto> void delete(T entity) throws DaoException;
    /**
     * ����ɾ��ʵ��
	 * @param entityClass ʵ����
	 * @param entityid ʵ��id
     */
    public <T extends BaseDto> void delete(Class<T> entityClass,
    		            Object[] entityids) throws DaoException ;

    /**
     * ��������ɾ��ʵ��
	 * @param entityClass ʵ����
	 * @param where ����
	 * @param entityid ʵ��id
     */
    public <T extends BaseDto> void deleteByWhere(Class<T> entityClass,
    		            String where, Object[] delParams);
    
    /**
     * ��ȡʵ��
     *
     * @param <T>
     * @param entityClass
     *            ʵ����
     * @param entityId
     *            ʵ��id
     * @return
     */
    public <T extends BaseDto> T find(Class<T> entityClass, Object entityId);

    /**
     * ������ȡʵ��
     *
     * @param <T>
     * @param entityClass
     *            ʵ����
     * @param where ����
     * @param entityId
     *            ʵ��id
     * @return
     */
    public <T extends BaseDto> T findByWhere(Class<T> entityClass, String where ,Object[] params);
    
    public <T extends BaseDto> T load(Class<T> entityClass, Object entityId);
    
    /**
     * ���������ж�ʵ���Ƿ����
     * @param entityClass
     *            ʵ����
     * @param whereql
     *            ��ѯ����(�ɿ�,��Ϊ field1=? and field2=? ��ʽ,Ҳ��Ϊfield1='value1' and
     *            field2='value2'����ʽ)
     * @param queryParams
     *            ����(�ɿգ����ǵ�����ʹ����field1=? and field2=? ����ʽ���������Ϊ��)
     * @return �Ƿ����
     */
    public <T extends BaseDto> boolean isExistedByWhere(Class<T> entityClass,
    		            String whereql, Object[] queryParams);

    /**
     * ��ȡ��¼����
     *
     * @param entityClass
     *            ʵ����
     * @return
     */
    public <T extends BaseDto> long getCount(Class<T> entityClass);

    public <T extends BaseDto> long getCountByWhere(Class<T> entityClass,
    		            String whereql, Object[] queryParams);
    
    /**
     * ��ȡ��ҳ����
     *
     * @param <T>
     * @param entityClass
     *            ʵ����
     * @param firstindex
     *            ��ʼ����
     * @param maxresult
     *            ��Ҫ��ȡ�ļ�¼��
     * @return
     */

    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, Object[] queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, List<Object> queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, Map<String, Object> queryParams,
            LinkedHashMap<String, String> orderby);
    /**
     * @param queryfields �����ѯ�ֶ�
     */
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, Object[] queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, List<Object> queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, Map<String, Object> queryParams,
            LinkedHashMap<String, String> orderby);

    /**
     * ����������ѯʵ���е�ָ�������ֶ� <br>
     * ���ؽ��List<String[]>��ʽ���£� <br>
     * ��1�� �ֶ�1value , �ֶ�2value , �ֶ�3value <br>
     * ��2�� �ֶ�1value , �ֶ�2value , �ֶ�3value
     */

    public <T extends BaseDto> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams);
 
    public <T extends BaseDto> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams, int startRow, int rows);

    public EntityManager getEntityManager();
    
    /**
     * ����where������ѯʵ��bean�б�,��ָ��ȡ�ڼ��е��ڼ��� <br>
     * where��queryParams�ɿ�
     * @param startRow
     *            ��ʼ��
     * @param rows
     *            ��������
     */

    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
    		String wheresql, Object[] queryParams);
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
    		String wheresql, Object[] queryParams, int startRow, int rows);


    /**
     * ����������ѯʵ���е�ָ�������ֶ� <br>
     * ���صĽ����������װ��ʵ�������У�û�в�ѯ���ֶ�ΪNULL<br>
     * ע�⣺ʹ�øýӿ�ʱ��Ҫȷ��ʵ�������ж�Ӧ�Ĳ�ѯ�ֶε��в������췽�������Ҳ�����˳��Ҫ�ʹ˴���queryfields�����Ԫ��һ��
     * @param startRow
     *            ��ʼ��
     * @param rows
     *            ��������
     */
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams);
 
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams,
            int startRow, int rows);
    
    public <T extends BaseDto> boolean updateByQuery(Class<T> entityClass,Map fieldsAndParams,String wheresql) throws DaoException;
    
    public <T extends BaseDto> boolean updateDirectly(String tableName,Map fieldsAndParams,String wheresql) throws DaoException;
}
