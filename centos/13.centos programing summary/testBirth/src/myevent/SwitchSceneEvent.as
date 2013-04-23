package myevent
{
	import flash.events.Event;

	public class SwitchSceneEvent extends Event
	{
		public var from:Object;
		public var to:Object;
		public static const SWITCHSCENE_EVENT:String="switchScene";
		public function SwitchSceneEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type,bubbles,cancelable);
		}
	}
}