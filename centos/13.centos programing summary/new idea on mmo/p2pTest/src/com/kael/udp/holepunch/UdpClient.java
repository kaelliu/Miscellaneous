package com.kael.udp.holepunch;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public /*abstract*/ class UdpClient {
	private final Long timeout = 300000L;
	private HashMap<String,InetAddressCls> p2pAddresses = null;
//	private String localAddresses = null;
	private SocketAddress serverAddress = null;
	private Long lastMessageTime = null;
	private Long id = null;
	private boolean udpHolePunchingCheck = false;
	private DatagramSocket client;
	private UdpClientTimeEvent cte ;
	private boolean exit = false;
	private short status=-1;
	private boolean isChief=false;
	public static void main(String[] args) throws IOException{
		new UdpClient("127.0.0.1",12345);
	}
	
	public UdpClient(){
		cte = new UdpClientTimeEvent(this);
	}
	
	public UdpClient(String hostname,int port) throws SocketException{
		serverAddress = new InetSocketAddress(hostname, port);
		client = new DatagramSocket();
		p2pAddresses = new HashMap<String,InetAddressCls>();
		cte = new UdpClientTimeEvent(this);
		cte.start();
	}
	
	// register to server route table
	public void regist() throws IOException{
		byte[] sendbuf = new byte[1];
		// 1 - regist
		sendbuf[0] = 1;
        sendData(sendbuf,serverAddress);
        byte[] buf = new byte[32];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        client.receive(packet);
        // regist success;
	}
	
	// room architecture 
	public void getRoomPlayerList(){
		
	}
	
	public void onRoomPlayerIn(){
		
	}
	
	public void onRoomPlayerLeave(){
		
	}
	
	public void onGetRoomStatus(){
		
	}
	
	public void joinRoom(){
		
	}
	// room architecture over
	
	public void requestCommunicateWithPeer(SocketAddress target) throws IOException{
//		p2pAddresses
		String data = new String("current time in:"+client.getLocalAddress().getHostAddress()+" is:"+System.currentTimeMillis());
		byte[] buf = data.getBytes();
		sendData(buf,target);
	}
	
	public void scheduledJob() throws IOException{
		receiveloop();
	}
	
	protected void receiveloop() throws IOException{
		for (;;) {
			if(status == -1)// no add
			{
				sendData("add".getBytes(),serverAddress);
				status = 1;
			}else if (status == 1){
				// send to hole end point peer!
//				sendData("hello".getBytes(),serverAddress);
				Set entrys = p2pAddresses.entrySet();
				{
					Iterator it = entrys.iterator();
					while(it.hasNext()) 
					{
						Entry entry = (Entry) it.next();
						InetAddressCls cls = (InetAddressCls) entry.getValue();
						SocketAddress addr = new InetSocketAddress(cls.naddr, Integer.parseInt(cls.port.trim()));
						String content = "4:helloto:"+cls.naddr+":"+cls.port;
						sendData(content.getBytes(),addr);
					}
				}
				if(isChief){
					status = 3;// send over enter ping pong mode
				}else{
					status = 4;
				}
			}else if(status == 3){
				Set entrys = p2pAddresses.entrySet();
				{
					Iterator it = entrys.iterator();
					while(it.hasNext()) 
					{
						Entry entry = (Entry) it.next();
						InetAddressCls cls = (InetAddressCls) entry.getValue();
						SocketAddress addr = new InetSocketAddress(cls.naddr, Integer.parseInt(cls.port.trim()));
						String content = "6:ping!";
						sendData(content.getBytes(),addr);
					}
				}
				status = 4;
			}else if(status == 4){
				
			}
			byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            client.receive(packet);
            handleData(packet);
		}
	}
	
	public void sendData(byte[] sendbuf,SocketAddress target) throws IOException{
		DatagramPacket pack = new DatagramPacket(sendbuf, sendbuf.length, target);
        client.send(pack);
	}
	
	public void handleData(DatagramPacket packet){
		String message = new String(packet.getData());
		String [] msgs = message.split(":");
		int cmd = Integer.parseInt(msgs[0]);
		if(cmd == 2){
			InetAddressCls cls = new InetAddressCls();
			cls.naddr = msgs[1];
			cls.port = msgs[2];
			System.out.println("len:" + msgs.length);
			if(msgs.length>3){
				isChief = true;
			}
			p2pAddresses.put(msgs[1]+":"+msgs[2], cls);
			
		}else if(cmd == 3){
			p2pAddresses.remove(msgs[1]+":"+msgs[2]);
		}else if(cmd == 6){
			status = 3;
			System.out.println("ping," + message);
		}
		else{
			System.out.println("receive from other peer:" + message);
		}
        //获取接收到请问内容后并取到地址与端口,然后用获取到地址与端口回复内容
	}
//	public abstract void handleData(DatagramPacket packet);
}
