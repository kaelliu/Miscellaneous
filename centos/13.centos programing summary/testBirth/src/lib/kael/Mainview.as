package lib.kael
{
	import com.adobe.nativeExtensions.Vibration;
	import com.greensock.TimelineMax;
	import com.greensock.TweenLite;
	import com.greensock.easing.Bounce;
	import com.greensock.easing.Circ;
	
	import flash.display.Loader;
	import flash.display.Sprite;
	import flash.events.AccelerometerEvent;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TouchEvent;
	import flash.net.URLRequest;
	import flash.sensors.Accelerometer;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	
	import myevent.SwitchSceneEvent;

	public class Mainview extends Sprite
	{
		private var isSupported:Boolean = Accelerometer.isSupported;
		private var _accelerometer:Accelerometer = new Accelerometer();
		
		private const THRESHOLD:Number = 1.5;
		private var _isMeasuring:Boolean = false;
		private var _isShaking:Boolean = false;
		private var _offPrevState:int = 1;// 1 require for shake step, 2 menu drop step 
		private var _memorySprite:Loader;
		private var _videoSprite:Loader;
		private var _addonSprite:Loader;
		private var _iconLoaded:int=0;
		private var timeline:TimelineMax;
		private var _text:TextField;
		public function Mainview()
		{
			_memorySprite = new Loader();
			_videoSprite = new Loader();
			_addonSprite = new Loader();
			timeline = new TimelineMax({yoyo:true,onComplete:timelineFin});
			this.addEventListener(Event.ADDED_TO_STAGE,onAdded);
			this.addEventListener(Event.REMOVED_FROM_STAGE,onRemove);
			
			_text = new TextField;
			_text.selectable = false;
			var tmf:TextFormat =new TextFormat();
			tmf.align = TextFormatAlign.LEFT;
			tmf.color = 0xffffff;
			tmf.size = 32;
			_text.defaultTextFormat=tmf;
			_text.width = 240;
			_text.text = "选一个吧亲爱的";
			_text.visible = false;
			this.addChild(_text);
		}
		
		private function onAdded(e:Event):void{
			checksupport();
		}
		
		private function timelineFin():void{
//			_memorySprite.addEventListener(TouchEvent.TOUCH_BEGIN,handleTouchMemory);
//			_videoSprite.addEventListener(TouchEvent.TOUCH_BEGIN,handleTouchVideo);
//			_addonSprite.addEventListener(TouchEvent.TOUCH_BEGIN,handleTouchAddon);
//			_memorySprite.addEventListener(TouchEvent.TOUCH_END,handleSelectMemory);
//			_videoSprite.addEventListener(TouchEvent.TOUCH_END,handleSelectVideo);
//			_addonSprite.addEventListener(TouchEvent.TOUCH_END,handleSelectAddon);
			
			if(this.parent !=null)
			{
				_text.y = _memorySprite.y + _memorySprite.height;
				_text.x = (this.parent.stage.stageWidth - _text.width) / 2;
				_text.visible = true;
			}
			
			_memorySprite.addEventListener(MouseEvent.MOUSE_DOWN,handleTouchMemory);
			_videoSprite.addEventListener(MouseEvent.MOUSE_DOWN,handleTouchVideo);
			_addonSprite.addEventListener(MouseEvent.MOUSE_DOWN,handleTouchAddon);
			_memorySprite.addEventListener(MouseEvent.MOUSE_UP,handleSelectMemory);
			_videoSprite.addEventListener(MouseEvent.MOUSE_UP,handleSelectVideo);
			_addonSprite.addEventListener(MouseEvent.MOUSE_UP,handleSelectAddon);
			
		}
		
		private function checksupport():void {
			if (isSupported) {
				_accelerometer.addEventListener(AccelerometerEvent.UPDATE, updateHandler);
			} else {
				showMainView();
			}
		}
		
		public function onRemove(e:Event):void {
			if(_memorySprite)
				_memorySprite.removeEventListener(TouchEvent.TOUCH_END,handleSelectMemory);
			if(_videoSprite)
				_videoSprite.removeEventListener(TouchEvent.TOUCH_END,handleSelectVideo);
			if(_addonSprite)
				_addonSprite.removeEventListener(TouchEvent.TOUCH_END,handleSelectAddon);
		}
		
		private function updateHandler(event:AccelerometerEvent):void {
			if (_isMeasuring) {
				return;
			}
			_isMeasuring = true;
			trace("x", event.accelerationX);
			trace("y", event.accelerationY);
			trace("z", event.accelerationZ);
			
			if (Math.abs(event.accelerationX) > THRESHOLD)
			{
				_isShaking = true;
			}
			if (Math.abs(event.accelerationY) > THRESHOLD*2)
			{
				_isShaking = true;
			}
			if (Math.abs(event.accelerationZ) > THRESHOLD*3)
			{
				_isShaking = true;
			}
			if (_isShaking)
			{
				trace("we have a shake");
				// do shake event here
				// Vibration machine
				var vibe:Vibration;
				if (Vibration.isSupported)
				{
					vibe = new Vibration();
					vibe.vibrate(2000);
				}
				showMainView();
			}
			_isMeasuring = false;
		}
		
		private function showMainView():void{
			_memorySprite.load(new URLRequest("../assets/pic/1.png"));
			_memorySprite.contentLoaderInfo.addEventListener(Event.COMPLETE, onLoadComplete);
			_videoSprite.load(new URLRequest("../assets/pic/2.png"));
			_videoSprite.contentLoaderInfo.addEventListener(Event.COMPLETE, onLoadComplete);
			_addonSprite.load(new URLRequest("../assets/pic/3.png"));
			_addonSprite.contentLoaderInfo.addEventListener(Event.COMPLETE, onLoadComplete);
		}
		
		private function onLoadComplete(e:Event):void{
			_iconLoaded++;
			if(_iconLoaded>=3){
				doProcess();				
			}
		}
		
		private function doProcess():void{
			this.addChild(_memorySprite);
			this.addChild(_videoSprite);
			this.addChild(_addonSprite);
			_memorySprite.y = -Math.random() * 64;// 64 is icon size
			_memorySprite.x = (this.parent.stage.stageWidth/2 - 64)/2;
			
			_videoSprite.y = -Math.random() * 64;// 64 is icon size
			_videoSprite.x = (this.parent.stage.stageWidth - 64) / 2;
			
			_addonSprite.y = -Math.random() * 64;// 64 is icon size
			_addonSprite.x = (this.parent.stage.stageWidth/2 - 64)/2 + (this.parent.stage.stageWidth) / 2;
			
			timeline.insert(TweenLite.to(_memorySprite,1.75, {y:this.parent.stage.stageHeight / 2 + 32,ease:Bounce.easeOut}));
			timeline.insert(TweenLite.to(_videoSprite,1.75,{y:this.parent.stage.stageHeight / 2 + 32,ease:Bounce.easeOut}));
			timeline.insert(TweenLite.to(_addonSprite,1.75,{y:this.parent.stage.stageHeight / 2 + 32,ease:Bounce.easeOut}));
			timeline.play();
		}
		
		private function handleSelectMemory(e:Event):void {
			TweenLite.to(_memorySprite,0.25, {scaleX:1, scaleY:1, ease:Circ.easeOut});
			var mem:MemoryView = new MemoryView;
			var event:SwitchSceneEvent = new SwitchSceneEvent(SwitchSceneEvent.SWITCHSCENE_EVENT,true);
			event.from = this;
			event.to = mem;
			dispatchEvent(event);
		}
		
		private function handleSelectVideo(e:Event):void {
			TweenLite.to(_videoSprite,0.25, {scaleX:1, scaleY:1, ease:Circ.easeOut});
			var addon:VideoView = new VideoView;
			var event:SwitchSceneEvent = new SwitchSceneEvent(SwitchSceneEvent.SWITCHSCENE_EVENT,true);
			event.from = this;
			event.to = addon;
			dispatchEvent(event);
		}
		
		private function handleSelectAddon(e:Event):void {
			TweenLite.to(_addonSprite,0.25, {scaleX:1, scaleY:1, ease:Circ.easeOut});
			var addon:AddonView = new AddonView;
			var event:SwitchSceneEvent = new SwitchSceneEvent(SwitchSceneEvent.SWITCHSCENE_EVENT,true);
			event.from = this;
			event.to = addon;
			dispatchEvent(event);
		}
		
		private function handleTouchMemory(e:Event):void {
			TweenLite.to(_memorySprite,0.25, {scaleX:1.2, scaleY:1.2, ease:Circ.easeOut});
		}
		
		private function handleTouchVideo(e:Event):void {
			TweenLite.to(_videoSprite,0.25, {scaleX:1.2, scaleY:1.2, ease:Circ.easeOut});
		}
		
		private function handleTouchAddon(e:Event):void {
			TweenLite.to(_addonSprite,0.25, {scaleX:1.2, scaleY:1.2, ease:Circ.easeOut});
		}
	}
}