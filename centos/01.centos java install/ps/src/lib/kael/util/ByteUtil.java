package lib.kael.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;

public class ByteUtil
{
	public static byte[] getBytes(Object obj) throws IOException {  
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();  
	        ObjectOutputStream out = new ObjectOutputStream(bout);  
	        out.writeObject(obj);  
	        out.flush();  
	        byte[] bytes = bout.toByteArray();  
	        bout.close();  
	        out.close();  
	        return bytes;  
	}
	public static Object getObject(byte[] bytes)throws IOException, ClassNotFoundException{
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(bi);
		Object obj = oi.readObject();
		bi.close();
		oi.close();
		return obj;
	}
	public static ByteBuffer getByteBuffer(Object obj)throws IOException
	{
		byte[] bytes = ByteUtil.getBytes(obj);
		ByteBuffer buff = ByteBuffer.wrap(bytes);
		return buff;
	}
	public static Object getObject(ByteBuffer byteBuffer)throws ClassNotFoundException, IOException {  
        //
		;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		for (int i = 0; i < byteBuffer.capacity(); i++) {  
            byteBuffer.position(i);  
            buffer.writeByte(byteBuffer.get());
        }  
		ChannelBufferInputStream input = new  ChannelBufferInputStream(buffer);
        ObjectInputStream oi = new ObjectInputStream(input);  
        Object obj = oi.readObject();  
        input.close();  
        oi.close();  
        return obj;  
	}
}