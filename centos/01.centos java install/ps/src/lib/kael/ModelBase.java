package lib.kael;

import java.util.Map;

public abstract class ModelBase {
	protected CommonDao _dbOperater;
	protected CacheBase _cacheOperater;
	protected String _modelName;
	public CacheBase get_cacheOperater() {
		return _cacheOperater;
	}
	public void set_cacheOperater(CacheBase _cacheOperater) {
		this._cacheOperater = _cacheOperater;
	}
	
	public CommonDao get_dbOperater() {
		return _dbOperater;
	}
	public void set_dbOperater(CommonDao _dbOperater) {
		this._dbOperater = _dbOperater;
	}

	public String get_modelName() {
		return _modelName;
	}
	public void set_modelName(String _modelName) {
		this._modelName = _modelName;
	}
	
	public abstract Object GetObject(Object key,Object extra);
	public abstract Object PutObject(Object key,Object extra,Object value,Map modify);
	public abstract void DeleteObject(Object key,Object extra);
}
