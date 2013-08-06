<?php
// skill logic 1 - fire ball attack
class skl0 extends clsSkillCommonStrategy {
	public function onHarmFormula($obj,$fd,$sd){
		parent::onHarmFormula($obj,$fd,$sd);	
		foreach($obj->currentTarget as $tar){
			if($tar->defendEffect->hit){
				// 技能立即效果 sd->value
			}
		}
	}
}
