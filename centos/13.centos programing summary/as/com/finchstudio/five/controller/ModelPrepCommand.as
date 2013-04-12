package com.finchstudio.five.controller
{
	import com.finchstudio.five.model.proxy.WarProxy;
	
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.command.SimpleCommand;
	
	public class ModelPrepCommand extends SimpleCommand
	{
		override public function execute(notification:INotification):void
		{
			//register proxy
			//facade.registerProxy(new WarProxy(WarProxy.NAME));
		}
	}
}
