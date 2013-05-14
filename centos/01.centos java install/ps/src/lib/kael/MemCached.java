package lib.kael;
import java.util.Date;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemCached
{
	// 创建实例
	protected static MemCachedClient mcc = new MemCachedClient();
	protected static MemCached mc = new MemCached();
	// 设置与缓存服务器的连接池
	static
	{
		String[] servers={"127.0.0.1:11211"};
		Integer[] weights ={3};
		// 获取连接池实体对象
		SockIOPool pool = SockIOPool.getInstance();
		
		// 设置服务器信息
		pool.setServers(servers);
		pool.setWeights(weights);
		// 设置初始连接数，最小连接数，最大连接数，最大处理时间
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaxIdle(1000 * 60 * 60 * 6);
		// 设置主线程睡眠时间
		pool.setMaintSleep(30);
		
		// 设置TCP参数
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(0);
		// 初始化连接池
		pool.initialize();
		
		// 压缩设置，超过指定大小K的都会被压缩
//		mcc.setCompressEnable(true);
//		mcc.setCompressThreshold(64);
	}
	// 单例
	protected MemCached()
	{}
	public static MemCached getInstance()
	{
		return mc;
	}
	// 添加一个指定的值到缓存中
	public boolean add(String key,Object value)
	{
		return mcc.add(key, value);
	}
	public boolean add(String key,Object value,Date expiry)
	{
		return mcc.add(key, value,expiry);
	}
	// 替换
	public boolean replace(String key,Object value)
	{
		return mcc.replace(key, value);
	}
	public boolean replace(String key,Object value,Date expiry)
	{
		return mcc.replace(key, value,expiry);
	}
	// 删除	
	public Object remove(String key)
	{
		Object value = mcc.get(key);
		if(mcc.delete(key))
			return value;
		else
			return null;
	}
	// 获取对象
	public Object get(String key)
	{
		return mcc.get(key);
	}
}