package com.kael.hibernatejpa.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

//import com.kael.hibernatejpa.model.ModelBase;
/**
 *  Dao基础功能实现,实体Dao需继承
 */
@SuppressWarnings("unchecked")
public abstract class BaseJpaDao implements BasementDao{
	private SqlBuilder sqlBuilder = new SqlBuilder();
	
	protected static EntityManagerFactory entityManagerFactory;

	// The Entity Manager Instance attached to the model to be managed
	protected EntityManager entityManager;
	
//	protected ModelBase model;
	public BaseJpaDao()
	{
	   init();
	}
	
	protected void init()
    {
       // Ensure Factory Reference Available
       if(entityManagerFactory == null)
       {
          entityManagerFactory = Persistence.createEntityManagerFactory( "sfs2x.hibernatejpa.project.model.jpa" );
       }

       // Ensure Entity Manager Reference Available
       if(entityManager == null)
       {
          entityManager = entityManagerFactory.createEntityManager();
       }
    }
	
	@Override
	public void clear() {
		getEntityManager().clear();
	}

//	@Transactional
    public <T extends BaseDto> void create(T entity) throws DaoException {
    	startTxn();
//        getEntityManager().persist(entity);
        try 
        {
        	getEntityManager().persist( entity );
        	getEntityManager().getTransaction().commit();
        } 
        catch(EntityExistsException e) 
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Create Failed: " + e.getMessage());
        } 
        catch(IllegalArgumentException e)
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Create Failed: " + e.getMessage());
        } 
        catch(TransactionRequiredException e) 
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Create Failed: " + e.getMessage());
        }
    }

    public <T extends BaseDto> void createBatch(List<T> entitys) throws DaoException {
        for (T entity : entitys) {
            create(entity);
        }
    }

    public <T extends BaseDto> void update(T entity) throws DaoException {
    	startTxn();
        try 
        {
        	getEntityManager().merge(entity);
//        	getEntityManager().flush();
        	getEntityManager().getTransaction().commit();
        }
        catch(TransactionRequiredException e) 
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Update Failed: " + e.getMessage());
        }
        catch(PersistenceException e)
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Update Failed: " + e.getMessage());
        } 
        catch(Exception e)
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Update Failed: " + e.getMessage());
        } 
    }

    public <T extends BaseDto> void saveAll(List<T> entitys) throws DaoException {
        for (int i = 0; i < entitys.size(); i++) {
            T entity = entitys.get(i);
            save(entity);
        }
    }
    public <T extends BaseDto> void save(T entity) throws DaoException {
        if (entity.grabPrimaryKey() == null) {
            this.create(entity);
        } else {
            this.update(entity);
        }
    }

    public <T extends BaseDto> void delete(Class<T> entityClass, Object entityid) throws DaoException {
        delete(entityClass, new Object[] { entityid });
    }
    
    public <T extends BaseDto> void delete(Class<T> entityClass,
        Object[] entityids) throws DaoException {
	    for (Object id : entityids) {
	    	startTxn();
//	        getEntityManager().remove(getEntityManager().find(entityClass, id));
	        try
	        {
	        	getEntityManager().remove(getEntityManager().find(entityClass, id));
	        	getEntityManager().getTransaction().commit();
	        }
	        catch(IllegalArgumentException e)
	        {
	        	getEntityManager().getTransaction().rollback();
	            e.printStackTrace();
	            throw new DaoException("Create Failed: " + e.getMessage());
	        } 
	        catch(TransactionRequiredException e) 
	        {
	        	getEntityManager().getTransaction().rollback();
	            e.printStackTrace();
	            throw new DaoException("Update Failed: " + e.getMessage());
	        }
	    }
    }
    
    public <T extends BaseDto> void delete(T entity) throws DaoException{
    	startTxn();
    	try
        {
        	getEntityManager().remove(entity);
        	getEntityManager().getTransaction().commit();
        }
        catch(IllegalArgumentException e)
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Create Failed: " + e.getMessage());
        } 
        catch(TransactionRequiredException e) 
        {
        	getEntityManager().getTransaction().rollback();
            e.printStackTrace();
            throw new DaoException("Update Failed: " + e.getMessage());
        }
    }

    public <T extends BaseDto> void deleteByWhere(Class<T> entityClass,
            String where, Object[] delParams) {
        StringBuffer sf_QL = new StringBuffer("DELETE FROM ").append(
                sqlBuilder.getEntityName(entityClass)).append(" o WHERE 1=1 ");
        if (where != null && where.length() != 0) {
            sf_QL.append(" AND ").append(where);
        }
        Query query = getEntityManager().createQuery(sf_QL.toString());
        this.setQueryParams(query, delParams);
 
        query.executeUpdate();
    }

    protected void setQueryParams(Query query, Object queryParams) {
        sqlBuilder.setQueryParams(query, queryParams);
    }

    public <T extends BaseDto> T find(Class<T> entityClass, Object entityId) {
        return getEntityManager().find(entityClass, entityId);
    }
 
    public <T extends BaseDto> long getCount(Class<T> entityClass) {
        return getCountByWhere(entityClass, null, null);
    }
    
    public <T extends BaseDto> long getCountByWhere(Class<T> entityClass,
	        String whereql, Object[] queryParams) {
	    StringBuffer sf_QL = new StringBuffer("SELECT COUNT(").append(
	            sqlBuilder.getPkField(entityClass, "o")).append(") FROM ")
	            .append(sqlBuilder.getEntityName(entityClass)).append(
	                    " o WHERE 1=1 ");
	    if (whereql != null && whereql.length() != 0) {
	        sf_QL.append(" AND ").append(whereql);
	    }
	    Query query = getEntityManager().createQuery(sf_QL.toString());
	    this.setQueryParams(query, queryParams);
	    return (Long) query.getSingleResult();
	}
    
    public <T extends BaseDto> boolean isExistedByWhere(Class<T> entityClass,
            String whereql, Object[] queryParams) {
        long count = getCountByWhere(entityClass, whereql, queryParams);
        return count > 0 ? true : false;
    }

    public <T extends BaseDto> QueryResult<T> getPageData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, Object[] queryParams,
            LinkedHashMap<String, String> orderby) {
        return scroll(entityClass, firstindex, maxresult, wherejpql,
                queryParams, orderby);
    }

    public <T extends BaseDto> QueryResult<T> getPageData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, List<Object> queryParams,
            LinkedHashMap<String, String> orderby) {
        Object[] ps = null;
        if (queryParams != null) {
            ps = queryParams.toArray();
        }
        return getPageData(entityClass, firstindex, maxresult, wherejpql, ps,
                orderby);
    }
    
    @Override
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, Map<String, Object> queryParams,
            LinkedHashMap<String, String> orderby){
    	return scroll(entityClass, firstindex, maxresult, wherejpql,
              queryParams, orderby);
    	
    }
    
    /**
     * 根据条件查询实体指定字段的值并回填到实体内. <br/>
     * <b>注意:</b> <br/>
     * 实体必须有包括要查询的字段为参数的构造函数.
     */
    private <T extends BaseDto> QueryResult<T> scroll(Class<T> entityClass,
            String[] queryfields, int firstindex, int maxresult,
            String wherejpql, Object queryParams,
            LinkedHashMap<String, String> orderby) {
        QueryResult<T> qr = new QueryResult<T>();
        String entityname = sqlBuilder.getEntityName(entityClass);
        String where = "";
        if(wherejpql != null){
        	if(wherejpql.length() > 0){
        		where = "WHERE " + wherejpql; 
        	}
        }
        Query query = getEntityManager()
                .createQuery(
                        (sqlBuilder.buildSelect(entityname, queryfields, "o")
                                + "FROM "
                                + entityname
                                + " o "
                                + where + sqlBuilder
                                .buildOrderby(orderby)));
        setQueryParams(query, queryParams);
        if (firstindex != -1 && maxresult != -1)
            query.setFirstResult(firstindex).setMaxResults(maxresult).setHint(
                    "org.hibernate.cacheable", true);
        qr.setResultlist(query.getResultList());
        query = getEntityManager().createQuery(
                "SELECT COUNT("
                        + sqlBuilder.getPkField(entityClass, "o")
                        + ") FROM "
                        + entityname
                        + " o "
                        + where);
        setQueryParams(query, queryParams);
        qr.setTotalrecord((Long) query.getSingleResult());
        return qr;
    }


    /**
     * 根据条件查询某个实体的列表
     * @param <T>
     * @param entityClass
     *            实体类型
     * @param firstindex
     *            开始行
     * @param maxresult
     *            结束行
     * @param wherejpql
     *            where条件
     * @param queryParams
     *            参数
     * @param orderby
     *            排序条件
     * @return
     */
    private <T extends BaseDto> QueryResult<T> scroll(Class<T> entityClass,
            int firstindex, int maxresult, String wherejpql,
            Object queryParams, LinkedHashMap<String, String> orderby) {
        QueryResult<T> qr = new QueryResult<T>();
        String entityname = sqlBuilder.getEntityName(entityClass);
        String where = "";
        if(wherejpql != null){
        	if(wherejpql.length() > 0){
        		where = "WHERE " + wherejpql; 
        	}
        }
        Query query = getEntityManager()
                .createQuery(
                        "SELECT o FROM "
                                + entityname
                                + " o "
                                + where
                                + sqlBuilder.buildOrderby(orderby));
        setQueryParams(query, queryParams);
        if (firstindex != -1 && maxresult != -1)
            query.setFirstResult(firstindex).setMaxResults(maxresult).setHint(
                    "org.hibernate.cacheable", true);
        qr.setResultlist(query.getResultList());
        query = getEntityManager().createQuery(
                "SELECT COUNT("
                        + sqlBuilder.getPkField(entityClass, "o")
                        + ") FROM "
                        + entityname
                        + " o "
                        + where);
        setQueryParams(query, queryParams);
        qr.setTotalrecord((Long) query.getSingleResult());
        return qr;
    }
     
    @Override
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, List<Object> queryParams,
            LinkedHashMap<String, String> orderby) {
        return this.scroll(entityClass, queryfields, firstindex, maxresult,
                wherejpql, queryParams, orderby);
    }
 
    @Override
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, Map<String, Object> queryParams,
            LinkedHashMap<String, String> orderby) {
        return this.scroll(entityClass, queryfields, firstindex, maxresult,
                wherejpql, queryParams, orderby);
    }
    @Override
    public <T extends BaseDto> QueryResult<T> gePageData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, Object[] queryParams,
            LinkedHashMap<String, String> orderby) {
        return this.scroll(entityClass, queryfields, firstindex, maxresult,
                wherejpql, queryParams, orderby);
    }

    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String wheresql, Object[] queryParams) {
        String entityname = sqlBuilder.getEntityName(entityClass);
        Query query = getEntityManager().createQuery(
                "SELECT o FROM "
                        + entityname
                        + " o "
                        + ((wheresql == null || wheresql.length() == 0) ? ""
                                : "WHERE " + wheresql));
        setQueryParams(query, queryParams);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }

    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String wheresql, Object[] queryParams, int startRow, int rows) {
        String entityname = sqlBuilder.getEntityName(entityClass);
        Query query = getEntityManager().createQuery(
                "SELECT o FROM "
                        + entityname
                        + " o "
                        + ((wheresql == null || wheresql.length() == 0) ? ""
                                : "WHERE " + wheresql));
        setQueryParams(query, queryParams);
        if (startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rows > 0) {
            query.setMaxResults(rows);
        }
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }
 
    @Override
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams) {
        return queryByWhere(entityClass, queryfields, wheresql, queryParams,
                -1, -1);
    }

    @Override
    public <T extends BaseDto> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams,
            int startRow, int rows) {
        String entityname = sqlBuilder.getEntityName(entityClass);
        Query query = getEntityManager().createQuery(
                sqlBuilder.buildSelect(entityname, queryfields, "o") + " FROM "
                        + entityname + " o "
                        + (wheresql == null ? "" : "WHERE " + wheresql));
        setQueryParams(query, queryParams);
        if (startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rows > 0) {
            query.setMaxResults(rows);
        }
        return query.getResultList();
    }

    public <T extends BaseDto> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams) {
        return queryFieldValues(entityClass, queryfields, wheresql,
                queryParams, -1, -1);
    }

    @Override
    public <T extends BaseDto> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams, int startRow, int rows) {
        String entityname = sqlBuilder.getEntityName(entityClass);
        Query query = getEntityManager().createQuery(
                sqlBuilder.buildSelect(queryfields, "o") + " FROM "
                        + entityname + " o "
                        + (wheresql == null ? "" : "WHERE " + wheresql));
        setQueryParams(query, queryParams);
        if (startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rows > 0) {
            query.setMaxResults(rows);
        }
        return query.getResultList();
    }

    @Override
    public <T extends BaseDto> T load(Class<T> entityClass, Object entityId) {
        try {
            return getEntityManager().getReference(entityClass, entityId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T extends BaseDto> T findByWhere(Class<T> entityClass, String where,
            Object[] params) {
         List<T> l = queryByWhere(entityClass, where, params);
         if(l != null && l.size() == 1){
             return l.get(0);
         }else if(l.size() > 1){
             throw new RuntimeException("查寻到的结果不止一个.");
         }else{
             return null;
         }
    }
    
    /**
     * 设置查询参数
     * @param query
     *            查询
     * @param queryParams
     *            查询参数
     */
    protected void setQueryParams(Query query, Object[] queryParams) {
        sqlBuilder.setQueryParams(query, queryParams);
    }

    
	@Override
	public abstract EntityManager getEntityManager();
	
	protected void startTxn() throws DaoException
    {
	    try
	    {
		    getEntityManager().getTransaction().begin();
	    }
	    catch(IllegalStateException e)
	    {
	       throw new DaoException("Start Txn Failed: " + e.getMessage());
	    }
   }
	
	/**
	 * 按字段更新
	 * @throws DaoException 
	 */
	public <T extends BaseDto> boolean updateByQuery(Class<T> entityClass,Map fieldsAndParams,String wheresql) throws DaoException{
		String entityname = sqlBuilder.getEntityName(entityClass);        
        updateProcess(entityname,fieldsAndParams,wheresql);
		return true;
	}

	public <T extends BaseDto> boolean updateDirectly(String tableName,Map fieldsAndParams,String wheresql) throws DaoException{
		updateProcess(tableName,fieldsAndParams,wheresql);
        return true;
	}
	
	private void updateProcess(String tableName,Map fieldsAndParams,String wheresql) throws DaoException{
		StringBuffer sf_update = new StringBuffer("UPDATE ").append(tableName).append(" o set");
		Object[] params = new Object[fieldsAndParams.size()];
        int index = 0;
        Set entrys = fieldsAndParams.entrySet();
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				sf_update.append(" ").append(entry.getKey()).append("=?,");
				params[index++] = entry.getValue();
			}
		}

		Query query = getEntityManager().createQuery(sf_update.substring(0, sf_update.length() - 1)+((wheresql==null)?"":(" where "+wheresql)));
		setQueryParams(query, params);
        startTxn();
        query.executeUpdate();
        getEntityManager().getTransaction().commit();
	}
}
