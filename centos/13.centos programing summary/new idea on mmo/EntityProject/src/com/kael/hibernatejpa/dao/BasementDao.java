package com.kael.hibernatejpa.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
/**
 * Dao基类
 */
public interface BasementDao {
	/**
     * 清除一级缓存的数据
     */
    public void clear();
    
    /**
     * 新增实体
	 * @param entity 实体
     */
    public <T extends BaseDto> void create(T entity) throws DaoException;
    
    /**
     * 批量新增实体
	 * @param entity 实体
     */
    public <T extends BaseDto> void createBatch(List<T> entitys) throws DaoException;

    /**
     * 更新实体
	 * @param entity 实体
     */
    public <T extends BaseDto> void update(T entity) throws DaoException;

    /**
     * 删除实体
	 * @param entityClass 实体类
	 * @param entityid 实体id
     */
    public <T extends BaseDto> void delete(Class<T> entityClass, Object entityid) throws DaoException ;
    
    public <T extends BaseDto> void delete(T entity) throws DaoException;
    /**
     * 批量删除实体
	 * @param entityClass 实体类
	 * @param entityid 实体id
     */
    public <T extends BaseDto> void delete(Class<T> entityClass,
    		            Object[] entityids) throws DaoException ;

    /**
     * 批量条件删除实体
	 * @param entityClass 实体类
	 * @param where 条件
	 * @param entityid 实体id
     */
    public <T extends BaseDto> void deleteByWhere(Class<T> entityClass,
    		            String where, Object[] delParams);
    
    /**
     * 获取实体
     *
     * @param <T>
     * @param entityClass
     *            实体类
     * @param entityId
     *            实体id
     * @return
     */
    public <T extends BaseDto> T find(Class<T> entityClass, Object entityId);

    /**
     * 条件获取实体
     *
     * @param <T>
     * @param entityClass
     *            实体类
     * @param where 条件
     * @param entityId
     *            实体id
     * @return
     */
    public <T extends BaseDto> T findByWhere(Class<T> entityClass, String where ,Object[] params);
    
    public <T extends BaseDto> T load(Class<T> entityClass, Object entityId);
    
    /**
     * 根据条件判断实体是否存在
     * @param entityClass
     *            实体类
     * @param whereql
     *            查询条件(可空,可为 field1=? and field2=? 形式,也可为field1='value1' and
     *            field2='value2'的形式)
     * @param queryParams
     *            参数(可空，但是当条件使用了field1=? and field2=? 的形式后参数不能为空)
     * @return 是否存在
     */
    public <T extends BaseDto> boolean isExistedByWhere(Class<T> entityClass,
    		            String whereql, Object[] queryParams);

    /**
     * 获取记录总数
     *
     * @param entityClass
     *            实体类
     * @return
     */
    public <T extends BaseDto> long getCount(Class<T> entityClass);

    public <T extends BaseDto> long getCountByWhere(Class<T> entityClass,
    		            String whereql, Object[] queryParams);
    
    /**
     * 获取分页数据
     *
     * @param <T>
     * @param entityClass
     *            实体类
     * @param firstindex
     *            开始索引
     * @param maxresult
     *            需要获取的记录数
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
     * @param queryfields 需求查询字段
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
     * 根据条件查询实体中的指定几个字段 <br>
     * 返回结果List<String[]>格式如下： <br>
     * 行1： 字段1value , 字段2value , 字段3value <br>
     * 行2： 字段1value , 字段2value , 字段3value
     */

    public <T extends BaseDto> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams);
 
    public <T extends BaseDto> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams, int startRow, int rows);

    public EntityManager getEntityManager();
    
    /**
     * 根据where条件查询实体bean列表,可指定取第几行到第几行 <br>
     * where和queryParams可空
     * @param startRow
     *            开始行
     * @param rows
     *            共多少行
     */

    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
    		String wheresql, Object[] queryParams);
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
    		String wheresql, Object[] queryParams, int startRow, int rows);


    /**
     * 根据条件查询实体中的指定几个字段 <br>
     * 返回的结果将重新组装到实体属性中，没有查询的字段为NULL<br>
     * 注意：使用该接口时，要确保实体类中有对应的查询字段的有参数构造方法，并且参数的顺序要和此处的queryfields数组的元素一致
     * @param startRow
     *            开始行
     * @param rows
     *            共多少行
     */
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams);
 
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams,
            int startRow, int rows);
    
    public <T extends BaseDto> boolean updateByQuery(Class<T> entityClass,Map fieldsAndParams,String wheresql) throws DaoException;
    
    public <T extends BaseDto> boolean updateDirectly(String tableName,Map fieldsAndParams,String wheresql) throws DaoException;
}
