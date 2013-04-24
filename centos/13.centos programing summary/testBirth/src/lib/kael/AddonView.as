package lib.kael
{
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;

	public class AddonView extends BaseGiftView
	{
		private var bg:Sprite;
		private var htmlTextView:TextField;
//		private var someVirtualGift:Array
		public function AddonView()
		{
			bg = new Sprite;
//			bg.x = (this.parent.stage.stageWidth - bg.width)/2;
			bg.y = 30;
			this.addChild(bg);
			htmlTextView = new TextField;
			htmlTextView.selectable=false;
			bg.addChild(htmlTextView);
			htmlTextView.filters = [InitScene.dropFilter];
			
			var tmf:TextFormat =new TextFormat();
			tmf.align = TextFormatAlign.LEFT;
			tmf.color = 0xffffff;
			tmf.size = 16;
			htmlTextView.wordWrap = true;
			htmlTextView.defaultTextFormat=tmf;
		}
		
		override public function onAdded(e:Event):void{
			super.onAdded(e);
			bg.graphics.beginFill(0xffffff,0.5);  
			// mi 2s is 1280*720
			bg.graphics.drawRoundRect(0,0,this.parent.stage.stageWidth - 60,this._back.y - 80 ,10,10);
			bg.x = (this.parent.stage.stageWidth - bg.width)/2;
			htmlTextView.width = bg.width - 60;
			htmlTextView.height = bg.height - 60;
			htmlTextView.htmlText = "1.老公心里只有老婆一个人,做梦也要梦见老婆2.老公不允许骂老婆,打老婆,踢老婆,捏老婆,欺负老婆3.老婆打老公,老公不能还手4.老公永远要说老婆最漂亮，最可爱5.老公在老婆面前不能提别的女人6.老公不能说老婆老了丑了7.老公不可以丢下老婆,不要老婆8.老公QQ资料里要写老婆的名字,不能写其他人的名字和QQ号.9.老婆比老公的姐妹要重要10.老公的手指甲要干净";
			htmlTextView.x = 30;
			htmlTextView.y = 30;
			
			var v1:VirtualGiftView = new VirtualGiftView("至高权限",3,2);
			v1.x = bg.x;
			v1.y = bg.y+bg.height+6;
			this.addChild(v1);
			
			var v2:VirtualGiftView = new VirtualGiftView("无底线银行卡",4,1);
			v2.x = v1.x + v1.width;
			v2.y = bg.y+bg.height+6;
			this.addChild(v2);
			
			var v3:VirtualGiftView = new VirtualGiftView("全身大按摩",5,5);
			v3.x = v2.x + v2.width;
			v3.y = bg.y+bg.height+6;
			this.addChild(v3);
		}
	}
}