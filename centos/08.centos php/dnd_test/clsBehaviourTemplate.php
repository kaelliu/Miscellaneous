<?php

abstract class clsBehaviourTemplate {
	// depends on fight data
	abstract public function doProcessBuff($team,$id);
	abstract public function doChooseBehaviour();
	abstract public function doOneAction($team);
	abstract public function doPickUpTarget($team,$id);
	// process one round
	// team contains each side!
	public function TemplateMethod($team){
		$usedSkID = $this->doChooseBehaviour();
		$this->doProcessBuff($team,$usedSkID);
		$this->doPickUpTarget($team,$usedSkID);
		$this->doOneAction($team);
	}
}

/*
class clsAiBehaviour extends clsBehaviourTemplate {

}
 */
