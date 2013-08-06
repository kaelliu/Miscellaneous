<?php
// skill logic 0 - normal attack
class skl0 extends clsSkillCommonStrategy {
	// obj - clsRoleStaticData
	// fd  - fightData
	// sd  - clsSkillStaticData
	public function onHarmFormula($obj,$fd,$sd){
		// step 01 normal
		// formula = pow * 1.23
		parent::onHarmFormula($obj,$fd,$sd);	
		foreach($obj->currentTarget as $tar){
			if($tar->defendEffect->hit){
				$pow = $obj->pow;//+ $obj->buffAddingValue["POW"];
				$harm;
				if($tar->defendEffect->gedang){
					$harm = 1;
				}else{
					$harm = ($pow * 1.23) * (1+$tar->defendEffect->criticalR)+$tar->defendEffect->criticalHit;
				}
				$tar->hp -= $harm;
			}
			if($tar->hp<=0){
				$fd->onRoleDeadCallback($tar);
			}
			if($tar->defendEffect->fanji){
				$harm2 = ($tar->pow * 1.23);
				$obj->hp-=$harm2;
				// command record,use echo here for temp
				echo 'side:'.$tar->side.' opSlot '. $tar->slot . ' fanji,hit slot '. $obj->slot . ' for:' . $harm2 . '<br>';
				if($obj->hp<=0){
					$fd->onRoleDeadCallback($obj);
					break;// attacker dead,do not do next logic
				}
			}
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
		// step 2 add some factor,baoji,fanji
	}
}
