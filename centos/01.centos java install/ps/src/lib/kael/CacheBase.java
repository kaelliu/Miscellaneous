package lib.kael;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CacheBase {
	// should init by inheir,data saved in memory
	// <primary key,<sub key,<id,object>...>...>
	private ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>>> values;
	private CacheCommon cacheInterface;
	public CacheCommon getCacheInterface() {
		return cacheInterface;
	}

	public void setCacheInterface(CacheCommon cacheInterface) {
		this.cacheInterface = cacheInterface;
	}

	public CacheBase(){
		// like u1 : r : 1 :roleDto; u1 : i : 1 : itemDto,u1 : h : 1 : heroDto...
		values = new ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>>>();
	}
	
	public Object GetElements(String key,String element,Class cls){
		if(values.containsKey(key)){
			if(values.get(key).containsKey(element)){
				return values.get(key).get(element);
			}
		}
		ConcurrentHashMap<Integer,Object> datas = (ConcurrentHashMap<Integer, Object>) cacheInterface.getMObjectLike(key+element+"*", cls);
		if(datas.size() > 0){
			ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>> ob;
			if(values.containsKey(key)){
				ob = values.get(key);
			}else{
				ob = new ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>>();
				values.put(key, ob);
			}
			ob.put(element, datas);
			return datas;
		}
		return null;
	}
	
	public Object GetSingle(String key,String element,int id,Class cls){
		if(values.containsKey(key)){
			if(values.get(key).containsKey(element)){
				return values.get(key).get(element).get(id);
			}
		}
		if(cacheInterface.hasKey(key+element+id)){
			Object value = cacheInterface.getObject(key+element+id,cls);
			ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>> ob = new ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>>();
			ConcurrentHashMap<Integer,Object> obinner = new ConcurrentHashMap<Integer,Object>();
			obinner.put(id, value);
			ob.put(element, obinner);
			values.put(key, ob);
			return value;
		}
		return null;
	}
	
	public Object GetMutil(String key,String element,List<Integer> keys,Class cls){
		if(keys == null)
			return null;
		if(keys.size() == 0)
			return null;
		Map<String,Object> result = new HashMap<String,Object>();
		for(int i=0;i<keys.size();++i){
			String finalkey = key + element + keys.get(i);
			Object ob = GetSingle(key, element ,keys.get(i),cls);
			if(ob!=null){
				result.put(finalkey, ob);
			}
		}
		return result;
	}
	
	public void DeleteKey(String key){
		values.remove(key);
		cacheInterface.removeObject(key);
	}
	
	public void DeleteKeyElements(String key,String element,Integer id){
		if(values.containsKey(key)){
			if(values.get(key).containsKey(element)){
				values.get(key).get(element).remove(id);
			}
		}
		cacheInterface.removeObject(key+element+id);
	}
	
	public void PutOne(String key,String element,Integer id,Object value){
		if(values.containsKey(key)){
			if(values.get(key).containsKey(element)){
				values.get(key).get(element).put(id, value);
			}else{
				ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>> ob = new ConcurrentHashMap<String,ConcurrentHashMap<Integer,Object>>();
				ConcurrentHashMap<Integer,Object> obinner = new ConcurrentHashMap<Integer,Object>();
				obinner.put(id, value);
				ob.put(element, obinner);
				values.put(key, ob);
			}
		}
		cacheInterface.putObject(key+element+id, value);
	}
	
	public void PutAll(String key,String element,Map<Integer,Object> value){
		Set entrys = value.entrySet();
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				PutOne(key, element,(Integer) entry.getKey(), entry.getValue());
			}
		}
	}
}
