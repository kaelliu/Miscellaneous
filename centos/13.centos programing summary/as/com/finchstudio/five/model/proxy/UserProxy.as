package com.finchstudio.five.model.proxy
{
	import org.puremvc.as3.interfaces.IProxy;
	import org.puremvc.as3.patterns.proxy.Proxy;
	
	public class UserProxy extends Proxy implements IProxy
	{
		public function UserProxy(proxyName:String=null, data:Object=null)
		{
			super(proxyName, data);
		}
	}
}
