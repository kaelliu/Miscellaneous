package com.finchstudio.five.utils.libkael
{
	// an entity is role in war
	public class Entity
	{
		public var _agent:Agent ;
		public var _fsm:StateMachine;
		public var _showobj:Object = {};
		public function Entity(fsm:StateMachine){
			_fsm = fsm;
			_agent = new Agent(this);
		}
		
		public function assignAgent(lst:Array,startnow:Boolean=false):void{
			while(lst.length != 0){
				_agent._sequence.push(lst.shift());
			}
			
			if(startnow){
				_agent.doit();
			}
		}
		
		public function stateStepOver():void{
			_agent.doit();
		}
	}
}
