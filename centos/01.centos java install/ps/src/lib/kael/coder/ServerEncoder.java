package lib.kael.coder;

import lib.kael.ProtocolBase;
import lib.kael.util.CipherUtil;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;


public class ServerEncoder extends OneToOneEncoder
{

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel ch,
			Object obj) throws Exception {
		ProtocolBase res = (ProtocolBase)obj;
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer();//
        byte [] data = CipherUtil.encryptBuffer(res.toBytes());
        int dataLength = data.length;
        buf.writeShort(dataLength);
        buf.writeBytes(data);
		return buf;
	}
}