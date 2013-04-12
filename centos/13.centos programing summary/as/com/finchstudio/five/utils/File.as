package com.finchstudio.five.utils
{
    import flash.display.Loader;
    import flash.errors.IOError;
    import flash.events.Event;
    import flash.events.IOErrorEvent;
    import flash.events.ProgressEvent;
    import flash.events.SecurityErrorEvent;
    import flash.net.URLRequest;
    import flash.system.ApplicationDomain;
    import flash.system.LoaderContext;

	public class File {
		private var _loader:Loader;
		private var _uri:String;
		private var _applicationDomain:ApplicationDomain;
		private var request : URLRequest;
		private var lc:LoaderContext;
		public static var onVersion:Function;
		public var onProgress:Function = new Function();
		public var onComplete:Function = new Function();
		
		public function get loader():Loader 
		{
			return _loader;
		}
		
		public function get applicationDomain():ApplicationDomain 
		{
			return _applicationDomain;
		}
		
		public function File() 
		{
			_loader = new Loader();
            _loader.contentLoaderInfo.addEventListener(Event.COMPLETE, complete);
			_loader.contentLoaderInfo.addEventListener(ProgressEvent.PROGRESS, progress);
            _loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
			_loader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
		}
		
		public function load(uri:String):void
		{
			_uri = uri;
			request = new URLRequest(_uri);
			
			lc = new LoaderContext();
			lc.checkPolicyFile = true;
			lc.applicationDomain = new ApplicationDomain(ApplicationDomain.currentDomain);
			
			_loader.load(request, lc);
		}
		
		private function complete(e:Event):void 
		{
			_applicationDomain = _loader.contentLoaderInfo.applicationDomain;
			onComplete();
        }
		private function progress(e:ProgressEvent):void
		{
			onProgress(e.bytesTotal, e.bytesLoaded);
		}
		
		private function securityErrorHandler(event:SecurityErrorEvent):void
		{
            _loader.unload();
			load(_uri);
        }

		private function ioErrorHandler(e:IOErrorEvent):void 
		{
			_loader.unload();
			load(_uri);
		}
		
		// get class
		public function getClassByName(className:String):Class
		{
			try 
			{
				return _applicationDomain.getDefinition(className) as Class;
			}
			catch (e:Error) {
				throw new Error(className + ' not found in ' + _uri + '\n' + e);
			}
			return null;
		}
	}
}
