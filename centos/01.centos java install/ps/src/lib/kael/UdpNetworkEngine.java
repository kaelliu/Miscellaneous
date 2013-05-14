package lib.kael;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class UdpNetworkEngine
{
	public static int MAX_CONNECT = 1024;
	private ChannelGroup channelGroup ;
	private ExecutionHandler executionHandler;
	private DatagramChannelFactory factory;
	public UdpNetworkEngine(int port)
	{
		factory = 
				new NioDatagramChannelFactory(
						Executors.newCachedThreadPool(),Runtime.getRuntime().availableProcessors()*2+1);
		ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);
		ChannelGroup channelGroup = new DefaultChannelGroup(
				UdpNetworkEngine.class.getName());
		executionHandler = new ExecutionHandler(
		           new OrderedMemoryAwareThreadPoolExecutor(200, 1048576, 1073741824));//,TimeUnit,Executors.defaultThreadFactory()));
		bootstrap.setPipelineFactory(new ServerPipelineFactory(executionHandler,MAX_CONNECT));
		
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("broadcast", "false");
		bootstrap.setOption("sendBufferSize", 65536);
		bootstrap.setOption("receiveBufferSize", 65536);
		
		Channel channel = bootstrap.bind(new InetSocketAddress(port));
		channelGroup.add(channel);
	}
	/**
	 * conclude the server 
	 * 
	 */
	public void conclude()
	{
		channelGroup.close();
		executionHandler.releaseExternalResources();
		factory.releaseExternalResources();
	}
	public void restart()
	{}
}