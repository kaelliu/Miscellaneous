package lib.kael;

import org.jboss.netty.channel.Channel;

public interface ConnectionHandler
{
	public boolean handleConnect(Channel ch);
	public boolean handleDisConnect(Channel ch);
}