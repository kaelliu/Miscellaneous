package lib.kael;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;


public class HeartBeat extends IdleStateAwareChannelHandler{
	 private int i = 0;
	 @Override
	 public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception
	 {
		  super.channelIdle(ctx, e);  
		  if(e.getState() == IdleState.WRITER_IDLE)
			  i++;
		  // 15 min do nothing,close the connection
		  if(i==3){
			  e.getChannel().close();
			  System.out.println("be shutdown¡£");
		  }
	 }
}
