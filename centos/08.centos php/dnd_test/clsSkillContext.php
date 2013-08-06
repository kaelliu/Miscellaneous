<?php

class clsSkillContext {
	private $_skillStaticData;
	private $_compute;// implete staticdata,compute,condition callback in factory
	private $_conditionCheck=null;
	private $_pickup;
	public function __construct($sd,$comp,$cond){
		$this->_skillStaticData = $sd;
		$this->_compute = $comp;
		$this->_conditionCheck = $cond;
		$this->_pickup = new clsPickupCommon;
	}

	public function pickupTarget($rd,$team){
		if($this->_pickup == null){
			return null;
		}else{
			return $this->_pickup->pick($rd,$team,$this->_skillStaticData);
		}
	}

	// when do this,we consider it is be setting correct
	public function doLogic($obj,$team){
		if($this->_conditionCheck == null){
			return null;
		}else{
			if($this->_conditionCheck->onCondition($obj)){
				// do and change obj's detail data
				$this->_compute->onHarmFormula($obj,$team,$this->_skillStaticData);
			}
		}
	}
}
