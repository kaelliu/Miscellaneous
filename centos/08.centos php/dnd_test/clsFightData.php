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

		$attackSide["3"]=$slot1;
		$attackSide["6"]=$slot2;
		$attackSide["9"]=$slot3;
		$attackSide["5"]=$slot4;
		$attackSide["4"]=$slot5;

		$defendSide["5"]=$slot6;
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
