package lib.kael
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Loader;
	import flash.display.PixelSnapping;
	import flash.display.Sprite;
	import flash.events.Event;
	
	public class MImage extends Sprite
	{
		private var _file:File;
		public var url:String;
		private var onComplete:Function;
		public static var errorImage:BitmapData = new BitmapData(1, 1, true, 0);
		public static var loadClass:Class = Sprite;
		private static var _cacheList:Object = {};
		private var _path:String = "";
		
		public function MImage(path:String, addLoadSpt:Boolean = false, callBack:Function=null,callBackLater:Boolean = false,nHitArea:Boolean=false)
		{
			_path = path;
			if(!callBackLater)
				load(path,addLoadSpt,callBack);
		}
		
		public function load(path:String, addLoadSpt:Boolean = false, callBack:Function=null):void
		{
			_cacheList = DataLocation.getInstance()._cacheList;
			url = path;
			onComplete = callBack;
			if (_cacheList[path])
			{
				if(this.numChildren!=0)
				{
					for(var i:int = 0;i<this.numChildren;++i)
					{
						this.removeChild(this.getChildAt(i));
					}
				}
				addChild(new Bitmap(_cacheList[path], PixelSnapping.AUTO, true));
				runComplete();
			}
			else
			{
				_file = new File();
				_file.onComplete = completeHandler;
//				_file.onError = ioErrorHandler;
				_file.load(path);
				if (addLoadSpt == true)
				{
					addChild(new loadClass());
				}
			}
		}
		
		private function ioErrorHandler() : void
		{
			clear();
			addChild(new Bitmap(errorImage));
			runComplete();
			return;
		}
		
		private function completeHandler() : void
		{
			var loadContent:Object = null;
			clear();
			var loader:Loader = _file.loader;
			
			if(loader.content == null)
				return;
			
			addChild(loader.content);
			
			loadContent = loader.content;
			//_cacheList[url] = (loadContent as Bitmap).bitmapData.clone();
			(loadContent as Bitmap).smoothing = true;
//			(loadContent as Bitmap).bitmapData.transparent = true;
			
			runComplete();
		}
		
		private function runComplete() : void
		{
			if (onComplete is Function == false)
			{
				return;
			}
			if (onComplete.length == 0)
			{
				onComplete();
			}
			else
			{
				onComplete(this);
			}
		}
		private function onAddedToStage(event:Event):void
		{ 
			removeEventListener(Event.ADDED_TO_STAGE, onAddedToStage);
			addEventListener(Event.REMOVED_FROM_STAGE, onRemovedFromStage);
		}
		
		private function onRemovedFromStage(event:Event):void
		{
			removeEventListener(Event.REMOVED_FROM_STAGE, onRemovedFromStage);
			if(this.getChildAt(0) != null && this.getChildAt(0) is Bitmap)
			{
				(this.getChildAt(0) as Bitmap).bitmapData.dispose();
				delete _cacheList[_path];
			}
			releaseMe();
			clear();
		}
		
		public function releaseMe():void
		{
			
		}
		
		private function clear() : void
		{
			while (numChildren)
			{
				removeChildAt(0);
			}
		}
		
	}
}