<?php
class clsBuffFightData {
	public $lastRound;
	//public $type;
	//public $value;
	public $buffAddValue=array();

	public function getValue($key){
		if(isset($buffAddValue[$key])){
			return $buffAddValue[$key];
		}else{
			return 0;
		}
	}
}
