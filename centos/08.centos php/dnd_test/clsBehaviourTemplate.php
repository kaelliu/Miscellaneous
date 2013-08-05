<?php

abstract class clsBehaviourTemplate {
	// depends on fight data
	abstract public function doProcessBuff();
	abstract public function doChooseBehaviour();
	abstract public function doOneAction($team);
	abstract public function doPickUpTarget($team,$id);
	// process one round
	// team contains each side!
	public function TemplateMethod($team){
		$this->doProcessBuff();
		$this->doPickUpTarget($team,$this->doChooseBehaviour());
		$this->doOneAction($team);
	}
}

/*
class clsAiBehaviour extends clsBehaviourTemplate {

}
 */
