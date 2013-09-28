package com.kael.hibernatejpa.dao;

import java.util.List;

import com.kael.hibernatejpa.model.ModelBase;

public interface BaseDao {
	/**
	    * Insert A Record in the database
	    * @throws DaoException
	    */
	   void create() throws DaoException;

	   /**
	    * Update A Record in the database
	    * @throws DaoException
	    */
	   void update() throws DaoException;

	   /**
	    * Delete A Record in the database
	    * @throws DaoException
	    */
	   void delete() throws DaoException;

	   /**
	    * Find a record by it's primary key
	    * @return Model   An instance implementation of the Model interface
	    * @throws DaoException
	    */
	   ModelBase findById(Long id) throws DaoException;
	   
	   /**
	    * operation by query string
	    */
	   Object doQuery(String query) throws DaoException;
	   
}
