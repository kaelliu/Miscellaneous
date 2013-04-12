package com.finchstudio.five.utils.libkael
{
	public class State
	{
		// may be extends and have own enter/exit function
		public var name:String;
		public var from:Object;
		public var _parent:State;
		public var children:Array;
		public var oneshot:Boolean = false;// is one shot state
		// fsm who have this state
		public var owner:Object;
		// need extends and override this
		public function enter(param:Object):void{
		
		}
		
		public function exit(param:Object):void{
			
		}
		
		// state update function
		public function update(param:Object):void{
			
		}
		
//		// do detail logic to decide need entity or not,and when enter over,logic finished,we do oneshotcomplete
//		public function onOneShotComplete():void{
////			owner._owner.stateStepOver();
//		}
		
		public function State(name:String, from:Object = null ,parent:State = null){
			this.name = name;
			if (from==null) from = "*";
			this.from = from;
			this.children = [];
			if(parent){
				_parent = parent;
				parent.children.push(this);
			}
		}
		
		public function set parent(parent:State):void{
			if(parent){
				_parent = parent;
				_parent.children.push(this);
			}
		}
		
		public function get parent():State{
			return _parent;
		}
		
		public function get root():State{
			var parentState:State = _parent;
			if(parentState){
				while (parentState.parent){
					parentState = parentState._parent;
				}
			}
			return parentState;
		}
		
		public function get parents():Array{
			var parentList:Array = [];
			var parentState:State = _parent;
			if(parentState){
				parentList.push(parentState);
				while (parentState.parent){
					parentState = parentState.parent;
					parentList.push(parentState);
				}
			}
			return parentList;
		}
		
		public function toString():String{
			return this.name;
		}
	}
}
