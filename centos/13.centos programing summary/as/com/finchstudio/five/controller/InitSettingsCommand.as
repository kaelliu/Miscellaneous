package com.finchstudio.five.controller
{
	import com.finchstudio.five.datapool.Settings;
	
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.command.SimpleCommand;
	
	/**
	 * 加载flash参数及基本配置信息  
	 * @author kael 
	 * 
	 */
	public class InitSettingsCommand extends SimpleCommand
	{
		override public function execute(notification:INotification):void
		{
			var sprite:Five = notification.getBody() as Five;
			var params:Object = sprite.loaderInfo.parameters;
			Settings.getInstance().convert(params);
		}
		
	}
}
