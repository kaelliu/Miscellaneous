<?php
// skill logic 1 - fire ball attack
class skl0 extends clsSkillCommonStrategy {
	public function onHarmFormula($obj,$fd,$sd){
		parent::onHarmFormula($obj,$fd,$sd);	
		foreach($obj->currentTarget as $tar){
			if($tar->defendEffect->hit){
				// 技能立即效果 sd->value
				// 立即造成XXX点伤害,角色怒气值减100
				// 特殊规则:无视格挡,不能反击
				$harm = $sd->value;
				$tar->hp -= $harm;	
			}
			if($tar->hp<=0){
				$fd->onRoleDeadCallback($tar);
			}

			// bla bla bla part
			// command record,use echo here for temp
			if($tar->defendEffect->hit){
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
			}else{
				echo 'side:'.$obj->side.' slot '. $obj->slot . ' missing attack <br>';
			}
		}
		$obj->rage-=100;
		if($obj->rage<0){
			$obj->rage = 0;
		}
	}
}
