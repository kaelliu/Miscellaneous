package
{
	import flash.display.*;
	import flash.events.*;
	import flash.geom.*;
	
	public class Player extends MovieClip
	{
		public var videoContainer_mc:MovieClip;
		public var preloader_mc:MovieClip;
		public var controlUI_mc:MovieClip;
		public var playPauseBigButton_mc:MovieClip;
		public var _videos:Array;
		public var _currentVideo:Object;
		public var _mouseIsOver:Boolean;
		public var _silentMode:Boolean;
		public var _preScrubPaused:Boolean;
		
		public function Player()
		{
			
		}// end function
		
		public function force2Digits(param1:Number) : String
		{
			return param1 < 10 ? ("0" + String(param1)) : (String(param1));
		}// end function
	}
}