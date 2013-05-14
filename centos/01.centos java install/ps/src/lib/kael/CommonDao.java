package lib.kael;

import java.util.List;
import java.util.Map;

public interface CommonDao {
	// String fields,String condition,String tbl,String limit,String orderBy
	public List<Map> Query(Map<?, ?> param);
	// String tbl,String data,String condition
	// return row affect to do
	public void    Update(Map<?, ?> param);
	// String tbl,String fields,String value,String onDuplicate
	// return insert id
	public Integer Insert(Map<?, ?> param);
	// String tbl,String condition,String limit
	public void    Delete(Map<?, ?> param);	
	// String tbl,String condition
	// return count
	// select count(1) as c from table
	public Integer Count(Map<?, ?> param);
	
	// to do
//	public void    InsertAll(Map param);
	// need primary key
	// String tbl,String fields,String value
	public void    Replace(Map<?, ?> param);
}
