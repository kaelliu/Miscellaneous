<?php
// common
class clsBuffCommonStrategy implements clsBuffComputeStrategy {
	static public function onBuffAdd($obj,$bd){
		// 是否 有同ID BUFF,刷新剩余回合数
		if(isset($obj->buffsOnBody[$bd->id])){
			$obj->buffsOnBody[$bd->id]->lastRound=$bd->leftRound;
			echo 'refresh buff on:'.$obj->slot;
		}else{
			// 角色添加buff
			$buffInfo = new clsBuffFightData;
			// 计算buff加成到角色的数值,用于后续计算和判断
			$buffInfo->lastRound = $bd->leftRound;
			$buffInfo->buffAddValue[$bd->targetProperty] = $bd->value;
			$obj->buffsOnBody[$bd->id]=$buffInfo;
			if(isset($obj->buffAddingValue[$bd->targetProperty])){
				$obj->buffAddingValue[$bd->targetProperty]+=$bd->value;
			}else{
				$obj->buffAddingValue[$bd->targetProperty]=$bd->value;
			}
			echo 'add buff on:'.$obj->slot;
		}
	}

	// v - 初始值
	// type - 类型 按比率-按值
	// addV - 需要的增量
	static public function onBuffEffectOnValue($v,$type,$addV){
		if($type == 1){
			return $v+$addV;
		}else if($type == 2){
			return $v*$(1+$addV);
		}
	}

	// rules
	// 1.buff can not do harm to enemy targets
	// 2.if do harm every round,use teamSkill to trigger skill every round
	// 3.if buff depend on some one's skill,like if use arcushot on role who
	// 4.same property should overwrite,can not have two same property 
	// have a buff named 'seek',can increase 30% hitrate,so input the skill id 
	// for special logic	
	//public function onBuffLogic($obj,$fd,$bd,$skid){}
	
	public function onBuffRemove($obj,$bd){
		// rules
		// 1.dot/hot 与血量相关的,buff消除后不恢复
		//   dot/hot 叠加属性的,buff消除后恢复
		// 2.persist 的根据逻辑变化的,buff消除后根据逻辑逆向算法恢复数值
		//   如:血量越少,加成力量越多,需要自实现逆向逻辑
		//   回合中阶段逻辑变化导致每回合不一样的属性变化,记录在角色
		//   buffOnBody对应clsBuffFightData结构中的总值移除
		if(isset($obj->buffsOnBody[$bd->id])){
			$obj->buffAddingValue[$obj->buffsOnBody[$bd->id]->targetProperty]-=$obj->buffsOnBody[$bd->id]->value;
			unset($obj->buffsOnBody[$bd->id]);

			echo 'remove buff on:'.$obj->slot;
		}
	}
}
