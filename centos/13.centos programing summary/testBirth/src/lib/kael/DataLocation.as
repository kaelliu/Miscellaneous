package lib.kael
{
	public class DataLocation
	{
		public var _cacheList:Object = {};		//保存加载过的图片 bitmap		(单纯的IMAGE)
		private static var instance:DataLocation = null;
		
		public static const videos:Array = [
			1,2,3
		];
		
		protected function Datalocation()
		{
			if ( instance != null )
			{
				throw new Error("Singleton Error!", "DataLocator" );
			}
			instance = this;
		}
		
		public static function getInstance() : DataLocation
		{
			if ( instance == null )
			{
				instance = new DataLocation();
			}
			return instance;
		}
	}
}