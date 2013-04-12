package com.finchstudio.five
{
	import com.finchstudio.five.constants.CommandNotification;
	import com.finchstudio.five.controller.StartWarCommand;
	import com.finchstudio.five.controller.StartupCommand;
	
	import flash.display.Stage;
	
	import org.puremvc.as3.patterns.facade.Facade;
	
	public class ApplicationFacade extends Facade
	{
		
		public static function getInstance():ApplicationFacade
		{
			if ( instance == null ) instance = new ApplicationFacade();
			return instance as ApplicationFacade;
		}
		
		override protected function initializeController( ) : void
		{
			super.initializeController();
			registerCommand(CommandNotification.STARTUP, StartupCommand);
			registerCommand(CommandNotification.WARENTER, StartWarCommand);
		}
		
		public function startup(sprite:Five):void
		{
			sendNotification(CommandNotification.STARTUP, sprite);
		}
	}
}
