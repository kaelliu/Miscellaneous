package lib.kael
{
	import com.greensock.TweenLite;
	import com.greensock.easing.Circ;
	
	import flash.display.Shape;
	import flash.display.SimpleButton;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TouchEvent;
	import flash.text.TextField;
	import flash.text.TextFormat;

	public class CommonYesnoView extends Sprite
	{
		private var _yes:SimpleButton;
		private var _no:SimpleButton;
		public var onYes:Function;
		public var onNo:Function;
		private var _yesLabel:TextField  = new TextField();
		private var _noLabel:TextField  = new TextField();
		public function CommonYesnoView()
		{
			_yes = new SimpleButton();   
			//创建按钮的不同状态  
			_yes.upState = createCircle(0x00FF00, 15,_yesLabel,"确定");  
			_yes.overState = createCircle(0xFFFFFF, 16,_yesLabel,"确定");  
			_yes.downState = createCircle(0xCCCCCC, 15,_yesLabel,"确定");  
			_yes.hitTestState = _yes.upState;  
			//为click事件添加监听器  
			_yes.addEventListener(MouseEvent.MOUSE_DOWN,handleTouchIn);  
			_yes.addEventListener(MouseEvent.MOUSE_UP,handleTouched); 
			
//			_yes.addEventListener(TouchEvent.TOUCH_BEGIN,handleTouchIn);  
//			_yes.addEventListener(TouchEvent.TOUCH_END,handleTouched);  
			//添加到显示清单上  
			addChild(_yes); 
			
			_no = new SimpleButton();   
			_no.x = _yes.width + 6;
			//创建按钮的不同状态  
			_no.upState = createCircle(0x00FF00, 15,_noLabel,"取消");  
			_no.overState = createCircle(0xFFFFFF, 16,_noLabel,"取消");  
			_no.downState = createCircle(0xCCCCCC, 15,_noLabel,"取消");  
			_no.hitTestState = _yes.upState;  
			//为click事件添加监听器  
			_no.addEventListener(MouseEvent.MOUSE_DOWN,handleTouchIn);  
			_no.addEventListener(MouseEvent.MOUSE_UP,handleTouched);  
//			_no.addEventListener(TouchEvent.TOUCH_BEGIN,handleTouchIn);  
//			_no.addEventListener(TouchEvent.TOUCH_END,handleTouched);  
			//添加到显示清单上  
			addChild(_yes); 
			
			_yesLabel.x = 12;
			_yesLabel.y = 5;
			_yesLabel.selectable = false;
			_yesLabel.mouseEnabled = false;
			
			_noLabel.x = 12;
			_noLabel.y = 5;
			_noLabel.selectable = false;
			_noLabel.mouseEnabled = false;
		}
		
		public static function createCircle(color:uint, radius:Number,logLabel:TextField,text:String):Sprite{  
			
			logLabel.text = text;
			logLabel.width = logLabel.textWidth + 10;
			logLabel.height = logLabel.textHeight + 10;
		
			var lf:TextFormat = new TextFormat("宋体",12,0xffffff);
			logLabel.setTextFormat(lf);
			
			var shape:Sprite=new Sprite();
			shape.graphics.beginFill (color);
			shape.graphics.drawRect(0,0,50,26);
			shape.graphics.endFill();
			shape.addChild(logLabel);
			return shape;  
		}  
		
		private function handleTouchIn(event:Event):void{  
			TweenLite.to(event.target,0.25, {scaleX:1.2, scaleY:1.2, ease:Circ.easeOut});
		} 
		
		private function handleTouched(event:Event):void{ 
			TweenLite.to(event.target,0.25, {scaleX:1, scaleY:1, ease:Circ.easeOut});
			if(event.target == _yes){
				if(onYes){
					onYes();
				}
			}
			else if(event.target == _no){
				if(onNo){
					onNo();
				}
			}
		} 
	}
}