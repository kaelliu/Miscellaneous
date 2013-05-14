package com.kael.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lib.kael.ProtocolBase;

public class OperateResultProtocol extends ProtocolBase
{
	/**
	 * 
	 */
	private int res_code;
	public OperateResultProtocol(int resCode,boolean success)
	{
		res_code=resCode;
		this.Cmd = (short) (ProtocolMatch.MAIN_CMD_OPERATE_RESULT + (success?1:2));
	}
	@Override
	public byte[] to_pure_bytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
			dos.writeShort(Cmd);
			dos.writeInt(res_code);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	@Override
	public boolean from_pure_bytes(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		DataInputStream dis = new DataInputStream(bais);
		try {
			Cmd = dis.readShort();
			res_code = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public int getRes_code() {
		return res_code;
	}
	
	public void setRes_code(int res_code) {
		this.res_code = res_code;
	}
	
}