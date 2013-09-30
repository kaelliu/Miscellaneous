import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPServer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DatagramSocket serverSocket = new DatagramSocket(9876);
		byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        {
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	String sentence = new String( receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            System.out.println("RECEIVED: " + IPAddress.getHostAddress() + ":" +port+
  					", data:"+sentence);
            String clientSocketInfo = IPAddress.getHostAddress() + ":" +port;
            sendData = clientSocketInfo.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
	}

}
