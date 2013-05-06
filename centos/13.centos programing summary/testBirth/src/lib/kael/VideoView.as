package lib.kael
{
	import flash.events.Event;

	public class VideoView extends BaseGiftView
	{
		private var player:VideoPlayerView;
		public function VideoView()
		{
			player = new VideoPlayerView;
		}
		
		override public function onAdded(e:Event):void
		{
			super.onAdded(e);
			this.addChild(player);
		}
		
		override public function onBackClick():void{
			super.onBackClick();
			if(player)
			{
				player.onQuit();
			}
		}
	}
}