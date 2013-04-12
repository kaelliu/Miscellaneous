package com.finchstudio.five.datapool
{
	import mx.core.Singleton;

	/**
	 * 保存flash参数及基本配置信息 
	 * @author kael
	 * 
	 */
	public class Settings
	{
		
		private static var _instance:Settings;
		public function Settings(singleton:SingletonConstraint)
		{
//			throw new Error("call getInstance instead");
		}
		
		public static function getInstance():Settings{
			if(_instance == null){
				_instance = new Settings(new SingletonConstraint());
			}
			return _instance;
		}
		
		public function convert(params:Object):void{
			
		}
	}
}
class SingletonConstraint{
	
}
