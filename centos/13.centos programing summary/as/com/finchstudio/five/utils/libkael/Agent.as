package com.finchstudio.five.utils.libkael
{
	// an agent is a sequence state change 
	public class Agent
	{
		public var _sequence:Vector.<String> = new Vector.<String>;
		public var _sequenceParam:Vector.<Object> = new Vector.<Object>;
		public var _entity:Entity;
		public var _isdoing:Boolean = false;
		public var _isover:Boolean = false;
		public function Agent(entity:Entity){
			_entity = entity;
		}
		
		private function doOneAgent():void{
			if(!_isdoing)
			{
				var newState:String = _sequence.shift();
				var enterParam:Object = _sequenceParam.shift();
				if(newState != null){
					_entity._fsm.changeState(newState);
					_isover = false;
				}
				else{
					_entity._fsm.changeState("idle");
					_isover = true;
				}
				_isdoing = true;
			}
		}
		
		public function doit():void{
			doOneAgent();
		}
	}
}
