package com.kael.udp.holepunch;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class UdpServerX {
	private Map rooms;
	private final static byte maxRoom=100;
	private DatagramSocket server;
	public UdpServerX() throws IOException, InterruptedException{
		// init all rooms
		rooms = new ConcurrentHashMap<Integer, Room>();
		for(int i=0;i<maxRoom;i++){
			Room room = new Room();
			room.id = i+1;
			room.users = new ConcurrentHashMap<String,RoomUser>();
			rooms.put((i+1), room);
		}
		server = new DatagramSocket(12345);
		// 最好可以换其他线程
		doJob();
	}
	
	public void doJob() throws IOException, InterruptedException{
		for(;;){
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			server.receive(packet);
			String receiveMessage = new String(packet.getData(), 0, packet.getLength());
			System.out.println("start:"+receiveMessage);
			if (receiveMessage.contains("add")) {
				// 假定加入相同房间
				// info for other users in room
				RoomUser ru = new RoomUser();
				ru.natAddr = packet.getAddress();
				ru.natPort = packet.getPort();
//				packet.getAddress();
				Room room = (Room) rooms.get(1);
				StringBuilder sb = new StringBuilder();
				sb.append(ru.natAddr.getHostAddress()).append(":").append(ru.natPort);
				String key = sb.toString();
				String str = "2:" + key;
				String isChief = "";
				if(room.chiefUser == null){
					room.chiefUser = ru.natAddr.getHostAddress()+":"+ru.natPort;
					isChief = ":1";
				}
				Set entrys = room.users.entrySet();
				{
					Iterator it = entrys.iterator();
					while(it.hasNext()) 
					{
						Entry entry = (Entry) it.next();
						RoomUser ru_other = (com.kael.udp.holepunch.RoomUser) entry.getValue();
						String str2 = "2:"+ru_other.natAddr.getHostAddress()+":"+ru_other.natPort+isChief;
						// 2 for other added
						sendMsg(str,ru_other.natPort,ru_other.natAddr);
						sendMsg(str2,ru.natPort,ru.natAddr);
					}
				}
				room.users.put(key, ru);
				
			}else if (receiveMessage.contains("leave")) {
				Room room = (Room) rooms.get(1);
				StringBuilder sb = new StringBuilder();
				sb.append(packet.getAddress()).append(":").append(packet.getPort());
				String key = sb.toString();
				
				RoomUser ru = room.users.get(key);
				if(ru != null){
					Set entrys = room.users.entrySet();
					{
						Iterator it = entrys.iterator();
						while(it.hasNext()) 
						{
							Entry entry = (Entry) it.next();
							RoomUser ru_other = (com.kael.udp.holepunch.RoomUser) entry.getValue();
							String str = "3:"+ru.natAddr.getHostAddress()+":"+ru.natPort;
							sendMsg(str,ru_other.natPort,ru_other.natAddr);
						}
					}
					room.users.remove(key);
				}
				
			}else if (receiveMessage.contains("dowork")) {
//				Room room = (Room) rooms.get(1);
//				StringBuilder sb = new StringBuilder();
//				sb.append(packet.getAddress()).append(":").append(packet.getPort());
//				String key = sb.toString();
//				RoomUser ru = room.users.get(key);
//				if(ru != null){
//					for(int i=0;i<room.users.size();++i){
//						RoomUser ru_other = room.users.get(i);
//						// sync status
//						sendMsg(receiveMessage,ru_other.natPort,ru_other.natAddr);
//					}
//					room.users.remove(key);
//				}
			}
			System.out.println("收成功");
			Thread.sleep(1000);
		}
	}
	
	private void sendMsg(String message, int port, InetAddress address) {
        try {
            byte[] sendBuf = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, port);
            server.send(sendPacket);
            System.out.println("消息发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) throws IOException, InterruptedException {
		new UdpServerX();
	}
}
