package main.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//import com.kael.hibernatejpa.dao.ActorDao;
import com.kael.hibernatejpa.dao.ActorDao;
import com.kael.hibernatejpa.dao.DaoException;
import com.kael.hibernatejpa.dao.ItemDao;
import com.kael.hibernatejpa.model.Actor;
import com.kael.hibernatejpa.model.Item;
import com.kael.hibernatejpa.model.TestEntity;

public class Test {

	/**
	 * @param args
	 * you should put meta-inf to sfs2x folder
	 */
	public static void main(String[] args) {
		// Test code 01
//		Actor doubi = new Actor();
//		doubi.setExp(2);
//		doubi.setLevel((byte) 1);
//		doubi.setName("kaelliu");
//		
//		ActorDao aDao = new ActorDao( doubi );
//		try {
//			aDao.create();
//		}catch (DaoException e) {
//	         e.printStackTrace();
//	    }
		
		// test code 01
		ItemDao iDao = new ItemDao();
		Item itm = new Item();
		itm.setRid(2);
		itm.setName("soar's knife");
		itm.setGrade(7);
		Integer [] param = new Integer[1];
		param[0]=3;
		List<Item> result = (List<Item>) iDao.queryByWhere(Item.class, " grade > ?", param);
		System.out.println(result.get(0).getName());
		Item modify = result.get(0);
		modify.setName("soar's knife+5");
		try {
//			iDao.update(modify);
			Map update = new HashMap();
			update.put("name", modify.getName());
			update.put("grade", modify.getGrade()-1);
			iDao.updateDirectly("Item", update, " grade > 3");
//			iDao.create(itm);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		EntityManagerFactory factory = Persistence 
//	            .createEntityManagerFactory("sfs2x.hibernatejpa.project.model.jpa");  
//	    EntityManager entityManager = factory.createEntityManager();  
//	    TestEntity testEntity = new TestEntity();  
//	    testEntity.setStr1("ningnian");  
//	    entityManager.getTransaction().begin();  
//	    entityManager.persist(doubi);  
//	    entityManager.getTransaction().commit();  
	}

}
