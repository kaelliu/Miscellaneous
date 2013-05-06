package lib.kael
{
	import flash.display.GradientType;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.geom.Matrix;
	
	public class MemoryView extends BaseGiftView
	{
		private var book_container:MovieClip=new MovieClip();
		private var mypageflip:PageFlipClass=new PageFlipClass();
		private var myXML:XML =
			<content width="300" height="400">
				<page src="../assets/swf/1.swf"/>
				<page src="../assets/swf/2.swf"/>
				<page src="../assets/swf/3.swf"/>
				<page src="../assets/swf/4.swf"/>
				<page src="../assets/swf/5.swf"/>
				<page src="../assets/swf/6.swf"/>
				<page src="../assets/swf/7.swf"/>
				<page src="../assets/swf/8.swf"/>
				<page src="../assets/swf/9.swf"/>
				<page src="../assets/swf/10.swf"/>
				<page src="../assets/swf/11.swf"/>
				<page src="../assets/swf/12.swf"/>
			</content>;
		public function MemoryView()
		{
			
		}
		
		private function pageOver():void{
			var page:int = mypageflip.book_page;
			var id:int=0;
			if(page >= 0 && page <= 4)
			{
				id = 1;
			}
			else if(page > 4 && page <= 8){
				id = 2;
			}
			else{
				id = 3;
			}
			if(id != 0 )
				BaseSound.getInstance().playMusic(id);
		}
		
		override public function onBackClick():void{
			super.onBackClick();
			BaseSound.getInstance().stopCurrent();
			BaseSound.getInstance().resetCurrentPlaying();
		}
		
		override public function onAdded(e:Event):void
		{
			var colors : Array = [0x5BB5D8, 0x278FC6];
			var alphas : Array = [1,1];
			var ratios : Array = [0, 255];
			var matrix : Matrix = new Matrix();
			matrix.createGradientBox(80, 23, Math.PI / 2);
			this.graphics.beginGradientFill(GradientType.LINEAR, colors, alphas, ratios, matrix);
			this.graphics.drawRect(0,0,this.parent.stage.stageWidth,this.parent.stage.stageHeight);
			this.graphics.endFill();
			
			matrix.createGradientBox(78, 21, Math.PI / 2);
			this.graphics.beginGradientFill(GradientType.LINEAR, [0x60C8F1, 0x5AAFE7], [1, 1], [0, 255], matrix);
			this.graphics.drawRect(1,1,this.parent.stage.stageWidth,this.parent.stage.stageHeight);
			this.graphics.endFill();
			
			//The background
			matrix.createGradientBox(76, 19, Math.PI / 2);
			this.graphics.beginGradientFill(GradientType.LINEAR, [0x44BDEE, 0x329ADE], [1, 1], [32, 255], matrix);
			this.graphics.drawRect(2,2,this.parent.stage.stageWidth,this.parent.stage.stageHeight);
			this.graphics.endFill();
			
			super.onAdded(e);
			addChild(book_container);
			book_container.x=36;
			book_container.y=30;
			mypageflip.myXML=myXML;
			mypageflip.book_root=book_container;
			mypageflip.book_initpage=0;
			mypageflip.book_TimerNum=30;
			mypageflip.InitBook();
			mypageflip.onPageEnd = pageOver;
		}
	}
}