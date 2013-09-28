package com.kael.hibernatejpa.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import com.kael.hibernatejpa.model.ModelBase;

/**
 * Base DAO Abstract implements CRUD operations of Model Objects
 * 
 * Each DAO object needs to maintain it's own reference to 
 *       entity manager which has a 1-1 attachment to a model
 */
public abstract class AbstractDao implements BaseDao{
	// Entity Manager Factory
	protected static EntityManagerFactory entityManagerFactory;

	// The Entity Manager Instance attached to the model to be managed
	protected EntityManager entityManager;
	   
	// The POJO to manage persistence for
	protected ModelBase model;
	
	/**
    * Class Constructor [Empty]
    *       For finding and returning existing records and record sets
    */
   public AbstractDao()
   {
      init();
   }
   
   /**
    * Class Constructor
    * @param model   The User model to manage persistence for
    */
   public AbstractDao(ModelBase model) 
   {
      this.model = model;
      init();
   }
   
   /**
    * Initialize Dao
    */
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
   
   /**
    * Get base entity manager
    * @return entityManager
    */
   protected EntityManager getEntityManager()
   {
      return entityManager;
   }
   
   /**
    * Get Model MUST be overridden in child class
    *    model field in base needs to be hidden
    * @return
    */
   abstract public ModelBase getModel();
   
   /**
    * Insert a record in the database from provided model
    * 
    * @throws DaoException
    */
   @Override
   public void create() throws DaoException 
   {
      startTxn();
      try 
      {
         entityManager.persist( model );
         entityManager.getTransaction().commit();
      } 
      catch(EntityExistsException e) 
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Create Failed: " + e.getMessage());
      } 
      catch(IllegalArgumentException e)
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Create Failed: " + e.getMessage());
      } 
      catch(TransactionRequiredException e) 
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Create Failed: " + e.getMessage());
      }
   }
   
   /**
    * Update a record in the database from provided model
    * 
    * @throws DaoException
    */
   @Override
   public void update() throws DaoException 
   {
      
      startTxn();
      try 
      {
         entityManager.flush();
         entityManager.getTransaction().commit();
      }
      catch(TransactionRequiredException e) 
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Update Failed: " + e.getMessage());
      }
      catch(PersistenceException e)
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Update Failed: " + e.getMessage());
      } 
      catch(Exception e)
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Update Failed: " + e.getMessage());
      } 
   }
   
   /**
    * Delete a record in the database matching attached model
    * 
    * @throws DaoException
    */
   @Override
   public void delete() throws DaoException 
   {
      startTxn();
      try
      {
         entityManager.remove( model );
         entityManager.getTransaction().commit();
      }
      catch(IllegalArgumentException e)
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Create Failed: " + e.getMessage());
      } 
      catch(TransactionRequiredException e) 
      {
         entityManager.getTransaction().rollback();
         e.printStackTrace();
         throw new DaoException("Update Failed: " + e.getMessage());
      }
   }
   
   /**
    * Start a database transaction
    * 
    * @throws DaoException
    */
   protected void startTxn() throws DaoException
   {
      try
      {
         entityManager.getTransaction().begin();
      }
      catch(IllegalStateException e)
      {
         throw new DaoException("Start Txn Failed: " + e.getMessage());
      }
   }
   
   /**
    * Find a Record in the Database matching id primary key
    * 
    * @param id
    * @return model
    * @throws DaoException
    */
   public ModelBase findById(Class<?> claz, Long id) throws DaoException
   {
      try
      {
         model = (ModelBase) entityManager.find( claz, id );
      }
      catch(IllegalStateException e)
      {
         throw new DaoException("Find By Id Failed: " + e.getMessage());
      }
      
      return model;
   }
   
   /**
    * Close the entity manager
    * This should be called when the object attached to the DAO 
    *  requires no more persistence management.
    */
   public void closeEntityManager()
   {
      entityManager.close();
   }
   
	@Override
	public Object doQuery(String queryStr) throws DaoException {
//		startTxn();
	    try
	    {
	    	Query query = entityManager.createQuery(queryStr);
	    	Object result = query.getResultList();
	    	return result;
	    }catch(Exception e){
	    	throw new DaoException("query failed: " + e.getMessage());
	    }
	}
	
}
