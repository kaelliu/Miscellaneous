<?php

class clsSkillFactory {
	// skillid => condition class id,compute class id
	private static $_matchupConfig=array("0"=>array("0","0"),"1"=>array("0","0"),"2"=>array("1","0"));
	final static public function getEntity($skillId){
		$skillIndex = self::$_matchupConfig[$skillId];
		$skcond = "skc" . $skillIndex[0];
		$skcomp = "skl" .  $skillIndex[1];
		require DIR_FILE_PATH . "/skill/condition/". $skcond . ".php";
		require DIR_FILE_PATH . "/skill/compute/" . $skcomp . ".php";
		$staticData =  new clsSkillStaticData();
		$staticData->id = $skillId;
		$staticData->value=123;// blabla
		if (class_exists($skcond) && class_exists($skcomp)){
			$context = new clsSkillContext($staticData,new $skcomp,new $skcond);
			return $context;
		}else{
			return null;
		}
	}
}
