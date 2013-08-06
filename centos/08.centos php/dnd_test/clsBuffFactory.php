<?php

class clsBuffFactory {
	// buffid => compute class id
	private static $_matchupConfig=array("1"=>1,"2"=>1);
	private static $_buffConfig=array("1"=>array("tar"=>1,"bon"=>2,"p"=>8,"v"=>100,"lr"=>3,"pov"=>1),"2"=>array("tar"=>2,"bon"=>1,"p"=>8,"v"=3,"lr"=>2,"pov"=>2));
	final static public function getEntity($buffId){
		$buffIndex = self::$_matchupConfig[$buffId];
		$bfcomp = "bul" .  $buffIndex;
		require_once DIR_BUFFS . "/buff/compute/" . $bfcomp . "php";
		$staticData =  new clsBuffStaticData();
		$staticData->id = $buffId;
		$staticData->value=self::$_buffConfig[$buffId]["v"];// blabla
		$staticData->target=self::$_buffConfig[$buffId]["tar"];
		$staticData->buffOrNerf=self::$_buffConfig[$buffId]["bon"];
		$staticData->leftRound=self::$_buffConfig[$buffId]["lr"];
		$staticData->percentOrValue=self::$_buffConfig[$buffId]["pov"];
		$staticData->targetProperty=self::$_buffConfig[$buffId]["p"];
		if (class_exists($bfcomp)){
			$context = new clsBuffContext($staticData,new $bfcomp);
			return $context;
		}else{
			return null;
		}
	}
}
