package lib.kael;

import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;

public class MemcachedCache implements Cache
{
	// the read write lock
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private String id; 
	private LinkedList<String> cacheKeys = new LinkedList<String>();
	private MemCached mc = MemCached.getInstance();
	public MemcachedCache(String id)
	{
		this.id = id;
	}
	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getSize() {
		return cacheKeys.size();
	}

	// 设置数据至缓存中 
	@Override
	public void putObject(Object key, Object value) {
		String cacheKay = String.valueOf(key.hashCode());
		if(!cacheKeys.contains(cacheKay))
		{
			cacheKeys.add(cacheKay);
		}
		mc.add(cacheKay, value);
	}

	// 根据 key 从缓存中获取数据  
	@Override
	public Object getObject(Object key) {
		String cacheKey = String.valueOf(key.hashCode());
		Object value = mc.get(cacheKey);
		if(!cacheKeys.contains(cacheKey))
		{
			cacheKeys.add(cacheKey);
		}
		return value;
	}

	public boolean hasKey(Object key) {
		return cacheKeys.contains(key);
	}

	// 从缓存中删除指定 key 数据  
	@Override
	public Object removeObject(Object key) {
		String cacheKey = String.valueOf(key.hashCode()); 
		cacheKeys.remove(cacheKey);
		return mc.remove(cacheKey);
	}

	//清空当前 Cache 实例中的所有缓存数据  
	@Override
	public void clear() {
		for (int i = 0; i < cacheKeys.size(); i++){  
			String cacheKey = cacheKeys.get(i);  
			mc.remove(cacheKey);
		}
		cacheKeys.clear();
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}
}