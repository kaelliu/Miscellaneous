package lib.kael;

public class TimeSyncRunnable implements Runnable{
	private TimeSyncDbObject todo;
	
	public TimeSyncRunnable(TimeSyncDbObject obj){
		todo = obj;
	}
	
	@Override
	public void run() {
		if(todo.type == TimeSyncDbObject.I){
			((CommonDbService) ServerApp.getInstance().m_context.getBean("commonService")).Insert(todo.data);
		}else if(todo.type == TimeSyncDbObject.D){
			((CommonDbService) ServerApp.getInstance().m_context.getBean("commonService")).Delete(todo.data);
		}else if(todo.type == TimeSyncDbObject.U){
			((CommonDbService) ServerApp.getInstance().m_context.getBean("commonService")).Update(todo.data);
		}
		// set pointer to null
		todo = null;
	}
}
