package lib.kael
{
	import com.greensock.TweenLite;
	import com.greensock.easing.Circ;
	
	import flash.display.DisplayObject;
	import flash.display.SimpleButton;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFormat;

	public class MyExtendedBtn extends Sprite
	{
		protected var _back:Sprite;
		protected var _label:TextField  = new TextField();
		public var btnFunc:Function;
		public function MyExtendedBtn(text:String,func:Function=null)
		{
			_label.y = 5;
			_label.selectable = false;
			_label.mouseEnabled = false;
			_label.filters=[InitScene.dropFilter];
			btnFunc = func;
			//创建按钮的不同状态  
			
			_back = createCircle(0x00FF00, 15,_label,text);//为click事件添加监听器  
			_back.addEventListener(MouseEvent.MOUSE_DOWN,handleTouchIn);  
			_back.addEventListener(MouseEvent.MOUSE_UP,handleTouched); 
			//			_back.addEventListener(TouchEvent.TOUCH_BEGIN,handleTouchIn);  
			//		    _back.addEventListener(TouchEvent.TOUCH_END,handleTouched); 
			
			this.addChild(_back);
		}
		
		
		public static function createCircle(color:uint, radius:Number,logLabel:TextField,text:String):Sprite{  
			
			logLabel.text = text;
			logLabel.width = logLabel.textWidth + 4;
			logLabel.height = logLabel.textHeight + 10;
			
			var lf:TextFormat = new TextFormat("宋体",12,0xffffff);
			logLabel.setTextFormat(lf);
			
			var shape:Sprite=new Sprite();
			shape.graphics.beginFill (color,0.5);
			shape.graphics.drawRoundRect(0,0,radius*4,radius*2,5,5);
			shape.graphics.endFill();
			shape.addChild(logLabel);
			logLabel.x = (shape.width - logLabel.width )/2;
			
			return shape;  
		}  
		
		private function handleTouchIn(event:Event):void{  
			TweenLite.to(event.target,0.25, {scaleX:1.2, scaleY:1.2, ease:Circ.easeOut});
		} 
		
		private function handleTouched(event:Event):void{ 
			TweenLite.to(event.target,0.25, {scaleX:1, scaleY:1, ease:Circ.easeOut});
			if(btnFunc){
				btnFunc();
			}
		} 
	}
}