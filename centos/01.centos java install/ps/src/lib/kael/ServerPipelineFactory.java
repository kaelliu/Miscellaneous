package lib.kael;

import lib.kael.coder.ServerDecoder;
import lib.kael.coder.ServerEncoder;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

public class ServerPipelineFactory implements ChannelPipelineFactory
{
	private final ExecutionHandler executionHandler;
	private int MAX_CONNECT;
	// netty timer
	private Timer timer;
	public ServerPipelineFactory(ExecutionHandler eh,int maxconn)
	{
		this.executionHandler = eh;
		this.MAX_CONNECT = maxconn;
		// timer init
		timer = new HashedWheelTimer();
	}
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// 
		if(MAX_CONNECT == 0)
		{
			throw new Exception("End of server");
		}
		ChannelPipeline pipeline = Channels.pipeline();
		// heartbeat detect for connect for link broken by power off or netlink off
		// timeout and heartbeat option set 300seconds heartbeat package for check client channel is idle
		// if 15minute in idle,we close the connection
		// IdleStateHandler is class in netty jar package
		//pipeline.addLast("timeout", new IdleStateHandler(timer, 300, 300, 0));
		//implement of interface IdleStateAwareChannelHandler
		//pipeline.addLast("hearbeat", new HeartBeat());
		pipeline.addLast("encoder",new ServerEncoder());
		pipeline.addLast("decoder", new ServerDecoder());
		pipeline.addLast("pipelineExecutor", executionHandler);
		pipeline.addLast("handler", new ServerHandler());
		return pipeline;
	}
}