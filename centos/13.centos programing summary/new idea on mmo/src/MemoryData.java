
public abstract class MemoryData implements BaseMemoryData{
	public static final byte I=1;
	public static final byte U=2;
	public static final byte D=3;
	public static final byte NONE=0;
	protected byte status = NONE;
	// Initial status 
	/*
	 *  I -> U = I
	 *  I -> D = NONE
	 *  U -> I impossible
	 *  U -> D = D
	 *  D -> I = U // not implement yet
	 *  D -> U impossible
	 * */
//	public MemoryData(){
//		//System.out.println("object created.");
//	}
	
	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public void onCreate(){
		// called when object be generated in program,manually
		// when data model be generated,it's status is Insert 
		// make sure that Delete operation will not be present before Insert
		// if be delete before,but be create again,it should be update
		if(status == D){
			status = U;
		}else{
			status = I;
		}
	}
	
	public void onUpdate(){
		// called when setter be called
		// when it is insert already,next coming update will no change status
		if(status != I){
			status = U;
		}
	}
	
	public void onDeCreate(){
		// called when object be removed ,manually
		// when it is Need Insert state,Delete operation will be set back to none
		if(status == I){
			status = NONE;
		}else{
			status = D;
		}
	}
	
}
