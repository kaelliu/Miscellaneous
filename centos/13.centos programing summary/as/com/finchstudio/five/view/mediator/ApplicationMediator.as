package com.finchstudio.five.view.mediator
{
	import com.finchstudio.five.constants.CommandNotification;
	
	import flash.display.Stage;
	
	import org.puremvc.as3.interfaces.IMediator;
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.mediator.Mediator;
	
	public class ApplicationMediator extends Mediator implements IMediator
	{
		public static const NAME:String = "ApplicationMediator";
		public function ApplicationMediator(stage:Stage)
		{
			super(NAME, stage);
		}
		
		override public function onRegister():void
		{
			//初始化当前场景中需要用到的其他mediator
		}
		
		
		override public function listNotificationInterests():Array
		{
			return [
				
			];
		}
		
		override public function handleNotification(notification:INotification):void
		{
			// TODO Auto Generated method stub
			super.handleNotification(notification);
		}
		
		
		public function get stage():Stage{
			return viewComponent as Stage;
		}

	}
}
