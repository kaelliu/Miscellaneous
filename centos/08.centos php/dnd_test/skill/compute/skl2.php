<?php
// skill logic 2 - ice gun
class skl2 extends clsSkillCommonStrategy {
	public function onHarmFormula($obj,$fd,$sd){
		// 立即伤害并挂1-N个BUFF	
		parent::onHarmFormula($obj,$fd,$sd);	
		foreach($obj->currentTarget as $tar){
			//if($tar->defendEffect->hit){ // 一定命中的啊魂淡不然我怎么测试
				// 伤害=智力*0.27+sd->value
				// 可被格挡,可被反击	
				$harm;
				if($tar->defendEffect->gedang){
					$harm = 1;
				}else{
					$harm = ($obj->int * 0.27) + $sd->value;
				}
				$tar->hp -= $harm;
				if($tar->hp<=0){
					$fd->onRoleDeadCallback($tar);
				}
				if(parent::fanjiFormula($obj,$fd,$tar) == false){
					break;// attacker dead
				}
				// 挂一个冰盾buff和一个dot buff
				parent:: _doBuffAdd($obj,$sd);
			//}

			// command record,use echo here for temp
			//if($tar->defendEffect->hit){
				echo 'side:'.$obj->side.' slot '. $obj->slot . ' attack opSlot ' . $tar->slot . ' for:'. $harm.' hp<br>';
				if($tar->defendEffect->criticalHit > 0){
					echo 'side:'.$obj->side.' slot '. $obj->slot . ' baoji:' . $tar->defendEffect->criticalHit . '<br>';
				}
				if($tar->defendEffect->poji){
					echo 'side:'.$obj->side.' slot '. $obj->slot . ' poji:' . $tar->defendEffect->poji . '<br>';
				}
				if($tar->defendEffect->bizhong){
					echo 'side:'.$obj->side.' slot '. $obj->slot . ' bizhong:' . $tar->defendEffect->bizhong . '<br>';
				}
				if($tar->defendEffect->duoshan){
					echo 'side:'.$tar->side.' opslot '. $tar->slot . ' duoshan:' . $tar->defendEffect->duoshan . '<br>';
				}
				if($tar->defendEffect->gedang){
					echo 'side:'.$tar->side.' opslot '. $tar->slot . ' gedang:' . $tar->defendEffect->gedang . '<br>';
				}
				if($tar->defendEffect->juefang){
					echo 'side:'.$tar->side.' opslot '. $tar->slot . ' juefang:' . $tar->defendEffect->juefang . '<br>';
				}
			//}else{
			//	echo 'side:'.$obj->side.' slot '. $obj->slot . ' missing attack <br>';
			//}
		}
		$obj->rage-=100;
		if($obj->rage<0){
			$obj->rage = 0;
		}
	}
}
