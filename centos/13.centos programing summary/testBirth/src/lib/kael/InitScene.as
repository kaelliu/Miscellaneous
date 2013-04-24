package lib.kael
{
	import com.greensock.TimelineMax;
	import com.greensock.TweenLite;
	import com.greensock.TweenMax;
	import com.greensock.easing.Bounce;
	import com.greensock.easing.Circ;
	
	import flash.display.Loader;
	import flash.display.Shape;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.filters.DropShadowFilter;
	import flash.net.URLRequest;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	
	import myevent.SwitchSceneEvent;
	
	public class InitScene extends Sprite
	{
		private var logoPic:Loader;
		private var s:Shape = new Shape;
		private var timeline:TimelineMax; 
		private var _text:TextField;
		public static var dropFilter:DropShadowFilter = new DropShadowFilter(0, 45, 0, 1, 2, 2, 10);
		public function InitScene()
		{
			logoPic = new Loader();
			logoPic.load(new URLRequest("../assets/pic/JJS.png"));
			logoPic.contentLoaderInfo.addEventListener(Event.COMPLETE, onLoadComplete);
			timeline = new TimelineMax({yoyo:true,onComplete:timelineFin});
			
			_text = new TextField;
			_text.selectable = false;
			_text.filters = [dropFilter];
			var tmf:TextFormat =new TextFormat();
			tmf.align = TextFormatAlign.LEFT;
			tmf.color = 0xffffff;
			tmf.size = 24;
			_text.defaultTextFormat=tmf;
			_text.width = 0;
			_text.text = "Powered by FinchStudio";
			this.addChild(_text);
		}
		
		private function onLoadComplete(e:Event):void{
			logoPic.x = (this.parent.stage.stageWidth - logoPic.width)/2;
			logoPic.y = (this.parent.stage.stageHeight - logoPic.height)/2;
			this.addChild(logoPic);
			logoPic.alpha = 0;
			s.graphics.beginFill(0xffffff,0.5); 
			var radius:int=40;
			s.graphics.drawCircle(0,0,radius);  
			s.x = logoPic.x - radius * 2;
			s.y = logoPic.y + radius + 10;
			_text.y = logoPic.y + logoPic.height - 30;// 30 pixel height blank in picture
			_text.x = logoPic.x - 10;
			this.addChild(s);
			logoPic.mask = s;
			timeline.insert(TweenLite.to(logoPic, 2.5, {alpha:1}));
			timeline.insert(TweenLite.to(s,1.75,{x:logoPic.x+logoPic.width+30}));
			timeline.play();
		}
		
		private function timelineFin():void{
			TweenLite.to(s,0.75,{x:logoPic.x + (logoPic.width - s.width)/2+40,ease:Bounce.easeOut,onComplete:step2});
		}
		
		private function step2():void{
			TweenLite.to(s,1,{scaleX:2.8, scaleY:2.8, ease:Circ.easeOut,onComplete:onAlphaOn});
		}
		
		private function onAlphaOn():void{
			logoPic.mask = null;
			this.removeChild(s);
			TweenMax.to(logoPic, 1.5, {glowFilter:{color:0x91e600, alpha:1, blurX:30, blurY:30, remove:true},ease:Bounce.easeOut,onComplete:textStep});	
		}
		
		private function textStep():void{
//			TweenLite.to(_text, 1, {setSize:{width:240}});
			this.addEventListener(Event.ENTER_FRAME,enterframe);
		}
		
		private function enterframe(e:Event):void{
			_text.width+=20;
			if(_text.width >= 240){
				this.removeEventListener(Event.ENTER_FRAME,enterframe);
				// do enter main screen
				var main:Mainview = new Mainview;
				var event:SwitchSceneEvent = new SwitchSceneEvent(SwitchSceneEvent.SWITCHSCENE_EVENT,true);
				event.from = this;
				event.to = main;
				dispatchEvent(event);
			}
		}
	}
}