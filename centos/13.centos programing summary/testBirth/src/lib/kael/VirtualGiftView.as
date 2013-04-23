package lib.kael
{
	import flash.display.Shape;
	import flash.display.Sprite;
	import flash.text.TextField;
	import flash.text.TextFormat;
	
	import flashx.textLayout.formats.TextAlign;

	public class VirtualGiftView extends Sprite
	{
		private var star_lvl:Array=[];
		private var _name:TextField;
		private var count:int;
		private var background:Shape;
		public function VirtualGiftView(name:String,star:int,count:int)
		{
			background = new Shape;
			background.graphics.beginFill(0x00000,0.5);  
			background.graphics.drawRoundRect(0,0,160,70,10,10);   
			_name = new TextField();
			_name.filters = [InitScene.dropFilter];
			_name.selectable=false;
			_name.mouseEnabled = false;
			var tmf:TextFormat =new TextFormat();
			tmf.align = TextAlign.LEFT;
			tmf.color = 0xffffff;
			tmf.size = 20;
			_name.defaultTextFormat=tmf;
			_name.text = name + "  *"+5;
			_name.x = 3;
			_name.y = 3;
			_name.height = 32;
			_name.width = 160;
			this.addChild(background);
			for(var i:int=0;i<3;++i){
				var heart:MImage = new MImage("../assets/pic/favorite_love.png");
				star_lvl.push(heart);
				heart.y=_name.height;
				heart.x+=32*i;
				this.addChild(heart);
			}
			this.addChild(_name);
		}
	}
}