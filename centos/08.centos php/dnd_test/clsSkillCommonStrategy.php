<?php
class clsSkillCommonStrategy implements clsSkillComputeStrategy {

	protected function _doBuffAdd($obj,$sd){
		foreach($sd->buffs as $buffid){
			$bd = clsBuffFactory::getBuffStatic($buffid);
			if($bd == null){
				continue;
			}
			// 对自己或者是对目标
			if($bd->target == TARGET_ENEMY){// 敌方
				foreach($obj->currentTarget as $tar){
					clsBuffCommonStrategy::onBuffAdd($tar,$bd);
				}
			}else{
				clsBuffCommonStrategy::onBuffAdd($obj,$bd);
			}
		}	
	}

	public function onHarmFormula($obj,$targets,$sd){
		// pre compute value
		foreach($obj->currentTarget as $tar){
			// 预先计算被攻击目标buff的影响
			// 命中率 = 基础50%命中率 + role.int * 0.8 * (1+buffaddingRate)+buffaddingValue
			// 是否命中
			$tar->defendEffect->init();
			$myHitRate = $obj->int * 0.8 + 50;
			$x1 = rand(1,100);
			if($x1 < $myHitRate){// hit
				// 躲闪率 = role.dex * 0.67 / 100 + buffAdding.duoshan_rate
				$duoshan = $tar->dex * 0.67 ;
				// 获得最终值
				// $duoshan = $target->getEffectedFinValue($duoshan,DUOSHAN);
				$x2 = rand(1,100);
				if($x2 < $duoshan){ // enemy duoshan
					// 必中率 = 100% for now
					$bizhong = true;
					if($bizhong){ // 我触发必中,抵消躲闪效果
						$tar->defendEffect->bizhong = true;	
						$tar->defendEffect->hit = true;
					}else{
						//$tar->defendEffect->hit = false;
						$tar->defendEffect->duoshan = true;
					}
				}else{
					$tar->defendEffect->hit = true;
					//$tar->defendEffect->duoshan = false;
				}
			}else{
				//$tar->hit = false;
			}
			if($tar->defendEffect->hit){
				// 是否被格挡
				// 格挡率 = role.pow * 0.41
				$gedang = $tar->pow * 0.41;
				$x3 = rand(1,100);
				if($x3 < $gedang){// 这孙子格挡了			
					// 但是我有破击...
					// 破击率 = role.int * 0.24
					$poji = $obj->int * 0.24;
					$x4 = rand(1,100);
					if($x4 < $poji){
						$tar->defendEffect->poji = true;
					}else{
						$tar->defendEffect->gedang = true;
					}
				}
				if($tar->defendEffect->gedang === false){// 没有格挡继续走
					// 暴击值 = (dex * 2.3)
					// 暴击值*0.2/100 = 暴击率 
					// 暴击比 = 暴击率随机值(必须产生暴击,小于暴击率)/暴击率最大值
					// 暴击伤害=暴击值*(暴击比) + (1 + (暴击比)) * 基础伤害
					$myBaseBaoji = $obj->dex * 2.3;
					$x5 = rand(1,100);
					$baoji = ($myBaseBaoji*0.2);///100;
					if($x5 < $baoji){// 
						// 孙子是否绝防了我的暴击...
						// juefang = 0 for now
						$juefang = 0;
						if($juefang != 0){// juefang
							$tar->defendEffect->juefang = true;
						}else{
							$rate = $x5/$baoji;
						
							$tar->defendEffect->criticalHit = $rate * $myBaseBaoji;
							$tar->defendEffect->criticalR = $rate;
						}
					}
				}
			}

			// obj是否被反击
			// 反击率 = role.int * 0.31
			$fanji = $tar->int * 0.31;
			$x6 = rand(1,100);
			if($x6 < $fanji){
				$tar->defendEffect->fanji = true;
			}
		}
	}
	
	public function beAttackStep($obj,$fd,$tar){
		// 检查被攻击触发的BUFF
		if($this->beHitBuffTrigger($obj,$fd,$tar)==true){

		}else{
			// 有BUFF盾就不能反击
			// if dead,attack his body!
			$this->fanjiFormula($obj,$fd,$tar);
		}
		$tar->hp-=$tar->defendEffect->harm;
		if($tar->hp<=0){
			$fd->onRoleDeadCallback($tar);
		}	
	}

	// target->defendEffect->harm	
	public function beHitBuffTrigger($attacker,$fd,$target){
		// 盾反,吸血,免伤
		// 遍历被攻击者身上的BUFF!
		$have = false;
		foreach($target->buffsOnBody as $buffid=>$buffInfo){
			$buffContext = clsBuffFactory::getEntity($buffid);
			if($buffContext->getBuffStaticData()->autoOrBeattack == TRIGGER_BATT){
				$have = $buffContext->doLogic($target,$attacker,$fd);
			}
		}
		return $have;
	}

	public function fanjiFormula($obj,$fd,$tar){
		// rules
		// 反击 - 如果需要连反,可视将反击为普通攻击行为 - 对behaviourTemplate做继承实现自己逻辑
		// BUFF反伤类似
		// 死亡 - 死亡行为对behaviourTemplate做继承实现自己逻辑,做释放死亡技能处理
		if($tar->defendEffect->fanji){
			$harm2 = ($tar->pow * 1.23);
			$obj->hp-=$harm2;
			// command record,use echo here for temp
			echo 'side:'.$tar->side.' opSlot '. $tar->slot . ' fanji,hit slot '. $obj->slot . ' for:' . $harm2 . '<br>';
			if($obj->hp<=0){
				$fd->onRoleDeadCallback($obj);
				return false;// attacker dead,do not do next logic
			}
		}
		return true;
	}
}
