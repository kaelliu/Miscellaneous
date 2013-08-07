<?php

function _cmp($a, $b){
	if ($a->dex == $b->dex) {
     	return 0;
    }
    return ($a->dex > $b->dex) ? -1 :1;
}

class clsFightData {
	public $teams = array();
	public $roundStack = array();// round data for parse
	public $deadTeamMates = array();
	public $draw;
	public $winaid;
	public $currentRound=0;
	public $currentActionIndex=0;
	//public $currentActionSideFlag=0;
	public $attackSequence = array();
	public $needRearrangeSequence = true;
	public $isOver=false;
	public function rearrangeSequence(){
		// sort by dex
		usort($this->attackSequence, "_cmp");
		$this->needRearrangeSequence=false;
	}

	// actully dead,not trigger dead skill
	public function onRoleDeadCallback($who){
		// move to dead queue
		$deadTeamMates[$who->slot] = $who;	
		unset($this->teams[$who->side][$who->slot]);
		// emit from attackSequence
		$idx = 0;
		foreach($this->attackSequence as $val){
			if($val->slot == $who->slot && $val->side == $who->side){
				unset($this->attackSequence[$idx]);
				break;
			}
			$idx++;
		}
		$this->needRearrangeSequence = true;
	}

	// get next mover
	public function getNextMover(){
		$idx = $this->currentActionIndex;
		++$this->currentActionIndex;
		if($this->currentActionIndex >= count($this->attackSequence)){
			$this->currentActionIndex = 0;
		}
		return $this->attackSequence[$idx];
	}

	// return winner side
	public function isover(){	
		if(count($this->teams["me"]) == 0)
    		return 1;
		else if(count($this->teams["ota"]) == 0)
    		return -1;
		else
    		return 0;
	}

	private function _initPassiveSkillBuff($att,$def){
		foreach($att as $tar){
			if($tar->passiveSkill != -1){
				$skd = clsSkillFactory::getSkillStatic($tar->passiveSkill);
				if($skd != null){
					foreach($skd->buffs as $buff){
						// 添加BUFF
						$buffInfo = new clsBuffFightData;
						$bd = clsBuffFactory::getBuffStatic($buff);
						if($bd!=null){
							$buffInfo->lastRound = $bd->leftRound;
							$buffInfo->buffAddValue[$bd->targetProperty] = $bd->value;
							$tar->buffsOnBody[$buff] = $buffInfo;
						}
					}
				}
			}
		}
		foreach($def as $tar){
			if($tar->passiveSkill != -1){
				$skd = clsSkillFactory::getSkillStatic($tar->passiveSkill);
				if($skd != null){
					foreach($skd->buffs as $buff){
						// 添加BUFF
						$buffInfo = new clsBuffFightData;
						$bd = clsBuffFactory::getBuffStatic($buff);
						if($bd!=null){
							$buffInfo->lastRound = $bd->leftRound;
							$buffInfo->buffAddValue[$bd->targetProperty] = $bd->value;
							$tar->buffsOnBody[$buff] = $buffInfo;
						}
					}
				}
			}
		}
	}

	public function initDataForTest(){
		$attackSide = array();// slot => roleStaticData
		$defendSide = array();
		$slot1 = new clsRoleStaticData;
		$slot2 = new clsRoleStaticData;
		$slot3 = new clsRoleStaticData;
		$slot4 = new clsRoleStaticData;
		$slot5 = new clsRoleStaticData;
		$slot6 = new clsRoleStaticData;

		$slot1->hp=100;
		$slot1->dex=12;
		$slot2->hp=103;
		$slot2->dex=15;
		$slot3->hp=101;
		$slot3->dex=16;
		$slot4->hp=107;
		$slot4->dex=14;
		$slot5->hp=200;
		$slot5->dex=13;
		$slot6->hp=800;
		$slot6->dex=11;

		$slot1->pow=8;
		$slot2->pow=10;
		$slot3->pow=11;
		$slot4->pow=17;
		$slot5->pow=21;
		$slot6->pow=299;


		$slot1->side="me";
		$slot2->side="me";
		$slot3->side="me";
		$slot4->side="me";
		$slot5->side="me";
		$slot6->side="ota";
		$slot1->slot=3;
		$slot2->slot=6;
		$slot3->slot=9;
		$slot4->slot=5;
		$slot5->slot=4;
		$slot6->slot=5;
		$attackSide[3]=$slot1;
		$attackSide[6]=$slot2;
		$attackSide[9]=$slot3;
		$attackSide[5]=$slot4;
		$attackSide[4]=$slot5;

		$defendSide["5"]=$slot6;

		$this->_initPassiveSkillBuff($attackSide,$defendSide);
		/*
		 *   slot map
		 *   123    123
		 *   456 vs 456
		 *   789    789 
		 * */
		$this->teams["me"]=$attackSide;
		$this->teams["ota"]=$defendSide;
		
		$this->deadTeamMates["me"]=array();// id=>slot
		$this->deadTeamMates["ota"]=array();

		// index=>roledata
		$this->attackSequence[]=$slot1;
		$this->attackSequence[]=$slot2;
		$this->attackSequence[]=$slot3;
		$this->attackSequence[]=$slot4;
		$this->attackSequence[]=$slot5;
		$this->attackSequence[]=$slot6;
	}
}
