<?php
// bufflogic 1 - dot/hot ing
class bul1 extends clsBuffCommonStrategy {
	public function onBuffLogic($obj,$tar,$fd,$bd){
		if($bd->dotOrPersisit){
			// dot
			$obj->hp -= $obj->buffsOnBody[$bd->id]->buffAddValue[$bd->targetProperty];
			if($obj->hp<=0){
				$fd->onRoleDeadCallback($obj);
				return false;// this guy is dead,if dead skill trigger,will handle in oRDCallback
			}
			// command record
			echo 'slot:' . $obj->slot . ' harm by buff for value:' . $obj->buffsOnBody[$bd->id]->buffAddValue[$bd->targetProperty] . '<br>';
		}else{ //persist add hp
			// do nothing
		}	
		return true;
	}
}
