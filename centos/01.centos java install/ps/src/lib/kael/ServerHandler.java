package lib.kael;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ServerHandler extends SimpleChannelHandler 
{	
	//public ServerHandler()
	//{
		
	//}
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) 
	{
		// no right
		if (!(e.getMessage() instanceof String)) {
            return ;
        }
		boolean result = ServerApp.getInstance().get_logic().handle(e.getMessage(),e.getChannel());
		if(result == false)
		{
			// shutdown the connect ?
		}
	}
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		ServerApp.getInstance().get_conn().handleConnect(ctx.getChannel());
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) 
	{
		ServerApp.getInstance().get_conn().handleDisConnect(ctx.getChannel());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) 
	{
		
	}
}