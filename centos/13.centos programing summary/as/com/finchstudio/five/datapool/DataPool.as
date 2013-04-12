package com.finchstudio.five.datapool
{
	/**
	 * 数据池，集中存储所有用户数据 
	 * @author kael
	 * 
	 */
	public class DataPool
	{
		private static var _instance:DataPool;
		public function DataPool(singleton:SingletonConstraint)
		{
			throw new Error("call getInstance instead");
		}
		
		public static function getInstance():DataPool{
			if(_instance == null){
				_instance = new DataPool(new SingletonConstraint());
			}
			return _instance;
			
		}
	}
}
class SingletonConstraint{
	
}
