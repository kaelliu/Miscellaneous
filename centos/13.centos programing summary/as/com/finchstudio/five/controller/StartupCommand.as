package com.finchstudio.five.controller
{
	import org.puremvc.as3.patterns.command.MacroCommand;
	
	public class StartupCommand extends MacroCommand
	{
		override protected function initializeMacroCommand() :void
		{
			addSubCommand(InitSettingsCommand);
			addSubCommand(ModelPrepCommand);
			addSubCommand(ViewPrepCommand);			
		}
	}
}
