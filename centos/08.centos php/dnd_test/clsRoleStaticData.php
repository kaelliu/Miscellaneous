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
	public $buffsOnBody = array();// buffid=>lastround
	public $currentTarget = array();
	public $buffAddingValue = array();// buff effect value,type=>convert to value
	public $defendEffect;// clsDefendDataStruct

	public function __construct(){
		$this->defendEffect = new clsDefendDataStruct();
	}
}
