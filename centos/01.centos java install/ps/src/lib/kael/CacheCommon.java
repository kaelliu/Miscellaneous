package lib.kael;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheCommon {
	public void putObject(Object key, Object value);
	public Object getObject(Object key,Class<?> cls);
	public boolean hasKey(Object key);
	public Object removeObject(Object key);
	public void clear();
	public boolean hasKeyLike(Object key);
	public List<Object> getMObject(Set<?> key,Class<?> cls);
	public Map<?, ?> getMObjectLike(Object key,Class<?> cls);
	//
	public String handleInData(Object src);
	public Object handleOutData(String src,Class<?> cls);
}
