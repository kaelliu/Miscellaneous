package lib.kael;

public class ServerConstant
{
	public final static boolean isRobotTest = false;
//	public final static boolean isOnLine = false;//����ʱʹ��
	public final static boolean isOnLine = true;//����ʱ����  �� ��Ӧlinux �� jar�ļ� 
	
	public final static int   SOCKET_BUFFER 		= 8192;
	
	public final static byte  DATA_LEN_MAIN_CMD		= 2;
	public final static byte  DATA_LEN_SUB_CMD		= 4;
	public final static short PORT_FLASH_SECURITY   = 843;
	
	public final static byte  DATA_LEN_BYTES 		= 1;
	public final static byte  DATA_LEN_SHORT	    = 2;
	public final static byte  DATA_LEN_FLOAT	    = 4;
	public final static byte  DATA_LEN_LONG		    = 4;
	public final static byte  DATA_LEN_INTEGER      = 4;
	public final static byte  DATA_LEN_UTF_GAP		= 2;
	
	/**
	 *  1.spring config ,use spring for manage class,databasesource,mybatis session,sessionfactory,map
	 *  2.netty config
	 *    |- 1.OrderedMemoryAwareThreadPoolExecutor for connection pool.
	 *    |- 2.serverhandler for eventhandler,message receive,send need decoder,encoder
	 *    |- 3.logic part integrate with service layer
	 *    |- 4.data buffer manipulate.need byte package,the format of package:|head(main_command,sub_commane)|version,size_of_package|databuffer|
	 *  3.spring and mybatis config, in applicationContext.xml file,read properties from mysql.properties for database configuration,add some class from mybatis
	 *  4.mybatis config
	 *    |- 1.use proxool for database connection pool,in datasource config:org.logicalcobwebs.proxool.ProxoolDataSource,and ProxoolDataSourceFactory
	 *       | - ps.proxool datasource have max connection count configuration .etc
	 *    |- 2.base way for database work.
	 *    	|- 1)make xxDao interface and xxDaoMapper.xml file for database work
	 *    	|- 2)make xxService interface for data work,xxServiceImpl for detail work,you can call beanfactory.findbean("xxService) for it
	 *    	|- 3)make map for Dao and DaoMapper file,use org.mybatis.spring.mapper.MapperFactoryBean,set mapperInterface with xxDao ,and set factory
	 *    	|- 4)make xxService in bean file
	 *    |- 3.database work -> call procedure,just use <select> staff,make input parameterType for input parameter.pay attention for some different between ibatis,see UserDaoMap.xml for more
	 *    |- 4.page work use OffsetLimitInterceptor for set database use limit Statement like select * from xxx limit page,limition
	 *    |- 5.Fuzzy matching can use string like %xxx% direct input,or 'xxx' input and '%${xxx}%' in map file,or #{xxx} for complete word.
	 *    	|- ps.${xxx} for string replacement,#{xxx} for variable replacement
	 *    |- 6.sql mapper file -> <select>,<insert>,resultMap,parameterMap,and so on,specially resultMap have case Indetifier for different result
	 *    |- 7.string input seem have some problem in ubuntu.
	 *    |- 8.I use mapper by org.mybatis.spring.mapper.MapperFactoryBean directly .if need sqlsession for list query,need xxServiceImpl to implements SqlSessionDaoSupport,
	 *    	 and you can use this.getSqlSession() get session to do the work,you also need to add property for your xxService bean ,the sqlSessionTemplate refer to sqlSession.
	 *    |- 9.dynamic sql query,use <where></where> expression,see guide for more information.
	 *    |- 10.for mybatis setting special,to open lazyLoading,need cglib
	 *    |- 11.about the batch working.(also need to add)
	 *    |- 12.about sql statement exception.need to handle exception each query,do exception handle,or is there a Convenient way?
	 *   5.log4j staff.set configuration file for log every sql sentence
	 *   6.use lib total:log4j,commons-dbcp,commons-pool,commons-logging,spring-framework(aop,context,aspects,asm,beans,core,expression,jdbc,transaction),proxool,mysql-connector,mybatis
	 *     mybatis-spring,netty,jackson-json-lib
	 *   7.use jackson-json.lib instead of json.lib  
	 *   8.sql session,baseTransactionProxy for use,make key word for transaction such as insert,update,if failed,make rollback.all handler class 's function 
	 *     have functionName contains keyword involve transaction process.
	 *   9.export as a runnable jar,for server running,nohup java -jar xxx.jar
	 *   10.memcached for sql cache is in future version.  
	 *     |- <!-- flushCache="true" is default if you want change a select statement flush,set the flushCache to true -->
	 *     |- left join 's result will not effect
	 *     |- if you run in windows,download the memcached windows version,then you can use cmd to open and run install as a service -> check
	 *        http://www.codeforest.net/how-to-install-memcached-on-windows-machine for more
	 *     |- if you run in linux,see manual for more detail
	 *     |- use memcached service for database's cache,java_memcached as client ,default select cached is true,if you do not need it ,set it to false 
	 *     |- notice that if memcached service is down,the whole server will be slow,this must find the solution
	 *   11.transaction's cost,just do insert/update/delete inside transation,use updater for this layer,take the logic to top,this will reduce transaction's cost
	 *      use transation only in key operation,too long time compute will make mysql 's transaction exception;  
	 *   12.about batch operation,if you want use batch,use sqlsessionTemplate in your class,use spring for injection,and then you can use sqlsessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,fals)
	 *      for make a session,then parse your mapper,such as if you use roledao,mapper = sqlsession.getMapper(roleDao.class) for it.you will use this dao for operation next.make a big number for commit.
	 *      after operation,you should close the session and clear the cache->sqlsession.clearCache().
	 *      p.s.in mybatis configuration file,we can use REUSE for defaultExecutorType?
	 *   13.to close the connection in right way ,you should add the all client channel to some group,then unbind the channel,channel.unbind();
	 *      next we close the channel , channel.close(),final call the factory's function releaseExternalResources();
	 *   14.want to pass list as param to query,parameterType="java.util.List" ,then tag foreach ,most important part is collection,if differnet param sent in,
	 *      if pass a list,just leave "list",see mybatis manual for more detail 
	 **/
}