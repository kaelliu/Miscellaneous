<?php
class clsRoleBehaviour extends clsBehaviourTemplate {
	private $_roleStaticData;
	private $_goesOn=true;
	private $_currentSkCursor=0;
	public function __construct($rd){
		$this->_roleStaticData = $rd;
	}
	// 
	// normal action/skill action/special action/
	// solution 1,behaviour depend on detail action
	// solution 2,behaviour depend on an interface and can be implete by inheired
	// solution 3,every thing is driven by a 'skillcontext',even as a normal attack,not need ioc by this way
	// role behaviour can not depend on concrete class of behaviour,we use ioc to
	// let concrete behaviour depend on interface which rolebehaviour supply.
	private $_skillContext;
	// it need a enemy team here if a buff can effect other one's hp like burning buff on myself 
	// can take effect on other people
	public function doProcessBuff(){

		// 检查被动技能产生的永久BUFF,放在BUFF表里
		
		// buff逻辑
		foreach($this->_roleStaticData->buffsOnBody as $buffid=>$lastrounds){
			// check buff valid or not
			if($lastrounds - 1 <= 0){
				$lastrounds = 0;
				// remove it
				continue;
			}

			$buffContext = clsBuffFactory::getEntity($buffid);
			$buffContext->doLogic($this->_roleStaticData);
		}
	}

	public function doChooseBehaviour(){
		$usedSkID = 0;
		if($this->_goesOn){
			// global behaviour
			// 合体技check or more rules
			if($this->_roleStaticData->rage > 100){
				foreach($this->_roleStaticData->mainskill as $skill){
					$usedSkID = $this->_roleStaticData->mainskill[$this->_currentSkCursor];
					$this->_currentSkCursor++;
					if($this->_currentSkCursor >= count($this->_roleStaticData->mainskill))
					{
						$this->_currentSkCursor = 0;
					}
					break;
				}
			}
		}
		return $usedSkID;
	}

	public function doPickUpTarget($team,$usedSkID){
		if($this->_goesOn){
			$this->_skillContext = clsSkillFactory::getEntity($usedSkID);
			if($this->_skillContext!=null){
				$this->_roleStaticData->currentTarget = $this->_skillContext->pickupTarget($this->_roleStaticData,$team);
				if($this->_roleStaticData->currentTarget == null){
					$this->_goesOn=false;
					return;
				}
			}else{
				$this->_goesOn=false;
				return;
			}
		}
	}

	public function doOneAction($team){
		if($this->_goesOn){
			$command = $this->_skillContext->doLogic($this->_roleStaticData,$team); // call skill context by skillid and do skill data handle logic
			$this->_outputOneAction($command);
		}
	}

	private function _outputOneAction($command){

	}
}
