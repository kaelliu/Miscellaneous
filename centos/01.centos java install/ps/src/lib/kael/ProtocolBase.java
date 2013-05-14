package lib.kael;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

//import net.sf.json.JSONObject;

// the package entity
public class ProtocolBase
{
	/**
	 * 
	 */
	protected short Cmd;
	public short getCmd() {
		return Cmd;
	}
	public void setCmd(short cmd) {
		Cmd = cmd;
	}

	// to pure byte you can implement this to make your data
	public byte[] to_pure_bytes()
	{
		return null;
	}
	public boolean from_pure_bytes(byte[] b)
	{
		return false;
	}
	// use json make data to byte array
	public byte[] toBytes()
	{
//		JSONObject jsonObject = JSONObject.fromObject(this);
//		return jsonObject.toString().getBytes();
		
		try {
			return PojoMapper.toJson(this, false).getBytes();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	
	}
	public Object fromBytes(byte[] b)
	{
//		JSONObject jsonObject = JSONObject.fromObject(new String(b));
//		return JSONObject.toBean(jsonObject,this.getClass());
		
		try {
			return PojoMapper.fromJson(new String(b), this.getClass());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
	
//	public Object fromJObj(JSONObject jb)
//	{
//		return JSONObject.toBean(jb,this.getClass());
//	}
	
	public Object fromJObj(String str)//JSONObject jb)
	{
		try {
			return PojoMapper.fromJson(str, this.getClass());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
}