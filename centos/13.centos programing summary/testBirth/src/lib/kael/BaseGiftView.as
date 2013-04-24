package lib.kael
{
	import flash.display.Sprite;
	import flash.events.Event;
	
	import myevent.SwitchSceneEvent;

	public class BaseGiftView extends Sprite
	{
		protected var _back:MyExtendedBtn;
		
		public function BaseGiftView(){
			_back = new MyExtendedBtn("返回",onBackClick);
			this.addEventListener(Event.ADDED_TO_STAGE,onAdded);
		}
		
		public function onAdded(e:Event):void{
			_back.x = this.parent.stage.stageWidth - _back.width - 20;
			_back.y = this.parent.stage.stageHeight - _back.height - 60;
			// other zone is view main component
			this.addChild(_back);
		}
		
		public function onBackClick():void{
			var main:Mainview = new Mainview;
			var event:SwitchSceneEvent = new SwitchSceneEvent(SwitchSceneEvent.SWITCHSCENE_EVENT,true);
			event.from = this;
			event.to = main;
			dispatchEvent(event);
		}
	}
}