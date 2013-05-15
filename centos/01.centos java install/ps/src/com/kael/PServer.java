package com.kael;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import lib.kael.CacheBase;
import lib.kael.CacheCommon;
import lib.kael.CommonDao;
import lib.kael.ModelBase;
import lib.kael.ServerApp;
import lib.kael.ServerInterface;
import lib.kael.ShutdownHookThread;
import lib.kael.TcpNetworkEngine;
import lib.kael.TimeSyncDbObject;
import lib.kael.TimeSyncRunnable;

import org.jboss.netty.channel.Channel;

import com.data.GameRoomData;
import com.kael.datastruct.RoleDto;
import com.kael.timers.SimulateTimer;
import com.service.RoleModel;

public class PServer implements ServerInterface
{
	public static int PORT=3456;
	public static Map<Channel,GameRoomData> sessionDatas;		//����gameserver����
	public static Map<Integer,Channel> ridWithChannels;		    //����gameserver����
	public static Map<Integer,Channel> uidWithChannels;		    //����users����
	
	// memory only
	// rotation[primary key(u1r1)->[{type,u/i/d hash parameter},...],primary key...]
	// gathering -> query together in queue
	// how long for flush to database,here is 180second
	public static final int FlushDbTime = 60;
	// pointer for helper
	public static ConcurrentHashMap<String, TimeSyncDbObject> gatherRotation;
	public static ConcurrentHashMap<String, TimeSyncDbObject> queryingRotation;
	// really exist thing
	public static ConcurrentHashMap<String, TimeSyncDbObject> queue;
	public static ConcurrentHashMap<String, TimeSyncDbObject> backup;
	// controlling thread pool
	private static Executor executor;
	// memory only : 
	// cache + memory : user information
	// cache only : query other message but not online?
	// model entry
	public static ConcurrentHashMap<String,ModelBase> modelEntry;
	
//	public static Map<Integer,Channel> idWithChannel;			//�û���Ƶ��
	public static int POWEROONTIME = 0;
	public static AtomicInteger ids = new AtomicInteger();
	// important todo:
	// need add stop server command,to flush database before server close!
	// for testing
	private Timer _testTimer;
	public PServer()
	{
		ShutdownHookThread sht = new ShutdownHookThread();
        sht.add(this);
        this.start();
//		Map param = new HashMap();
//		param.put("tbl", "users");
//		param.put("fields", "*");
//		List<Map> abc = ((CommonDbService) ServerApp.getInstance().m_context.getBean("commonService")).Query(param);
//		long s1 = System.currentTimeMillis();
//		Random rand = new Random(s1);
//		UserTestDto utd = new UserTestDto();
//		Map abcd = abc.get(0);
//		for(int i=0;i<1000000;i++){
////			utd.setterFromMap(abc.get(rand.nextInt(abc.size())));
//			utd.setterFromMap(abcd);
//		}
//		
//		long s2 = System.currentTimeMillis();
//		System.out.println("cost:"+(s2-s1));
	}
	
