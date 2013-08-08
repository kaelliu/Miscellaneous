<?php

class clsBuffFactory {
	// buffid => compute class id
	private static $_matchupConfig=array("1"=>1,"2"=>1);
	private static $_buffConfig=array("1"=>array(1,2,8,100,3,1,1,1),"2"=>array(1,1,17,200,2,2,0,2));
	final static public function getEntity($buffId){
		$buffIndex = self::$_matchupConfig[$buffId];
		$bfcomp = "bul" .  $buffIndex;
		require_once DIR_FILE_PATH . "/buff/compute/" . $bfcomp . ".php";
	
		if (class_exists($bfcomp)){
			$context = new clsBuffContext(self::getBuffStatic($buffId),new $bfcomp);
			return $context;
		}else{
			return null;
		}
	}

	final static public function getBuffStatic($buffId){
		if(isset(self::$_buffConfig[$buffId])){
			$staticData =  new clsBuffStaticData();
			$staticData->id = $buffId;		
			$staticData->target=self::$_buffConfig[$buffId][0];
			$staticData->buffOrNerf=self::$_buffConfig[$buffId][1];// mean less can be remove,use negative for healing
			$staticData->targetProperty=self::$_buffConfig[$buffId][2];
			$staticData->value=self::$_buffConfig[$buffId][3];// blabla
			$staticData->leftRound=self::$_buffConfig[$buffId][4];
			$staticData->percentOrValue=self::$_buffConfig[$buffId][5];
			$staticData->dotOrPersisit=self::$_buffConfig[$buffId][6];
			$staticData->autoOrBeattack=self::$_buffConfig[$buffId][7];
			return $staticData;
		}else{
			return null;
		}
	}
}
