package lib.kael;

import org.springframework.context.ApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
// no use,just the interface of jedis pool
public class JedisClientManager {
	protected static JedisClientManager _instance = null;
	private JedisPool pool;
	public JedisClientManager(ApplicationContext app)
	{
		pool = (JedisPool)app.getBean("jedisPool");
	}
	
	public JedisClientManager()
	{
		pool = (JedisPool)ServerApp.m_context.getBean("jedisPool");
	}
	
	public Jedis getJedisClientPeer()
	{
		return pool.getResource();
	}
	
	public void returnResource(Jedis resource){
		pool.returnResource(resource);
	}
	
	public static JedisClientManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new JedisClientManager();
		}
		return _instance;
	}
}
