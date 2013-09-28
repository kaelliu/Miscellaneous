package com.kael.hibernatejpa.dao;

import java.util.Collection;

import com.kael.hibernatejpa.model.Actor;
import com.kael.hibernatejpa.model.ModelBase;

/**
 * User DAO: Maintains Persistence between model and database
 * Based on Hibernate JPA
 */
public class ActorDao extends AbstractDao{

	/**
    * Class Constructor [Empty] 
    */
    public ActorDao() 
    {
      super();
    }
    /**
    * Class Constructor
    * @param model   The User model to manage persistence for
    */
    public ActorDao(Actor userModel) 
    {
       super(userModel);
       this.model = (Actor) model;
    }
   
	@Override
	public ModelBase findById(Long id) throws DaoException {
		try
        {
           model = (Actor) super.findById(Actor.class, id);
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
		return (Actor) model;
	}

	@Override
	public Actor getModel() {
		return (Actor) model;
	}

}
