package lib.kael
{
	public class DataLocation
	{
		public var _isPlayMusic:Boolean = false;
		public var _cacheList:Object = {};		//保存加载过的图片 bitmap		(单纯的IMAGE)
		private static var instance:DataLocation = null;
		
		public static const videos:Array = [
			1,2,3
		];
		
		public static const bookContent:Array = [
			{},
			{id:2005,desc:"",title:"相识"},
			{id:2006,desc:"",title:""}
		];

		public function DataLocation()
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
