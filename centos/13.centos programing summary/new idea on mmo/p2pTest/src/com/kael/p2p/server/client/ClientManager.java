package com.kael.p2p.server.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.kael.p2p.server.adapter.ServerAdapter;

public class ClientManager {
	private static Long seed = System.currentTimeMillis();
	private static ClientManager instance = null;

	private Map<String, Client> normalClients = null; 
	private Map<String, Client> systemClients = null; 
	private Map<String, Client> waitingClients = null; 
	private Set<Client> clientSet = null;
	private Map<String, Client> clients = null; 
	private Long connectCount = 0L;
	private int systemMaxCount = 50;
	private ServerAdapter adapter;

	private ClientManager() {
		normalClients = new ConcurrentHashMap<String, Client>();
		systemClients = new ConcurrentHashMap<String, Client>();
		waitingClients = new ConcurrentHashMap<String, Client>();
		clients = new WeakHashMap<String, Client>();
	}

	public static synchronized ClientManager getInstance() {
		if(instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	public void setAdapter(ServerAdapter adapter) {
		this.adapter = adapter;
	}
	public DatagramSocket getSocket() {
		return adapter.getSocket();
	}

	public Client getTargetClient(DatagramPacket packet) {
		
		Client client;
		//Gets the SocketAddress (usually IP address + port number) of the remote host that this packet is being sent to or is coming from.
		String packetKey = packet.getSocketAddress().toString();
		
		client = clients.get(packetKey);
		if(client != null) {
			return client;
		}
		client = new Client(packet, generateId(packet));
		clients.put(packetKey, client);
		connectCount ++;
		if(connectCount == Long.MAX_VALUE) {
			connectCount = 0L;
		}
		return client;
	}
	
	private Long generateId(DatagramPacket packet) {
		return seed + packet.getSocketAddress().hashCode();
	}
	
	public void pingJob() {
		Map<String, Client> nextClients = new ConcurrentHashMap<String, Client>();
		synchronized(normalClients) {
			for(Entry<String, Client> entry : normalClients.entrySet()) {
				if(entry.getValue().sendPing(adapter.getSocket())) {
					nextClients.put(entry.getKey(), entry.getValue());
				}
			}
		}
		normalClients = nextClients;
		nextClients = new ConcurrentHashMap<String, Client>();
		synchronized (systemClients) {
			for(Entry<String, Client> entry : systemClients.entrySet()) {
				if(entry.getValue().sendPing(adapter.getSocket())) {
					nextClients.put(entry.getKey(), entry.getValue());
				}
			}
		}
		systemClients = nextClients;
		nextClients = new ConcurrentHashMap<String, Client>();
		synchronized(waitingClients) {
			for(Entry<String, Client> entry : waitingClients.entrySet()) {
				if(entry.getValue().sendPing(adapter.getSocket())) {
					nextClients.put(entry.getKey(), entry.getValue());
				}
			}
		}
		waitingClients = nextClients;
	}
	
	public void setupClient(Client client) {
		setupClientMode(client);
		Long target = client.getTarget();
		if(target == -1) {
			doSystemClient(client);
			return;
		}
		if(target > 0L) {
			doTargetClient(client);
			return;
		}
		if(target == 0L) {
			doClient(client);
			return;
		}
	}
	
	private void doSystemClient(Client client) {
		systemClients.put(client.getAddressKey(), client);
		client.sendMode();
	}

	private void doTargetClient(Client client) {
		String key = null;
		Client target = waitingClients.get(client.getId().toString());
		if(target != null && target.getId() == client.getTarget()) {
			return;
		}
		for(Entry<String, Client> entry : normalClients.entrySet()) {
			if(entry.getValue().getId() == client.getTarget()) {
				key = entry.getKey();
				break;
			}
		}
		if(key != null) {
			target = normalClients.remove(key);
			return;
		}
		waitingClients.put(client.getTarget().toString(), client);
	}

	private void doClient(Client client) {
		Client target = waitingClients.get(client.getId().toString()); 
		if(target != null) {
			System.out.println("found the target with queue...");
			waitingClients.remove(target);
			doConnectWithClient(client, target);
			return;
		}
		target = null;
		System.out.println(normalClients.size());
		for(Entry<String, Client> entry : normalClients.entrySet()) {
			if(client == entry.getValue()) {
				System.out.println("that's myself....");
				continue;
			}
			System.out.println("found the target with clients list...");
			target = entry.getValue();
			break;
		}
		if(target == null) {
			System.out.println("not found put on queue...");
			normalClients.put(client.getAddressKey(), client);
			client.sendMode();
		}
		else {
			doConnectWithClient(client, target);
		}
	}

	private void doConnectWithClient(Client client, Client target) {
		client.setTarget(target.getId());
		target.setTarget(client.getId());
		client.sendMode();
		target.sendMode();
	}

	private void setupClientMode(Client client) {
		Long target = client.getTarget();
		if(target == null) {
			if(canBeSystemClient(client)) {
				client.setSystemClient();
			}
			else {
				client.setTarget(0L);
			}
			target = client.getTarget();
		}
		if(target == -1L) {
			normalClients.remove(client.getAddressKey());
			systemClients.put(client.getAddressKey(), client);
			return;
		}
		if(target > 0L) {
			normalClients.remove(client.getAddressKey());
			waitingClients.put(target.toString(), client);
		}
	}
	private boolean canBeSystemClient(Client client) {
		if(systemClients.size() >= systemMaxCount) {
			return false;
		}
		for(Entry<String, Client> element : systemClients.entrySet()) {
			if(element.getValue().getId().longValue() == client.getId().longValue()) {
				return false;
			}
		}
		return true;
	}
}
