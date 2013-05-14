package lib.kael;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class RemoteAddressBasedOMATPE extends OrderedMemoryAwareThreadPoolExecutor{

	public RemoteAddressBasedOMATPE(int corePoolSize,
			long maxChannelMemorySize, long maxTotalMemorySize) {
		super(corePoolSize, maxChannelMemorySize, maxTotalMemorySize);
		// 
	}

	@Override
    protected ConcurrentHashMap<Object, Executor> newChildExecutorMap() {
        // The default implementation returns a special ConcurrentMap that
        // uses identity comparison only (see IdentityHashMap).
        // Because SocketAddress does not work with identity comparison,
        // we need to employ more generic implementation.
        return new ConcurrentHashMap<Object, Executor>();
    }

    protected Object getChildExecutorKey(ChannelEvent e) {
        // Use the IP of the remote peer as a key.
        return ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();
    }

    // Make public so that you can call from anywhere.
    public boolean removeChildExecutor(Object key) {
       return super.removeChildExecutor(key);
    }
}
