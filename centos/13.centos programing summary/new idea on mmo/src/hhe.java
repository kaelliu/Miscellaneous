import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

//import redis.clients.jedis.Jedis;


public class hhe {
	public static ConcurrentHashMap<Integer, RoleModel> users = new ConcurrentHashMap<Integer, RoleModel>();
	// update people
			// share memory update when user login
	public static	int serverPeopleCount = 0;
			// update interval
	public static	final int updateInterval = 600000;// ten minute
	public static	final int threadInterval = 100;// ms
			// update from
	public static	int currentUpdateCursor = 0;
	public static	int needUpdateUserThisTime = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Jedis jedis = new Jedis("127.0.0.1");
//		String keys = "hello";  
//		// 删数据  
//		jedis.del(keys);  
//		// 存数据  
//		jedis.set(keys, "snowolf");  
//		// 取数据  
//		String value = jedis.get(keys);
//		System.out.println(value);  
		
		RoleModel rm = new RoleModel();
		rm.readFromDatabase();
		users.put(rm.rmd.roleid, rm);
		hhe.serverPeopleCount = hhe.serverPeopleCount+1;
		
		Timer _testTimer = new Timer();
		_testTimer.schedule(new SimulateTimer(), 0,100);
		
//		for(int i=0;i<rm.imds.size();++i){
//			System.out.println("place:"+rm.imds.get(i).place+" value:"+rm.imds.get(i).itemid);
//		}
//		rm.onGetItem(123, 2);
//		for(int i=0;i<rm.imds.size();++i){
//			System.out.println("place:"+rm.imds.get(i).place+" value:"+rm.imds.get(i).itemid);
//		}
//		rm.onUseItem(3, 1);
//		rm.onUseItem(3, 1);
//		rm.onUseItem(3, 1);
//		for(int i=0;i<rm.imds.size();++i){
//			System.out.println("place:"+rm.imds.get(i).place+" value:"+rm.imds.get(i).itemid);
//		}
//		rm.onGetItem(124, 2);
//		for(int i=0;i<rm.imds.size();++i){
//			System.out.println("place:"+rm.imds.get(i).place+" value:"+rm.imds.get(i).itemid);
//		}
//		rm.onUseItem(3, 1);
//		rm.onUseItem(3, 1);
//		for(int i=0;i<rm.imds.size();++i){
//			System.out.println("place:"+rm.imds.get(i).place+" value:"+rm.imds.get(i).itemid);
//		}
//		// role operation simulate
//		
//		for(int i=0;i<rm.modifyedModule.length;++i){
//			{
//				Iterator it = rm.modifyedModule[i].iterator();
//				while(it.hasNext()) 
//				{
//					MemoryData data = (MemoryData) it.next();
//					data.doPersisit();
//				}
//			}
//		}
		int b=0xbad;
		
	}
}
