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
	public function doProcessBuff($team,$usedSkID){
		// 触发被动技能给自己挂BUFF的过程在战斗数据初始化
		// buff逻辑
		$this->_roleStaticData->currentSkillID = $usedSkID;// 如果某些BUFF需要对特定使用技能加成属性！
		foreach($this->_roleStaticData->buffsOnBody as $buffid=>$buffInfo){
			$buffContext = clsBuffFactory::getEntity($buffid);
			// check buff valid or not
			if($buffInfo->lastRound - 1 <= 0){
				//$lastrounds = 0;
				// remove it
				// 1.自动消失,按回合数
				// 2.主动消失,被攻击后消失或作用 -- 触发
				// 3.both,昏睡BUFF,不被攻击时不能动,被攻击BUFF消失	
				$buffContext->removeBuff($this->_roleStaticData);
				continue;
			}
			$buffInfo->lastRound -= 1;
			echo 'buff:'.$buffid.' left round:'.$buffInfo->lastRound.'<br>';
			// process my buff
			if(($buffContext->getBuffStaticData()->autoOrBeattack & TRIGGER_AUTO) != 0){
				$this->_goesOn = $buffContext->doLogic($this->_roleStaticData,null,$team);// if stun or somebuff,make role can not move
			}
		}
	}

	public function doChooseBehaviour(){
		$usedSkID = 0;
		if($this->_goesOn){
			// global behaviour
			// 合体技check or more rules
			// add rage every move
			$this->_roleStaticData->rage+=10;
			if($this->_roleStaticData->rage >= 100){
				foreach($this->_roleStaticData->mainSkill as $skill){
					$usedSkID = $this->_roleStaticData->mainSkill[$this->_currentSkCursor];
					echo 'side:'.$this->_roleStaticData->side.' slot:'.$this->_roleStaticData->slot.' pick up skill:' . $usedSkID.'<br>';
					$this->_currentSkCursor++;
					if($this->_currentSkCursor >= count($this->_roleStaticData->mainSkill))
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
		if($this->_goesOn){;
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
