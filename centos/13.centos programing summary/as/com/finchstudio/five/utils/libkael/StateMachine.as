package com.finchstudio.five.utils.libkael
{
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary; 

	public class StateMachine extends EventDispatcher
	{
		public var id:String;
		public var _currentState:String;
		public var _states:Dictionary;
		public var _outEvent:StateMachineEvent;
		public var parentState:State;
		public var parentStates:Array;
		public var path:Array;
		// entity who have this machine
		public var _owner:Object;
		
		/**
		 * Creates a generic StateMachine. Available states can be set with addState and initial state can
		 * be set using initialState setter.
		 * @example This sample creates a state machine for a player model with 3 states (Playing, paused and stopped)
		 * <pre>
		 *	playerSM = new StateMachine();
		 *
		 *	playerSM.addState("playing",new PlayingState());
		 *	playerSM.addState("paused",new PauseState("playing"));
		 *	
		 *	playerSM.addEventListener(StateMachineEvent.TRANSITION_DENIED,transitionDeniedFunction);
		 *	playerSM.addEventListener(StateMachineEvent.TRANSITION_COMPLETE,transitionCompleteFunction);
		 *	
		 *	playerSM.initialState = "stopped";
		 * </pre> 
		 *
		 * It's also possible to create hierarchical state machines using the argument "parent" in the addState method
		 * @example This example shows the creation of a hierarchical state machine for the monster of a game
		 * (Its a simplified version of the state machine used to control the AI in the original Quake game)
		 *	<pre>
		 *	monsterSM = new StateMachine()
		 *	
		 *	monsterSM.addState("idle",new IdleState());
		 *	monsterSM.addState("attack",new AttackState("idle"),"idle");// idle state should be add into statemachine's states first
		 *	
		 *	monsterSM.initialState = "idle"
		 *	</pre>
		 */
		public function StateMachine(){
			_states = new Dictionary();
		}
		
		public function Update( time:Number ):void{
			if( _currentState.length != 0 ){
				trace("[StateMachine]",id,"updating current state " + _currentState);
				_states[_currentState].update(time);
			}
		}
		
		public function addState(stateName:String, stateData:State,parent:String=""):void{
			if(stateName in _states) trace("[StateMachine]",id,"Overriding existing state " + stateName);
			if(stateData == null) trace("[StateMachine]",id,"add state failed " + stateName);
			stateData.parent = _states[parent];
			stateData.owner  = this;
			_states[stateName] = stateData;
		}
		
		public function set initialState(stateName:String):void{
			if (_currentState == null && stateName in _states){
				_currentState = stateName;
				
				var _callbackEvent:StateMachineEvent = new StateMachineEvent(StateMachineEvent.ENTER_CALLBACK);
				_callbackEvent.toState = stateName;
				
				if(_states[stateName].root){
					parentStates = _states[stateName].parents;
					for(var j:int = _states[stateName].parents.length-1; j>=0; j--){
						if(parentStates[j].enter){
							_callbackEvent.currentState = parentStates[j].name;
							parentStates[j].enter(_callbackEvent);
						}
					}
				}
				
				if(_states[_currentState].enter){
					_callbackEvent.currentState = _currentState;
					_states[_currentState].enter(_callbackEvent);
				}
				_outEvent = new StateMachineEvent(StateMachineEvent.TRANSITION_COMPLETE);
				_outEvent.toState = stateName;
				dispatchEvent(_outEvent);
			}
		}
		
		public function get state():String{
			return _states[_currentState];
		}
		
		public function get states():Dictionary{
			return _states;
		}
		
		public function getStateByName( name:String ):State{
			for each( var s:State in _states ){
				if( s.name == name )
					return s;
			}
			
			return null;
		}
		
		public function canChangeStateTo(stateName:String):Boolean{
			return (stateName!=_currentState && (_states[stateName].from.indexOf(_currentState)!=-1 || _states[stateName].from == "*"));
		}
		
		public function findPath(stateFrom:String, stateTo:String):Array{
			// Verifies if the states are in the same "branch" or have a common parent
			var fromState:State = _states[stateFrom];
			var c:int = 0;
			var d:int = 0;
			while (fromState)
			{
				d=0;
				var toState:State = _states[stateTo];
				while (toState)
				{
					if(fromState == toState)
					{
						// They are in the same brach or have a common parent Common parent
						return [c,d];
					}
					d++;
					toState = toState.parent;
				}
				c++;
				fromState = fromState.parent;
			}
			// No direct path, no commom parent: exit until root then enter until element
			return [c,d];
		}
		
		public function changeState(stateTo:String):void{
			// If there is no state that maches stateTo
			if (!(stateTo in _states)){
				trace("[StateMachine]",id,"Cannot make transition: State "+ stateTo +" is not defined");
				return;
			}
			
			// If current state is not allowed to make this transition
			if(!canChangeStateTo(stateTo)){
				trace("[StateMachine]",id,"Transition to "+ stateTo +" denied");
				_outEvent = new StateMachineEvent(StateMachineEvent.TRANSITION_DENIED);
				_outEvent.fromState = _currentState;
				_outEvent.toState = stateTo;
				_outEvent.allowedStates = _states[stateTo].from;
				dispatchEvent(_outEvent);
				return;
			}
			
			// call exit and enter callbacks (if they exits)
			path = findPath(_currentState,stateTo);
			if(path[0]>0){
				// exit current state
				var _exitCallbackEvent:StateMachineEvent = new StateMachineEvent(StateMachineEvent.EXIT_CALLBACK);
				_exitCallbackEvent.toState = stateTo;
				_exitCallbackEvent.fromState = _currentState;
				
				if(_states[_currentState].exit){
					_exitCallbackEvent.currentState = _currentState;
					_states[_currentState].exit(_exitCallbackEvent);
				}
				parentState = _states[_currentState];
				for(var i:int=0; i<path[0]-1; i++){
					parentState = parentState.parent;
					if(parentState.exit != null){
						_exitCallbackEvent.currentState = parentState.name;
						parentState.exit(_exitCallbackEvent);
					}
				}
			}
			var oldState:String = _currentState;
			_currentState = stateTo;
			if(path[1]>0){
				// enter next state
				var _enterCallbackEvent:StateMachineEvent = new StateMachineEvent(StateMachineEvent.ENTER_CALLBACK);
				_enterCallbackEvent.toState = stateTo;
				_enterCallbackEvent.fromState = oldState;
				
				if(_states[stateTo].root){
					parentStates = _states[stateTo].parents
					for(var k:int = path[1]-2; k>=0; k--){
						if(parentStates[k] && parentStates[k].enter){
							_enterCallbackEvent.currentState = parentStates[k].name;
							parentStates[k].enter(_enterCallbackEvent);
						}
					}
				}
				if(_states[_currentState].enter){
					_enterCallbackEvent.currentState = _currentState;
					_states[_currentState].enter(_enterCallbackEvent);
				}
			}
			trace("[StateMachine]",id,"State Changed to " + _currentState);
			
			// Transition is complete. dispatch TRANSITION_COMPLETE
			_outEvent = new StateMachineEvent(StateMachineEvent.TRANSITION_COMPLETE);
			_outEvent.fromState = oldState ;
			_outEvent.toState = stateTo;
			dispatchEvent(_outEvent);
		}
	}
}
