package lib.kael
{
	
	import com.greensock.TweenLite;
	
	import flash.events.Event;
	import flash.media.Sound;
	import flash.media.SoundChannel;
	import flash.media.SoundTransform;
	import flash.net.URLRequest;

	/**
	 * 注册控制类
	 * 列举各种控制类的引用
	 * 为win和操作建立关联
	 */
    public class BaseSound
    {
		
		private static var instance:BaseSound = null;
		private var lastSound:Sound = null;
		private var localSound:Sound = new Sound();
		private var channel:SoundChannel = new SoundChannel();
		private var pausePosition:int; 
		
		private var playing:Boolean = false;
		private var playTag:int = 0;
		private var currentPlaying:String = "";
        public function BaseSound()
        {
			if(instance!=null)throw new Error("Singlton Error!");
			return;
		}
		
		public static function getInstance():BaseSound{
			if(instance==null){
				instance = new BaseSound();
			}
			return instance;
		}
		
		public function init():void{ 
		}
		
		public function changeVolumn(vol:int):void{
			//创建转换对象  
			var trans:SoundTransform=new SoundTransform();  
			//获取声音的值，并加入转换对象  
			trans.volume=vol/100.0; 
			//实现转换  
			channel.soundTransform=trans;
		}
		
		public function playMusic(id:int):void{
//			if(!DataLocation.getInstance()._isPlayMusic)return;
			var url:String = "../assets/music/"+id+".mp3";
			if(url == currentPlaying)return;
			if(currentPlaying.length != 0){
				lastSound = localSound;
//				TweenLite.to(localSound, 1, {volume:0});
			}
			currentPlaying = url;
			var req:URLRequest = new URLRequest(url);
			localSound = new Sound();
			localSound.addEventListener(Event.COMPLETE, onLoadComplete);
			localSound.load(req);
		}
		
		public function resetCurrentPlaying():void{
			currentPlaying = "";
		}
		
		public function stopCurrent():void{
			channel.stop();
			playTag = 0;
			playing = false;
//			currentPlaying = "";
		}
		
		public function pauseCurrent():void{
			channel.stop();
			playing = false;
		}
		
		private function onLoadComplete(event:Event):void
		{
			localSound.removeEventListener(Event.COMPLETE, onLoadComplete);
			stopCurrent();
			playing = true;
			channel = localSound.play(0,int.MAX_VALUE);
			channel.soundTransform.volume = 0.4;
//			TweenLite.to(localSound, 1, {volume:1});
		}
		
		public function isPlaying():Boolean
		{
			return playing;
		}
    }
}
