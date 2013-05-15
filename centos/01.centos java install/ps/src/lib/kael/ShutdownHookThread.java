package lib.kael;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/*
 * class for add server down event hooker,but not kill -9 or taskkill
 * if need use class signal and signal handler,you need put
 * windows -> preferences -> java -> compiler -> errors/warnings
 * -> deprecated and restricted api -> forbidden reference -> to warning,not error
 * in eclipse.
 * */
public class ShutdownHookThread extends Thread{
	private boolean hooked = false; 
	private ArrayList<ServerInterface> servers = new ArrayList<ServerInterface>(); 
	private void createShutdownHook(){ 
		if(!hooked){
			try {
				Method shutdownHook = java.lang.Runtime.class.getMethod("addShutdownHook", 
						new Class[]{java.lang.Thread.class});
				shutdownHook.invoke(Runtime.getRuntime(), new Object[]{this});
				hooked = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	/** 
     * Add Server to servers list. 
     */  
    public boolean add(ServerInterface server){  
        createShutdownHook();  
        return this.servers.add(server);  
    }  

    /** 
     * Contains Server in servers list? 
     */  
    public boolean contains(ServerInterface server){  
        return this.servers.contains(server);  
    }  
    
    /** 
     * Append all Servers from Collection 
     */  
    public boolean addAll(Collection<ServerInterface> c){  
        createShutdownHook();  
        return this.servers.addAll(c);  
    }  

    /** 
     * Clear list of Servers. 
     */  
    public void clear(){  
        createShutdownHook();  
        this.servers.clear();  
    }  
    
    /** 
     * Remove Server from list. 
     */  
    public boolean remove(ServerInterface server){  
        createShutdownHook();  
        return this.servers.remove(server);  
    }  
    
    
	
	public void run(){
		setName("Shutdown");
		System.out.println("shutdown hook executing");
		Iterator it = servers.iterator();  
        while (it.hasNext()){
        	ServerInterface svr = (ServerInterface) it.next();
        	if (svr == null)  
                continue; 
        	try  
            {  
                svr.stop();  
            }  
            catch (Exception e)  
            {  
                e.printStackTrace();
            }  
        	System.out.println("server stop over"); 
        	// Try to avoid JVM crash  
            try  
            {  
                Thread.sleep(1000);  
            }  
            catch (Exception e)  
            {  
            	System.out.println("error occur");   
            }  
        }  
	}  
}
