package lib.kael.coder;

import lib.kael.ServerConstant;
import lib.kael.util.CipherUtil;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class ServerDecoder extends FrameDecoder
{

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel ch,
			ChannelBuffer cb) throws Exception {
		// 
//		System.out.println("recv bytes:"+cb.readableBytes());
		if(cb.readableBytes() < ServerConstant.DATA_LEN_BYTES)// first 2 byte is data length
		{
			return null;
		}
		short data_len = cb.getShort(cb.readerIndex());
		if(cb.readableBytes() < data_len) // next 2 bytes is main command ,and data goes
		{
			return null;
		}
		cb.readShort();//skip data length part
		byte []decoded = new byte[data_len];
		cb.readBytes(decoded);
		//cb.getBytes(cb.readerIndex(), decoded);
		CipherUtil.decryptBuffer(decoded);
		String result = new String(decoded);//System.out.println(result);
		return result;//cb.readBytes(data_len);// send data as buffer
	}
}