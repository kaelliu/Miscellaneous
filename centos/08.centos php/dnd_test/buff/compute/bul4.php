<?php
// bufflogic 4 - sleep
class bul4 extends clsBuffCommonStrategy {
	// if sleep,change autoOrBeattack = TRIGGER_BOTH
	// when be attack,buff remove,before that,target 
	// can not move too
	public function onBuffLogic($obj,$tar,$fd,$bd){
		if($tar == null){// check stage by target
			return false;
		}else{
			if($tar->defendEffect->harm == 0){
				return false;
			}else{
				// remove it
				unset($tar->buffsOnBody[$bd->id]);
				return true;
			}
		}
		// unreachable
		//return false;
	}
}
