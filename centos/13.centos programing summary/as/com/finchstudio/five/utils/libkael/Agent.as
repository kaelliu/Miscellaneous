package com.finchstudio.five.utils.libkael
{
	// an agent is a sequence state change 
	public class Agent
	{
		public var _sequence:Vector.<String> = new Vector.<String>;
		public var _entity:Entity;
		public var _isdoing:Boolean = false;
		public function Agent(entity:Entity){
			_entity = entity;
		}
		
		private function doOneAgent():void{
			if(!_isdoing)
			{
				var newState:String = _sequence.shift();
				if(newState != null){
					_entity._fsm.changeState(newState);
				}
				_isdoing = true;
			}
		}
		
		public function doit():void{
			doOneAgent();
		}
	}
}
