package
{
	import com.greensock.TweenLite;
	
	import flash.display.DisplayObject;
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	
	import lib.kael.InitScene;
	
	import myevent.SwitchSceneEvent;
	
	public class testBirth extends Sprite
	{
		private var prevScene:DisplayObject = null;
		private var currScene:DisplayObject = null; 
		public function testBirth()
		{
			super();
			// 支持 autoOrient
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			stage.color = 0;
			this.addEventListener(SwitchSceneEvent.SWITCHSCENE_EVENT,onEvent);
			currScene = new InitScene;
			addChild(currScene);
			
		}
		
		public function onEvent(e:SwitchSceneEvent):void{
			if(e.to == null)return;
			prevScene = e.from as DisplayObject;
			currScene = e.to as DisplayObject;
			if(prevScene){
				TweenLite.to(prevScene, 1.5, {alpha:0,onComplete:prevSceneDispeared});
			}else{
				showCurrentView();
			}
			e.stopPropagation();
		}
		
		public function prevSceneDispeared():void{
			if(this.contains(prevScene)){
				this.removeChild(prevScene);
			}
			if(currScene!=null){
				showCurrentView();
			}
		}
		
		private function showCurrentView():void{
			if(!this.contains(currScene)){
				this.addChild(currScene);
			}
			currScene.alpha = 0;
			TweenLite.to(currScene, 1.5, {alpha:1});
		}
	}
}