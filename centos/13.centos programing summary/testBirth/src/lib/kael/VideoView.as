package lib.kael
{
	import com.greensock.TweenLite;
	import com.greensock.events.LoaderEvent;
	import com.greensock.loading.LoaderMax;
	import com.greensock.loading.VideoLoader;
	
	import flash.events.Event;

	public class VideoView extends BaseGiftView
	{
		private var video:VideoLoader;// current video
		public var queue:LoaderMax;
		public function VideoView()
		{
			
		}
		
		override public function onAdded(e:Event):void
		{
			super.onAdded(e);
			video = new VideoLoader("../assets/flv/"+DataLocation.videos[0]+".flv",{onProgress:progressHandler});
			video.load();
			queue = new LoaderMax({name:"mainQueue",onComplete:completeHandler, onError:errorHandler});
			queue.skipPaused = true;
			queue.append(video);
			queue.load();
		}
		
		private function progressHandler(event:LoaderEvent):void
		{
			
		}
		
		
		private function completeHandler(event:LoaderEvent):void {
//			video.playVideo();
			// do not play at first
			this.video.gotoVideoTime(0, true);
			video.volume = 0;
			this.video.videoPaused = true;
			addChild(video.content);
			// mi 2s is 1280 * 720
			video.content.width = this.parent.stage.stageWidth * 0.6;
			video.content.height = this.parent.stage.stageHeight * 0.6;
			video.content.x = (this.parent.stage.stageWidth - video.content.width)/2 - 100;
			video.content.y = 30;
//			TweenLite.to(video, 2, {volume:1});
			video.addEventListener(VideoLoader.VIDEO_COMPLETE, removeVideo);
		}
		
		private function removeVideo(e:LoaderEvent):void
		{
			video.removeEventListener(VideoLoader.VIDEO_COMPLETE, removeVideo);
			removeChild(video.content);
		}
		
		private function errorHandler(event:LoaderEvent):void {
			trace("error occured with " + event.target + ": " + event.text);
		}
		
		override public function onBackClick():void{
			super.onBackClick();
			if(video)
			{
				removeVideo(null);
				video.unload();
				queue.unload();
			}
		}
	}
}