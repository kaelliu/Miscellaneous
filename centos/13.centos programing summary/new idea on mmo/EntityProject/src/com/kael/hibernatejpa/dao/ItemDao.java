package com.kael.hibernatejpa.dao;

import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;

public class ItemDao extends BaseJpaDao{
	
	public ItemDao(){
	}
	
	@Override
	public <T extends BaseDto> QueryResult<T> gePageData(Class<T> entityClass,
			int firstindex, int maxresult, String wherejpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseDto> QueryResult<T> gePageData(Class<T> entityClass,
			int firstindex, int maxresult, String wherejpql,
			List<Object> queryParams, LinkedHashMap<String, String> orderby) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return entityManager;
	}

}
