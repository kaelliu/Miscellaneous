<?php
// bufflogic 3 - stun
class bul3 extends clsBuffCommonStrategy {
	// autoOrBeattack = TRIGGER_AUTO
	// return false to block next move...
	// can not remove by attack
	public function onBuffLogic($obj,$tar,$fd,$bd){
		return false;
	}
}
