package
{
	import flash.display.MovieClip;
	import flash.display.Sprite;
	
	[SWF(backgroundColor = 0xffffff, width = 1024, height = 650, frameRate = 30)]
	public class MemoryView extends Sprite
	{
		var book_container:MovieClip=new MovieClip();
		var mypageflip:PageFlipClass=new PageFlipClass();
		var myXML:XML =
			<content width="300" height="400">
				<page src="../assets/pic/1.swf"/>
				<page src="../assets/pic/2.png"/>
			</content>;
		public function MemoryView()
		{
			book_container.x=10;
			book_container.y=10;
			addChild(book_container);
			mypageflip.myXML=myXML;
			mypageflip.book_root=book_container;
			mypageflip.book_initpage=0;
			mypageflip.book_TimerNum=30;
			mypageflip.InitBook();
			mypageflip.onPageEnd = pageOver;
		}
		
		private function pageOver():void{
			var page:int = mypageflip.book_page;
			BaseSound.getInstance().playMusic(page);
		}
	}
}