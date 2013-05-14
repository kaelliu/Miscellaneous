package lib.kael;

import java.util.List;
import java.util.Map;

public class CommonDbServiceImpl implements CommonDbService{
	private CommonDao commonDao;
	
	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	@Override
	public List<Map> Query(Map param) {
		// TODO Auto-generated method stub
		return commonDao.Query(param);
	}

	@Override
	public void Update(Map param) {
		// TODO Auto-generated method stub
		commonDao.Update(param);
	}

	@Override
	public Integer Insert(Map param) {
		// TODO Auto-generated method stub
		return commonDao.Insert(param);
	}

	@Override
	public void Delete(Map param) {
		// TODO Auto-generated method stub
		commonDao.Delete(param);
	}

	@Override
	public Integer Count(Map param) {
		// TODO Auto-generated method stub
		return commonDao.Count(param);
	}

}
