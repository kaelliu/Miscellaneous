package lib.kael.database;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.logicalcobwebs.proxool.ProxoolDataSource;

public class ProxoolDataSourceFactory implements DataSourceFactory{
	protected ProxoolDataSource dataSource;
	
	public ProxoolDataSourceFactory() 
	{
		this.dataSource = new ProxoolDataSource();     
	}
	
	@Override
	public DataSource getDataSource() {
		// 
		return this.dataSource;
	}

	@Override
	public void setProperties(Properties properties) {
		// 
		dataSource.setDriver(properties.getProperty("driver"));
		dataSource.setDriver(properties.getProperty("driverUrl"));
		dataSource.setDriver(properties.getProperty("user"));
		dataSource.setDriver(properties.getProperty("password"));
		 //线程保持休眠时间，house keeper负责检查所有连接的状态，并测试是否需要销毁或者创建，默认时间为30秒
        if(properties.containsKey("houseKeepingSleepTime")){
            dataSource.setHouseKeepingSleepTime(Integer.parseInt(properties.get("houseKeepingSleepTime").toString()));
        }
        //如果house keep 线程发现空闲的连接，他会测试使用这个sql进行测试，这个语句应该快速的被执行。像查询日期的sql语句。
        if(properties.containsKey("houseKeepingTestSql")){
            dataSource.setHouseKeepingTestSql(properties.get("houseKeepingTestSql").toString());
        }
        //最大线程活动时间。
        //如果housekeeper 遇到一个线程活动时间超过定义的时间，将会终止这个线程。
        //所以你需要设置这个时间大于预计最慢响应的时间(默认时间是5分钟)。
        if(properties.containsKey("maximumActiveTime")){
            dataSource.setMaximumActiveTime(Integer.parseInt(properties.get("maximumActiveTime").toString()));
        }
        //数据库最大连接数（默认值为15）
        if(properties.containsKey("maximumConnectionCount")){
            dataSource.setMaximumConnectionCount(Integer.parseInt(properties.get("maximumConnectionCount").toString()));
        }
        //一个连接存在的最长保持活动的时间。默认值是4小时，单位是毫秒。
        if(properties.containsKey("maximumConnectionLifetime")){
            dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties.get("maximumConnectionLifetime").toString()));
        }
        //最小连接保持打开的个数，不管是否需要，默认值是5个。
        if(properties.containsKey("minimumConnectionCount")){
            dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties.get("minimumConnectionCount").toString()));
        }
        //这个帮助我们确定连接池的状态。如果在这个时间阀值内（单位为毫秒）拒绝了一个连接，就认为是过载了。默认时间60秒。
        if(properties.containsKey("overloadWithoutRefusalLifetime")){
            dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties.get("overloadWithoutRefusalLifetime").toString()));
        }      
	} 
	
}