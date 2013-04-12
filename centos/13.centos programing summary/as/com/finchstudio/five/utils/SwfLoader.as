package com.finchstudio.five.utils
{
	import flash.display.Loader;
	import flash.display.LoaderInfo;
	import flash.events.Event;
	import flash.net.URLRequest;

	public class SwfLoader
	{
		private var _callBack:Function = null;
		private var _loader:Loader = null;
		public function SwfLoader(callBack:Function,loader:Loader,pathing:String)
		{
			_callBack = callBack;
			_loader = loader;
			var temploader:Loader = new Loader();
			temploader.contentLoaderInfo.addEventListener(Event.COMPLETE,loaderEnd);
			temploader.load(new URLRequest(pathing));
		}
		
		private function loaderEnd(e:Event):void
		{
			e.currentTarget.loader.contentLoaderInfo.removeEventListener(Event.COMPLETE,loaderEnd);
			var loaderinfo:LoaderInfo = e.target as LoaderInfo;  
			
			_loader.contentLoaderInfo.addEventListener(Event.COMPLETE, secondLoaderEnd);  
			_loader.loadBytes(loaderinfo.bytes); 
		}
		
		private function secondLoaderEnd(e:Event):void
		{
			_loader.contentLoaderInfo.removeEventListener(Event.COMPLETE, secondLoaderEnd); 
			if(_callBack != null)
				_callBack(_loader);
		}
		
	}
}
