package
{	
	import com.greensock.TweenMax;
	import com.greensock.easing.Circ;
	import com.greensock.events.LoaderEvent;
	import com.greensock.loading.LoaderMax;
	import com.greensock.loading.VideoLoader;
	
	import flash.display.Loader;
	import flash.display.LoaderInfo;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TouchEvent;
	import flash.geom.Rectangle;
	import flash.net.URLRequest;

	[SWF(backgroundColor = 0xffffff, width = 1024, height = 650, frameRate = 30)]
	public class VideoPlayerView extends Sprite
	{
		var loader:Loader = new Loader();
		var pl:Player;
		private var video:VideoLoader;// current video
		public var queue:LoaderMax;
		public function VideoPlayerView()
		{
			loader.contentLoaderInfo.addEventListener(Event.COMPLETE, loadComplete);
			loader.load(new URLRequest("../assets/player.swf"));
		}
		
		private function loadComplete(evt:Event):void
		{
			pl = evt.target.content;
			this.addChild(pl);
			initUI();
		}
		
		private function initUI() : void
		{
			pl.controlUI_mc.progressBar_mc.mouseEnabled = false;
			pl.preloader_mc.mouseEnabled = false;
			pl.controlUI_mc.blendMode = "layer";
			var w:int = 0;
			pl.controlUI_mc.loadingBar_mc.width = 0;
			pl.controlUI_mc.progressBar_mc.width = w;
			pl.controlUI_mc.scrubber_mc.x = pl.controlUI_mc.progressBar_mc.x;
			TweenMax.allTo([pl.controlUI_mc, pl.playPauseBigButton_mc, pl.preloader_mc], 0, {autoAlpha:0});
			
			video = new VideoLoader("../assets/1.flv",{onProgress:progressHandler});
			video.load();
			queue = new LoaderMax({name:"mainQueue",onComplete:completeHandler, onError:errorHandler});
			queue.skipPaused = true;
			queue.append(video);
			queue.load();
			return;
		}// end function
		
		private function progressHandler(event:LoaderEvent):void
		{
			
		}
		
		private function completeHandler(event:LoaderEvent):void {
			// do not play at first
//			// mi 2s is 1280 * 720
			video.content.width = pl.width - 3;
			video.content.height = pl.height - pl.controlUI_mc.height + 6;
			this.showVideo(video);
		}
		
		private function errorHandler(event:LoaderEvent):void {
			trace("error occured with " + event.target + ": " + event.text);
		}
		
		private function addListeners(param1:Array, param2:String, param3:Function) : void
		{
			var len:* = param1.length;
			while (len--)
			{
				param1[len].addEventListener(param2, param3);
			}
			return;
		}// end function
		
		private function activateUI() : void
		{
			TweenMax.to(pl.controlUI_mc, 0.3, {autoAlpha:1});
			addListeners([pl.controlUI_mc.playPauseButton_mc, pl.videoContainer_mc], MouseEvent.CLICK, togglePlayPause);
			pl.playPauseBigButton_mc.addEventListener(MouseEvent.MOUSE_DOWN,bigPlayDown);
			pl.playPauseBigButton_mc.addEventListener(MouseEvent.MOUSE_UP,bigPlayUp);
			pl.controlUI_mc.audio_mc.addEventListener(MouseEvent.CLICK, toggleAudio);
			pl.controlUI_mc.next_mc.addEventListener(MouseEvent.CLICK, playList);
			pl.controlUI_mc.scrubber_mc.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownScrubber);
			pl.controlUI_mc.loadingBar_mc.addEventListener(MouseEvent.CLICK, this.scrubToMouse);
			
			// touch version
//			addListeners([pl.controlUI_mc.playPauseButton_mc, pl.videoContainer_mc], TouchEvent.TOUCH_END, togglePlayPause);
//			pl.playPauseBigButton_mc.addEventListener(TouchEvent.TOUCH_BEGIN,bigPlayDown);
//			pl.playPauseBigButton_mc.addEventListener(TouchEvent.TOUCH_END,bigPlayUp);
//			pl.controlUI_mc.audio_mc.addEventListener(TouchEvent.TOUCH_END, toggleAudio);
//			pl.controlUI_mc.scrubber_mc.addEventListener(TouchEvent.TOUCH_BEGIN, mouseDownScrubber);
//			pl.controlUI_mc.loadingBar_mc.addEventListener(TouchEvent.TOUCH_END, this.scrubToMouse);
			
			// common setting 
			var controls:Array = [pl.controlUI_mc.playPauseButton_mc, pl.playPauseBigButton_mc, pl.controlUI_mc.next_mc, pl.controlUI_mc.audio_mc, pl.controlUI_mc.scrubber_mc];
			var len:* = controls.length;
			while (len--)
			{
				controls[len].buttonMode = true;
				controls[len].mouseChildren = false;
			}
			return;
		}// end function
		
		private function bigPlayDown(e:Event):void{
			TweenMax.to(pl.playPauseBigButton_mc,0.25, {scaleX:1.2, scaleY:1.2, ease:Circ.easeOut});
		}
		
		private function bigPlayUp(e:Event):void{
			TweenMax.to(pl.playPauseBigButton_mc,0.25, {scaleX:1.0, scaleY:1.0, ease:Circ.easeOut});
			togglePlayPause();
		}
		
		private function toggleControlUI(event:Event) : void
		{
			pl._mouseIsOver = !pl._mouseIsOver;
			if (pl._mouseIsOver)
			{
				TweenMax.to(pl.controlUI_mc, 0.3, {autoAlpha:1});
			}
			else
			{
				TweenMax.to(pl.controlUI_mc, 0.3, {autoAlpha:0});
			}
			return;
		}// end function
		
		private function togglePlayPause(event:Event = null) : void
		{
			pl._currentVideo.videoPaused = !pl._currentVideo.videoPaused;
			if (pl._currentVideo.videoPaused)
			{
				TweenMax.to(pl.playPauseBigButton_mc, 0.3, {autoAlpha:1});
				pl.controlUI_mc.playPauseButton_mc.gotoAndStop("paused");
				TweenMax.to(pl.videoContainer_mc, 0.3, {blurFilter:{blurX:6, blurY:6}, colorMatrixFilter:{brightness:0.5}});
			}
			else
			{
				TweenMax.to(pl.playPauseBigButton_mc, 0.3, {autoAlpha:0});
				pl.controlUI_mc.playPauseButton_mc.gotoAndStop("playing");
				TweenMax.to(pl.videoContainer_mc, 0.3, {blurFilter:{blurX:0, blurY:0, remove:true}, colorMatrixFilter:{brightness:1, remove:true}});
			}
			return;
		}// end function
		
		private function toggleAudio(event:Event) : void
		{
			pl._silentMode = !pl._silentMode;
			if (pl._silentMode)
			{
				pl._currentVideo.volume = 0;
				pl.controlUI_mc.audio_mc.label.gotoAndStop("off");
			}
			else
			{
				pl._currentVideo.volume = 1;
				pl.controlUI_mc.audio_mc.label.gotoAndStop("on");
			}
			return;
		}// end function
		
		private function playList():void{
			
		}
		
		private function mouseDownScrubber(event:MouseEvent) : void
		{
			pl._preScrubPaused = pl._currentVideo.videoPaused;
			pl._currentVideo.videoPaused = true;
			pl.controlUI_mc.scrubber_mc.startDrag(false, new Rectangle(pl.controlUI_mc.loadingBar_mc.x, pl.controlUI_mc.loadingBar_mc.y, pl.controlUI_mc.loadingBar_mc.width, 0));
			stage.addEventListener(MouseEvent.MOUSE_UP, this.mouseUpScrubber);
			stage.addEventListener(MouseEvent.MOUSE_MOVE, this.scrubToMouse);
			
//			stage.addEventListener(TouchEvent.TOUCH_END, this.mouseUpScrubber);
//			stage.addEventListener(TouchEvent.TOUCH_MOVE, this.scrubToMouse);
			return;
		}// end function
		
		private function scrubToMouse(event:MouseEvent) : void
		{
			pl.controlUI_mc.progressBar_mc.width = pl.controlUI_mc.mouseX - pl.controlUI_mc.progressBar_mc.x;
			pl._currentVideo.playProgress = pl.controlUI_mc.progressBar_mc.scaleX;
			return;
		}// end function
		
		private function mouseUpScrubber(event:MouseEvent) : void
		{
			stage.removeEventListener(MouseEvent.MOUSE_UP, this.mouseUpScrubber);
			stage.removeEventListener(MouseEvent.MOUSE_MOVE, this.scrubToMouse);
			
//			stage.removeEventListener(TouchEvent.TOUCH_END, this.mouseUpScrubber);
//			stage.removeEventListener(TouchEvent.TOUCH_MOVE, this.scrubToMouse);
			pl.controlUI_mc.scrubber_mc.stopDrag();
			pl._currentVideo.videoPaused = pl._preScrubPaused;
			return;
		}// end function
		
		private function showVideo(param:VideoLoader) : void
		{
			if (param == pl._currentVideo)
			{
				return;
			}
			if (pl._currentVideo == null)
			{
				this.activateUI();
			}
			else
			{
				pl._currentVideo.removeEventListener(LoaderEvent.PROGRESS, updateDownloadProgress);
				pl._currentVideo.removeEventListener(VideoLoader.VIDEO_COMPLETE, playOver);
				pl._currentVideo.removeEventListener(VideoLoader.PLAY_PROGRESS, updatePlayProgress);
				pl._currentVideo.removeEventListener(VideoLoader.VIDEO_BUFFER_FULL, bufferFullHandler);
				pl._currentVideo.removeEventListener(LoaderEvent.INIT, refreshTotalTime);
				if (pl._currentVideo.videoPaused)
				{
					togglePlayPause();
				}
				TweenMax.to(pl.preloader_mc, 0.3, {autoAlpha:0, onComplete:pl.preloader_mc.stop});
				TweenMax.to(pl._currentVideo.content, 0.8, {autoAlpha:0});
				TweenMax.to(pl._currentVideo, 0.8, {volume:0, onComplete:pl._currentVideo.pauseVideo});
			}
			
			pl._currentVideo = param;
			pl._currentVideo.addEventListener(LoaderEvent.PROGRESS, updateDownloadProgress);
			pl._currentVideo.addEventListener(VideoLoader.VIDEO_COMPLETE, playOver);
			pl._currentVideo.addEventListener(VideoLoader.PLAY_PROGRESS, updatePlayProgress);
			if (pl._currentVideo.progress < 1 && pl._currentVideo.bufferProgress < 1)
			{
				pl._currentVideo.addEventListener(VideoLoader.VIDEO_BUFFER_FULL, bufferFullHandler);
				pl._currentVideo.prioritize(true);
				pl.preloader_mc.play();
				TweenMax.to(pl.preloader_mc, 0.3, {autoAlpha:1});
			}
			pl._currentVideo.gotoVideoTime(0, true);
			pl._currentVideo.volume = 0;
			if (!pl._silentMode)
			{
				TweenMax.to(pl._currentVideo, 0.8, {volume:1});
			}
			pl.videoContainer_mc.addChild(pl._currentVideo.content);
			TweenMax.to(pl._currentVideo.content, 0.8, {autoAlpha:1});
			this.refreshTotalTime();
			if (pl._currentVideo.metaData == null)
			{
				pl._currentVideo.addEventListener(LoaderEvent.INIT, this.refreshTotalTime);
			}
			updateDownloadProgress();
			updatePlayProgress();
			
			// stop from begin
			togglePlayPause();
			
			return;
			
		}
		
		private function bufferFullHandler(event:LoaderEvent) : void
		{
			TweenMax.to(pl.preloader_mc, 0.3, {autoAlpha:0, onComplete:pl.preloader_mc.stop});
			return;
		}// end function
		
		private function refreshTotalTime(event:LoaderEvent = null) : void
		{
			var min:* = pl.force2Digits(int(pl._currentVideo.duration / 60));
			var sec:* = pl.force2Digits(int(pl._currentVideo.duration % 60));
			pl.controlUI_mc.totalTime_tf.text = min + ":" + sec;
			return;
		}// end function
		
		private function updateDownloadProgress(event:LoaderEvent = null) : void
		{
			pl.controlUI_mc.loadingBar_mc.scaleX = pl._currentVideo.progress;
			return;
		}// end function
		
		private function playOver():void{
			
		}
		
		private function updatePlayProgress(event:LoaderEvent = null) : void
		{
			var videotime:* = pl._currentVideo.videoTime;
			var min:* = pl.force2Digits(int(videotime / 60));
			var sec:* = pl.force2Digits(int(videotime % 60));
			pl.controlUI_mc.currentTime_tf.text = min + ":" + sec;
			pl.controlUI_mc.progressBar_mc.scaleX = pl._currentVideo.playProgress;
			pl.controlUI_mc.scrubber_mc.x = pl.controlUI_mc.progressBar_mc.x + pl.controlUI_mc.progressBar_mc.width;
			return;
		}// end function
	}
}