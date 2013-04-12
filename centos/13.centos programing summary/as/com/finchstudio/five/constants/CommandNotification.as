package com.finchstudio.five.constants
{
	/**
	 * command相关的notification名称 
	 * @author kael 
	 * 
	 */
	public class CommandNotification
	{
		public static const STARTUP:String = "startup";
		public static const WARENTER:String = "enterwar";
		public static const WARINIT:String = "startwar";
		public static const WARACTION:String = "waraction";// manual change skill sequence
		public static const WAROVER:String = "warover";
		public static const WARLOADED:String = "warloaded";
		public static const WARREADYFORNEXT:String = "warready4next";
		public function CommandNotification()
		{
		}
	}
}
