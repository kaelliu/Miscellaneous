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
			htmlTextView.htmlText = "    7个夜晚，2199行代码，也不能表达完我对老婆大人的爱意。一路陪伴走来，你是我最爱的老婆，在我低潮的时候支持我，在我迷茫的时候鼓励我。没有五彩的鲜花，没有浪漫的诗句，没有贵重的礼物，没有兴奋的惊喜，我用我自己的想法和表达方式，做视频，做程序，为老婆大人送上生日最美好的祝福：祝你生日快乐，未来的每一个生日，我都会陪伴在你的左右。我没有美工，确实做得简陋得很，亲爱的不要生气哟。\n    说了这么多，还是要送你下面的三大礼物，终生有效哟亲！威力强大，请妥善使用啦~";
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