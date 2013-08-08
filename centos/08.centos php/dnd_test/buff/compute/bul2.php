<?php
// bufflogic 2 - shield
class bul2 extends clsBuffCommonStrategy {
	public function onBuffLogic($obj,$tar,$fd,$bd){
		$reflectValue = $tar->buffsOnBody[$bd->id]->buffAddValue[FANTAN];
		// s1.反弹一部分攻击,剩余变成自身伤害
		// s2.反弹全部攻击,value无效
		// 做的是s1方案
		
		$leftHarm = $tar->defendEffect->harm - $reflectValue;
		if($leftHarm > 0){
			$tar->defendEffect->harm = $leftHarm;
		}else{
			$tar->defendEffect->harm = 0;
		}
		$tar->buffsOnBody[$bd->id]->buffAddValue[FANTAN] -= $harm;
		if($tar->buffsOnBody[$bd->id]->buffAddValue[FANTAN]<=0){
			// 盾破
			echo 'shield be destory!';
			// remove buff
			unset($tar->buffsOnBody[$bd->id]);
		}
		// 反弹给我的
		$obj->hp-=$reflectValue;
		if($obj->hp<=0){
			$fd->onRoleDeadCallback($obj);
		}
		echo 'shield reflect harm:'.$reflectValue.'and harm me for:'.$leftHarm.'<br>';
		return true;
	}
}