	public static void flushToDb(){
		// if still querying...
		if(queryingRotation != null)
			return;
		// do query rotation
		queryingRotation = gatherRotation;
		// switch gather rotation
		if(gatherRotation == queue)
			gatherRotation = backup;
		else if(gatherRotation == backup)
			gatherRotation = queue;
		// this will block until work all finish,but if three minutes can not finish all work
		// if each operation is isolated,can use another thread pool for doing every operation,
		// but if every operation is related,or time sequence,will block in this one thread
		Set entrys = queryingRotation.entrySet();
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				TimeSyncDbObject todo = (TimeSyncDbObject) entry.getValue();
				// these will be block in one thread,seems not good
//				if(todo.type == TimeSyncDbObject.I){
//					((CommonDbService) ServerApp.getInstance().m_context.getBean("commonService")).Insert(todo.data);
//				}else if(todo.type == TimeSyncDbObject.D){
//					((CommonDbService) ServerApp.getInstance().m_context.getBean("commonService")).Delete(todo.data);
//				}else if(todo.type == TimeSyncDbObject.U){
//					((CommonDbService) ServerApp.getInstance().m_context.getBean("commonService")).Update(todo.data);
//				}
				// use executor for instead
				Runnable task = new TimeSyncRunnable(todo);
				executor.execute(task);
			}
		}
		
		// clear finished job
		queryingRotation.clear();
		// when finish queryingRotation should be null
		queryingRotation = null;
	}
	
	public static void main(String []args)
	{
		try{
			new PServer();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		System.out.println("step Do before server init");
		executor = Executors.newFixedThreadPool(28);
		ids.set(0);
		POWEROONTIME = (int) (System.currentTimeMillis()/1000);
		queue = new ConcurrentHashMap<String, TimeSyncDbObject>();
		backup = new ConcurrentHashMap<String, TimeSyncDbObject>();
		gatherRotation = queue;
		
		new TcpNetworkEngine(PORT);
		ServerApp.getInstance().set_logic(new GameServerLogicHandler());
		ServerApp.getInstance().set_conn(new GameConnectionHandler());
//		InputStream stream = null;
//
//		try {
//			if(ServerConstant.isOnLine){
//				stream = new FileInputStream("/usr/local/games/gameserver/staticData.jzzh");
//			}else{
//				stream = new FileInputStream(System.getProperty("user.dir")+"/bin/staticData.xls");
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		idWithChannel = new ConcurrentHashMap<Integer,Channel>();
		
		ServerApp.getInstance();
		uidWithChannels = new ConcurrentHashMap<Integer,Channel>();
		ridWithChannels = new ConcurrentHashMap<Integer,Channel>();
		sessionDatas = new ConcurrentHashMap<Channel,GameRoomData>();
		
		modelEntry = new ConcurrentHashMap<String, ModelBase>();
		RoleModel rm = new RoleModel();
		// can initial these in constructor if configure is constant
		CacheBase cb = new CacheBase();
		cb.setCacheInterface((CacheCommon) ServerApp.getInstance().m_context.getBean("jedisCacheFun"));
		rm.set_cacheOperater(cb);
		rm.set_dbOperater((CommonDao) ServerApp.getInstance().m_context.getBean("commonDao"));
		modelEntry.put("role", rm);
		
		RoleModel rm2 = (RoleModel) modelEntry.get("role");
		long s1 = System.currentTimeMillis();
		RoleDto rd = (RoleDto) rm2.GetObject(1, 1);
		long s2 = System.currentTimeMillis();
		System.out.println("cache role:"+rd.getRolename()+",cost:"+(s2-s1));
		
		s1 = System.currentTimeMillis();
		rd = (RoleDto) rm2.get_cacheOperater().getCacheInterface().getObject("u1r1", RoleDto.class);
		s2 = System.currentTimeMillis();
		System.out.println("cache after warm up role:"+rd.getRolename()+",cost:"+(s2-s1));
		
		s1 = System.currentTimeMillis();
		rd = (RoleDto) rm2.GetObject(1, 1);
		s2 = System.currentTimeMillis();
		System.out.println("memory role:"+rd.getRolename()+",cost:"+(s2-s1));
		
		s1 = System.currentTimeMillis();
		rm2.GetMyAllItem(1, 1);
		s2 = System.currentTimeMillis();
		System.out.println("db item cost:"+(s2-s1));
		
		s1 = System.currentTimeMillis();
		rm2.GetMyAllItem(1, 1);
		s2 = System.currentTimeMillis();
		System.out.println("cache item cost:"+(s2-s1));
		
		s1 = System.currentTimeMillis();
		rm2.GetMyAllItem(1, 1);
		s2 = System.currentTimeMillis();
		System.out.println("memory item cost:"+(s2-s1));
		
		// test updating timer
		if(_testTimer == null){
			_testTimer = new Timer();
		}
		_testTimer.schedule(new SimulateTimer(), 0,1000);
	}

	@Override
	public void stop() {
		// things do before stop server
		System.out.println("step Close tcp listening doing when close server..");
		System.out.println("step Flush db doing when close server..");
		System.out.println("step Clear Exist cache doing when close server..");
		flushToDb();
	}
}