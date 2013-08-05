<?php

class clsSkillContext {
	private $_skillStaticData;
	private $_comupte;// implete staticdata,compute,condition callback in factory
	private $_conditionCheck=null;
	private $_pickup;
	public function __construct($sd,$comp,$cond){
		$_skillStaticData = $sd;
		$_compute = $comp;
		$_condtionCheck = $cond;
		//$_pickup = new jzzh
	}

	public function pickupTarget($team){
		if($_pickup == null){
			return null;
		}else{
			return $this->_pickup($team,$_skillStaticData);
		}
	}

	// when do this,we consider it is be setting correct
	public function doLogic($obj,$targets){
		if($_conditionCheck == null){
			return null;
		}else{
			if($_conditionChcek($obj)){
				// do and change obj's detail data
				$_comupte($obj,$targets,$_skillStaticData);
			}
		}
	}
}
