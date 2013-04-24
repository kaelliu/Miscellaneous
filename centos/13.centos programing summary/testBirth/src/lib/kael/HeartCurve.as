package
{
	import flash.display.Sprite;
	import flash.events.*;
	import flash.geom.Point;
	import flash.geom.ColorTransform;
	import flash.display.Graphics;
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	public class HeartCurve extends Sprite
	{
		//四种方向心形线
		//r=a*(1+cos(angle))
		//r=a*(1-cos(angle))
		//r=a*(1+sin(angle))
		//r=a*(1-sin(angle))
		private var pen_up:Sprite;
		private var pen_down:Sprite;
		private var pen_left:Sprite;
		private var pen_right:Sprite;
		private var bmp:BitmapData;
		private var n:int=0;//数量
		private var maxStep:int=100;
		public function HeartCurve()
		{
			pen_up=this.clone();
			pen_down=this.clone();
			pen_left=this.clone();
			pen_right=this.clone();
			
			addChild(pen_up);
			addChild(pen_down);
			addChild(pen_left);
			addChild(pen_right);
			
			addEventListener(Event.ENTER_FRAME,Run);
		}
		
		private function clone():Sprite
		{
			return new Sprite();			
		}
		
		private function Run(event:Event):void{
			if (n<=maxStep)
			{
//				drawHeart(pen_up.graphics,n,100,new Point(250,200),"up");
				drawHeart(pen_down.graphics,n,30,new Point(250,200),"down");
//				drawHeart(pen_left.graphics,n,100,new Point(250,200),"left");
//				drawHeart(pen_right.graphics,n,100,new Point(250,200),"right");
			}
			else
			{
				stopDraw();
			}
			
			n++;
		}
		
		//停止绘制
		private function stopDraw():void
		{
			removeEventListener(Event.ENTER_FRAME,Run);
			trace("停止");
		}
		
		//绘制心形
		private function drawHeart(g:Graphics,num:int,radius:Number,p:Point,type:String):void
		{
			var angle:Number =2*Math.PI / maxStep * num;
			var r:Number;
//			switch (type)
//			{
//				case "up" :
//					r = radius*(1-Math.sin(angle));//上公式
//					break;
//				case "down" :
//					r= radius*(1+Math.sin(angle));//下公式
//					break;
//				case "left" :
//					r = radius*(1-Math.cos(angle));//左公式
//					break;
//				case "right" :
//					r = radius*(1+Math.cos(angle));//右公式
//					break;
//			}
			
			// flash坐标系y向下增加
//			r = radius * (Math.sin(angle) * Math.sqrt(Math.abs(Math.cos(angle))) / (Math.sin(angle) + 1.4) - 2 * Math.sin(angle) + 2);//心底朝上公式
//			r = radius * (Math.cos(angle) * Math.sqrt(Math.abs(Math.sin(angle))) / (Math.cos(angle) + 1.4) - 2 * Math.cos(angle) + 2);//心底左公式
//			r = radius * (Math.sin(angle+Math.PI) * Math.sqrt(Math.abs(Math.cos(angle+Math.PI))) / (Math.sin(angle+Math.PI) + 1.4) - 2 * Math.sin(angle+Math.PI) + 2);//心底朝下公式
//			r = radius * (Math.cos(angle+Math.PI) * Math.sqrt(Math.abs(Math.sin(angle+Math.PI))) / (Math.cos(angle+Math.PI) + 1.4) - 2 * Math.cos(angle+Math.PI) + 2);//心底右公式
			
			switch (type)
			{
				case "up" :
					r = radius * (Math.sin(angle) * Math.sqrt(Math.abs(Math.cos(angle))) / (Math.sin(angle) + 1.4) - 2 * Math.sin(angle) + 2);//上公式
					break;
				case "down" :
					r = radius * (Math.sin(angle+Math.PI) * Math.sqrt(Math.abs(Math.cos(angle+Math.PI))) / (Math.sin(angle+Math.PI) + 1.4) - 2 * Math.sin(angle+Math.PI) + 2);//下公式
					break;
				case "left" :
					r = radius * (Math.cos(angle) * Math.sqrt(Math.abs(Math.sin(angle))) / (Math.cos(angle) + 1.4) - 2 * Math.cos(angle) + 2);//左公式
					break;
				case "right" :
					r = radius * (Math.cos(angle+Math.PI) * Math.sqrt(Math.abs(Math.sin(angle+Math.PI))) / (Math.cos(angle+Math.PI) + 1.4) - 2 * Math.cos(angle+Math.PI) + 2);//右公式
					break;
			}
			
			var point:Point=Point.polar(r,angle);//极坐标转换笛卡尔坐标
			
			point.offset(p.x,p.y);//偏移
			trace(point);
			drawLine(g,point);
		}
		
		private function drawLine(g:Graphics,endPoint:Point):void
		{
			g.lineStyle(4);
			if (n==0)
			{
				g.moveTo(endPoint.x,endPoint.y);
				g.lineTo(endPoint.x,endPoint.y);
			}
			if (n>0)
			{
				g.lineTo(endPoint.x,endPoint.y);		
			}
		}
	}
}