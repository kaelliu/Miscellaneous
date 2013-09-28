package com.kael.hibernatejpa.dao;

import java.util.List;

import javax.persistence.Query;

import com.kael.hibernatejpa.model.ActorItem;
import com.kael.hibernatejpa.model.ModelBase;

public class ActorItemDao extends AbstractDao{

	public ActorItemDao(ActorItem userModel) 
    {
       super(userModel);
       this.model = (ActorItem) model;
    }
	@Override
	public ModelBase getModel() {
		return (ActorItem) model;
	}

	@Override
	public ModelBase findById(Long id) throws DaoException {
		try
        {
           model = (ActorItem) super.findById(ActorItem.class, id);
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
		return (ActorItem) model;
	}
	
	public List findUsersItem(Long id){
		String queryStr = "from ActorItem as a where rid="+id;
		Query query = entityManager.createQuery(queryStr);
    	List result = query.getResultList();
    	return result;
	}
}
