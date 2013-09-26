
public class ItemMemoryData extends MemoryData{
	public int id;
	public int roleid;
	public int place;
	public int amount;
	public int itemid;
	// save modified column in 
	public byte modifiedColumn[];
	@Override
	public void doPersisit() {
		// 
		if(status == MemoryData.I){
			System.out.println("persist item on create:"+id);
		}else if(status == MemoryData.D){
			System.out.println("persist item on delete:"+id);
		}else if(status == MemoryData.U){
			System.out.println("persist item on update:"+id);
		}else if(status == MemoryData.NONE){
			System.out.println("it just exist in memory in a period:"+id);
		}
	}
	
}
