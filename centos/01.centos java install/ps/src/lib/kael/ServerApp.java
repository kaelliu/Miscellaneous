package lib.kael;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerApp
{
	protected static ServerApp _instance = null;
	public static ApplicationContext m_context;
	static Logger logger = Logger.getLogger(ServerApp.class.getName());
	private LogicHandler _logic;
	private ConnectionHandler _conn;
	public ConnectionHandler get_conn() {
		return _conn;
	}
	public void set_conn(ConnectionHandler _conn) {
		this._conn = _conn;
	}
	protected ServerApp()
	{
		try{
			if(ServerConstant.isOnLine){
				m_context = new ClassPathXmlApplicationContext(getRealClassPath(new String[]{"applicationContext-datas.xml",
						"applicationContext-service.xml",
						"applicationContext-trans.xml",
						"applicationContext-timer.xml",
						"applicationContext-toplogic.xml",
						"applicationContext-cache.xml"}));
				PropertyConfigurator.configure("log4j.properties");
			}else{
				m_context = new  ClassPathXmlApplicationContext("applicationContext-*.xml");
				PropertyConfigurator.configure("log4j.properties");
			}
			logger.info("start");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String[] getRealClassPath(String[] locationfile)
    {
       String[] result = locationfile;
           for(int i = 0 ; i < locationfile.length; i++)
           {
              try
              {
                  URL url = Thread.currentThread().getClass().getResource(locationfile[i]);
                  String file = url.getFile();
                  if (file.indexOf(".jar!") > 0)
                     result[i] = new StringBuffer("jar:").append(file.substring(0,file.indexOf(".jar!")+".jar!".length()))
                            .append(locationfile[i]).toString();
              }
              catch(Exception ex)
              {}
           }
       return result;
    }
	public LogicHandler get_logic() {
		return _logic;
	}
	public void set_logic(LogicHandler _logic) {
		this._logic = _logic;
	}
	public void init()
	{}
	synchronized public static ServerApp getInstance()
	{
		if(_instance == null)
		{
			_instance = new ServerApp();
		}
		return _instance;
	}
}