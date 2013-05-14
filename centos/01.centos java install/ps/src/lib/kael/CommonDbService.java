package lib.kael;

import java.util.List;
import java.util.Map;

public interface CommonDbService {
	public List<Map> Query(Map param);
	public void    Update(Map param);
	public Integer Insert(Map param);
	public void    Delete(Map param);
//	public void    Replace(Map param);
	public Integer Count(Map param);
}
