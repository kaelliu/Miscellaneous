package com.kael.hibernatejpa.dao;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;

/**
 * SQL语句构建工具类
 */
public class SqlBuilder {
	
	public <T> String getEntityName(Class<T> entityClass) {
        String entityname = entityClass.getName();
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity.name() != null && !"".equals(entity.name())) {
            entityname = entity.name();
        }
        return entityname;
    }

	/**
     * 创建Select后所要查询的字段名称字符串
     * @param fields
     *          需要查询的字段
     * @param alias 
     *          表的别名
     * @return
     *          拼接成的字段名字符串
     */
	public String buildSelect(String[] fields, String alias) {
        StringBuffer sf_select = new StringBuffer("SELECT");
        for (String field : fields) {
            sf_select.append(" ").append(alias).append(".").append(field)
                    .append(",");
        }
        return (sf_select.substring(0, sf_select.length() - 1)).toString();
    }

	/**
     * 创建Select后所要查询的字段名称字符串，并作为实体类的构造函数
     */
	public String buildSelect(String className,String[] fields, String alias) {
        StringBuffer sf_select = new StringBuffer("SELECT new ").append(className).append("(");
        for (String field : fields) {
            sf_select.append(" ").append(alias).append(".").append(field)
                    .append(",");
        }
        return (sf_select.substring(0, sf_select.length() - 1))+")";
    }

	public String buildUpdate(String className,String[] fields){
		StringBuffer sf_update = new StringBuffer("UPDATE ").append(className).append(" o set");
		for (String field : fields) {
			sf_update.append(" ").append(field).append("=? ");
		}
		return (sf_update.substring(0, sf_update.length() - 1));
	}
	
	@Deprecated
	public String buildUpdate(String className,Map fieldAndParams,String wheresql){
		StringBuffer sf_update = new StringBuffer("UPDATE ").append(className).append(" o set");
        Set entrys = fieldAndParams.entrySet();
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				sf_update.append(" ").append(entry.getKey()).append("=").append(entry.getValue()).append(",");
			}
		}
		sf_update.deleteCharAt(sf_update.length() -1 ).append((wheresql==null)?"":(" where "+wheresql));
		return (sf_update.toString());
	}
	
	/**
     * 组装order by语句
     *
     * @param orderby
     *      列名为key ,排序顺序为value的map
     *      o.column desc
     * @return
     *      Order By 子句
     */

	public String buildOrderby(LinkedHashMap<String, String> orderby) {
        StringBuffer orderbyql = new StringBuffer("");
        if (orderby != null && orderby.size() > 0) {
            orderbyql.append(" order by ");
            for (String key : orderby.keySet()) {
                orderbyql.append("o.").append(key).append(" ").append(
                        orderby.get(key)).append(",");
            }
            orderbyql.deleteCharAt(orderbyql.length() - 1);
        }
        return orderbyql.toString();
    }

	/**
     * 得到Count聚合查询的聚合字段,既是主键列
     * @param <T>
     *              实体类型
     * @param clazz    
     *              实体类
     * @param alias
     *              表别名
     * @return
     *              聚合字段名(主键名)
     */

	public <T> String getPkField(Class<T> clazz, String alias) {
        String out = alias;
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector
                    .getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor propertydesc : propertyDescriptors) {
                Method method = propertydesc.getReadMethod();
                if (method != null && method.isAnnotationPresent(Id.class)) {
                    out = alias
                            + "."
                            + propertydesc.getName();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
	
	public Query setQueryParams(Query query, Object queryParams) {
        if (queryParams != null) {
            if (queryParams instanceof Object[]) {
                Object[] params = (Object[]) queryParams;
                if (params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i + 1, params[i]);
                    }
                }
            } else if (queryParams instanceof Map) {
                Map params = (Map) queryParams;
                Iterator<String> it = params.keySet().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    query.setParameter(key, params.get(key));
                }
            }
        }
        return query;
    }

	/**
     * 将集合中的字符串拼接成为SQL语句中 in的形式 'aaa','bbb','ccc'
     */

	public String toSQLIn(Collection<String> values){
        if(values == null || values.isEmpty())
            return null;
         
        String[] strvalues = new String[0];
        strvalues = (String[]) values.toArray(new String[values.size()]);
         
        return toSQLIn(strvalues);
    }
	
	public String toSQLIn(String[] values){
        StringBuffer bf_sqlin = new StringBuffer();
        if(values == null || values.length == 0)
            return null;
         
        int len = values.length;
        for(int i = 0 ; i < len ; i++){
            bf_sqlin = bf_sqlin.append(", '").append(values[i]).append("' ");
        }
        String str_sqlin = bf_sqlin.substring(1).toString();
         
        return str_sqlin;
    }


}
