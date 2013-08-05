<?php

class clsBuffFactory {
	// buffid => compute class id
	private static $_matchupConfig=array("1"=>1,"2"=>1);
	final static public function getEntity($buffId){
		$buffIndex = _matchupConfig[$buffId];
		$bfcomp = "bul" .  $buffIndex;
		require DIR_BUFFS . "/buff/compute/" . $bfcomp . "php";
		$staticData =  new clsBuffStaticData();
		$staticData->id = $buffId;
		$staticData->value=123;// blabla
		if (class_exists($bfcomp)){
			$context = new clsBuffContext($staticData,new $bfcomp);
			return $context;
		}else{
			return null;
		}
	}
}
