package com.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.kael.PServer;
import com.kael.datastruct.ItemDto;
import com.kael.datastruct.RoleDto;

import lib.kael.ModelBase;
import lib.kael.TimeSyncDbObject;

public class RoleModel extends ModelBase{
	private final static String modelIdentity = "r";
	// by calling getRoles for get all role belong to me
	public Map GetMyAllItem(Object key,Object extra){
		String ukey = "u"+key;
//		String uext = modelIdentity+extra; 
		// test module use i for item
		Object obj = this._cacheOperater.GetElements(ukey, "i", ItemDto.class);
		if(obj == null){
			// not in cache or memory,query database
			Map param = new HashMap();
			param.put("tbl", "item");
			param.put("fields", "*");
			param.put("conditions", "rid="+extra);
			
			List<?> arr = this._dbOperater.Query(param);
			for(int i=0;i<arr.size();++i){
				ItemDto id = new ItemDto();
				id.setterFromMap((Map<?, ?>) arr.get(i));
				this._cacheOperater.PutOne(ukey, "i", (Integer) id.getId(), id);
			}
			obj = this._cacheOperater.GetElements(ukey, "i", ItemDto.class);
		}
		return (Map) obj;
	}
	
	// by calling getModel("role").GetObject(1,3) for user 1's role 3
	@Override
	public Object GetObject(Object key, Object extra) {
		String ukey = "u"+key;
//		String uext = "r"+extra; 
		Object obj = this._cacheOperater.GetSingle(ukey, modelIdentity, (Integer) extra, RoleDto.class);
		if(obj == null){
			Map param = new HashMap();
			param.put("tbl", "role");
			param.put("fields", "*");
			param.put("conditions", "rid="+extra);
			List<?> arr = this._dbOperater.Query(param);
			if(arr.size()>0)
				obj = arr.get(0);
			if(obj!=null){
				RoleDto rd = new RoleDto();
				rd.setterFromMap((Map<?, ?>) obj);
				this._cacheOperater.PutOne(ukey, modelIdentity, (Integer) extra, rd);
				return rd;
			}
		}
		return obj;
	}

	// modify not null means is update or it means insert
	@Override
	public Object PutObject(Object key, Object extra, Object value, Map Modify) {
		String ukey = "u"+key;
//		String uext = "r"+extra; 
		String finalKey = ukey+modelIdentity+extra;
		TimeSyncDbObject tsdo = null;
		// arrange the gather queue
		if(PServer.gatherRotation.contains(finalKey)){
			tsdo = PServer.gatherRotation.get(finalKey);
		}else{
			tsdo = new TimeSyncDbObject();
			tsdo.type = TimeSyncDbObject.I;
			tsdo.data = new HashMap();
			tsdo.data.put("tbl", "role");
			PServer.gatherRotation.put(finalKey, tsdo);
		}
		if(Modify == null){
			// not exist now,insert
			RoleDto rd = (RoleDto)value;
			tsdo.data.put("fields", "(ROLEX,ROLEY,ROLEHP,ROLENAME)");
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(rd.getRolex());
			sb.append(",");
			sb.append(rd.getRoley());
			sb.append(",");
			sb.append(rd.getRolehp());
			sb.append(",'");
			sb.append(rd.getRolename());
			sb.append("')");
			tsdo.data.put("values", sb.toString());
			this._cacheOperater.PutOne(ukey, modelIdentity, (Integer) extra, value);
		}else{
			// if exist already,update
			tsdo.type = TimeSyncDbObject.U;
			RoleDto rd = (RoleDto)value;
			StringBuilder sb = new StringBuilder();
			Set entrys = Modify.entrySet();
			{
				Iterator it = entrys.iterator();
				while(it.hasNext()) 
				{
					Entry entry = (Entry) it.next();
					sb.append(entry.getKey());
					boolean isstr = entry.getValue() instanceof String;
					if(isstr)
						sb.append("='");
					else
						sb.append("=");
					sb.append(entry.getValue());
					if(isstr)
						sb.append("',");
					else
						sb.append(",");
				}
			}
			// trim last comma
			tsdo.data.put("data", sb.substring(0, sb.length()-1));
			tsdo.data.put("condition", "id="+rd.getId());
			// just update cache here,object value is a reference of memory model 
			this._cacheOperater.getCacheInterface().putObject(finalKey, value);
		}
		return value;
	}

	@Override
	public void DeleteObject(Object key, Object extra) {
		String ukey = "u"+key;
//		String uext = "r"+extra; 
		String finalKey = ukey+modelIdentity+extra;
		TimeSyncDbObject tsdo = null;
		if(PServer.gatherRotation.contains(finalKey)){
			tsdo = PServer.gatherRotation.get(finalKey);
		}else{
			tsdo = new TimeSyncDbObject();
			tsdo.type = TimeSyncDbObject.D;
			tsdo.data = new HashMap();
			tsdo.data.put("tbl", "role");
			PServer.gatherRotation.put(finalKey, tsdo);
		}
		tsdo.data.put("condition", "id="+extra);
		this._cacheOperater.DeleteKeyElements((String)key, modelIdentity, (Integer) extra);
	} 
}
