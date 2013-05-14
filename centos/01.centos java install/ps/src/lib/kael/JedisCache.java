package lib.kael;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import redis.clients.jedis.Jedis;

public class JedisCache implements CacheCommon{

	@Override
	public void putObject(Object key, Object value) {
		Jedis peer = JedisClientManager.getInstance().getJedisClientPeer();
		peer.set((String) key, handleInData(value));
	}

	@Override
	public Object getObject(Object key,Class<?> cls) {
		Jedis peer = JedisClientManager.getInstance().getJedisClientPeer();
		return handleOutData(peer.get((String) key),cls);
	}

	@Override
	public boolean hasKey(Object key) {
		Jedis peer = JedisClientManager.getInstance().getJedisClientPeer();
		return peer.exists((String) key);
	}

	@Override
	public Object removeObject(Object key) {
		Jedis peer = JedisClientManager.getInstance().getJedisClientPeer();
		String[] keys = new String[1];
		keys[0]=(String) key;
		peer.del(keys);
		return null;
	}

	@Override
	public void clear() {
			
	}

	@Override
	public String handleInData(Object src) {
		// TODO Auto-generated method stub
		try {
			return PojoMapper.toJson(src,false);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object handleOutData(String src,Class<?> cls) {
		// TODO Auto-generated method stub
		try {
			return PojoMapper.fromJson(src, cls);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean hasKeyLike(Object key) {
		Jedis peer = JedisClientManager.getInstance().getJedisClientPeer();
		Set<?> keys = peer.keys((String) key);
		return keys.size()>0;
	}

	@Override
	public List<Object> getMObject(Set<?> key,Class<?> cls) {
		List<Object> result = new LinkedList<Object>();
		Object[] keys = key.toArray();
		for(int i=0;i<keys.length;++i){
			result.add(getObject(keys[i],cls));
		}
		return result;
	}

	@Override
	public Map<Integer, Object> getMObjectLike(Object key,Class<?> cls) {
		Jedis peer = JedisClientManager.getInstance().getJedisClientPeer();
		String strkey = (String) key;
		Set<?> keys = peer.keys(strkey);
		Map<Integer, Object> result = new ConcurrentHashMap<Integer,Object>();
		Object[] keysObject = keys.toArray();
		String spliter = strkey.substring(0,strkey.indexOf("*"));
		int prefixlen = spliter.length();
		for(int i=0;i<keysObject.length;++i){
			String keysingle = (String) keysObject[i];
			Integer id = Integer.parseInt(keysingle.substring(prefixlen));
			if(id!=0)
				result.put(id,getObject(keysingle,cls));
		}
		return result;
	}
}
