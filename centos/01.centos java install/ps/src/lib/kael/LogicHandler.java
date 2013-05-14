package lib.kael;

import org.jboss.netty.channel.Channel;

public interface LogicHandler
{
	public boolean handle(Object data,Channel ch);
}