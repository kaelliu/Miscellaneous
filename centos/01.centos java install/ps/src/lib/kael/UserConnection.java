package lib.kael;

public class UserConnection
{
	private int     uid;// 平台ID
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	private int 	rid;/* 连接的用户ID*/
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	private int 	x;  /* 连接的用户所在X*/
	private int 	y;  /* 连接的用户所在Y*/
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	private long  	latency;/* 客户端本地时间和服务器本地时间的差值 */
	//private Channel channel;/* 客户端连接*/
	public UserConnection()
	{}
	public long getLatency() {
		return latency;
	}
	public void setLatency(long latency) {
		this.latency = latency;
	}
}