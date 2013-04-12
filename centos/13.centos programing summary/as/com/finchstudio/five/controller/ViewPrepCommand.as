package com.finchstudio.five.controller
{
	import com.finchstudio.five.constants.CommandNotification;
	import com.finchstudio.five.datapool.Settings;
	import com.finchstudio.five.view.mediator.ApplicationMediator;
	
	import flash.display.Stage;
	
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.command.SimpleCommand;
	
	public class ViewPrepCommand extends SimpleCommand
	{
		override public function execute(notification:INotification):void
		{
			var stage:Stage = (notification.getBody() as Five).stage;
			var appMediator:ApplicationMediator = new ApplicationMediator(stage);
			facade.registerMediator(appMediator);	
		}
	}
}
