package lib.kael;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class TcpNetworkEngine
{
	public static int MAX_CONNECT = 2048;
	private ChannelGroup channelGroup ;
	private ExecutionHandler executionHandler;
	private static final NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(
			Executors.newCachedThreadPool(),
			Executors.newCachedThreadPool(),Runtime.getRuntime().availableProcessors()*2+1);// to avoid memory leak
	public TcpNetworkEngine(int port)
	{
		// 2 * cpu_count + 1 worker thread 
//		factory = 
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		channelGroup = new DefaultChannelGroup(
				TcpNetworkEngine.class.getName());
		executionHandler = new ExecutionHandler(
		           new OrderedMemoryAwareThreadPoolExecutor(512, 1048576, 1073741824));//,TimeUnit,Executors.defaultThreadFactory()));
		bootstrap.setPipelineFactory(new ServerPipelineFactory(executionHandler,MAX_CONNECT));
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("child.reuseAddress", true);
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