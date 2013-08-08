<?php
class clsRoleStaticData {
	public $roleId;
	public $slot;
	public $side;
	public $mainSkill = array(); // -1 means this slot is empty
	public $passiveSkill = -1;
	public $leaderSkill = -1;
	public $rage = 0;
	public $hp = 0;
	// 3 main property effect on secondary property
	// mingzhong is base 50%
	public $int = 0;// mingzhong,fanji,poji
	public $pow = 0;// defend,zhaojia,attack
	public $dex = 0;// speed,baoji,duoshan
	public $buffsOnBody = array();// buffid=>(clsBuffFightData)
	public $currentTarget = array();
	public $currentSkillID = 0; // 当前技能ID
	// 方便取最终加成值 - nouse
	//public $buffAddingValue = array();// buff effect value,type=>convert to value
	public $defendEffect;// clsDefendDataStruct
	
	public function __construct(){
		$this->defendEffect = new clsDefendDataStruct();
	}

	//public function getEffectedFinalValue($type){
	//	if(isset($buffAddingValue[$type]){
	//		return $buffAddingValue[$type];
	//	}else{
	//		return 0;
	//	}
	//}

	// another solution
	public function getEffectedFinValue($base,$type){
		$result = $base;
		foreach($buffsOnBody as $buffid=>$bfd){
			$val = $bfd->getValue($type);
			if($val!=0){
				$bd = clsBuffFactory::getBuffStatic($buffid);
				$result = clsBuffCommonStrategy::onBuffEffectOnValue($result,$bd->percentOrValue,$val);
			}
		}
		return $result;
	}
}
