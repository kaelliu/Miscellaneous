<?php

class clsSkillFactory {
	// skillid => condition class id,compute class id
	// 测试数据
	// 普通攻击,技能攻击,治疗全体,附加buff技能
	private static $_matchupConfig=array("0"=>array("0","0"),"1"=>array("0","1"),"2"=>array("0","2"),"3"=>array("0","3"));
	// 负值加血
	private static $_skillStaticData=array("0"=>array("val"=>0,"ez"=>1),"1"=>array("val"=>123,"ez"=>1),"2"=>array("val"=>20,"ez"=>1,"buff"=>array("1","2")),"3"=>array("val"=>110,"ez"=>6));
	final static public function getEntity($skillId){
		$skillIndex = self::$_matchupConfig[$skillId];
		$skcond = "skc" . $skillIndex[0];
		$skcomp = "skl" .  $skillIndex[1];
		require_once DIR_FILE_PATH . "/skill/condition/". $skcond . ".php";
		require_once DIR_FILE_PATH . "/skill/compute/" . $skcomp . ".php";
		if (class_exists($skcond) && class_exists($skcomp)){
			$context = new clsSkillContext(self::getSkillStatic($skillId),new $skcomp,new $skcond);
			return $context;
		}else{
			return null;
		}
	}

	final static public function getSkillStatic($skillId){
		if(isset(self::$_skillStaticData[$skillId])){
			$staticData =  new clsSkillStaticData();
			$staticData->id = $skillId;
			$staticData->value= self::$_skillStaticData[$skillId]["val"];// blabla
			$staticData->effectZone=self::$_skillStaticData[$skillId]["ez"];
			if(isset(self::$_skillStaticData[$skillId]["buff"])){
				$staticData->buffs=self::$_skillStaticData[$skillId]["buff"];
			}
			return $staticData;
		}else{
			return null;
		}
	}
}
