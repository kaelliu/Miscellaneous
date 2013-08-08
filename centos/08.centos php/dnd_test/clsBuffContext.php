<?php

class clsBuffContext {
	private $_buffStaticData;
	private $_compute;

	public function __construct($bd,$comp){
		$this->_buffStaticData = $bd;
		$this->_compute = $comp;
	}

	public function removeBuff($obj){
		$this->_compute->onBuffRemove($obj,$this->_buffStaticData);
	}

	public function doLogic($obj,$tar,$fd){
		// do and change obj's detail data
		return $this->_compute->onBuffLogic($obj,$tar,$fd,$this->_buffStaticData);
	}

	public function getBuffStaticData(){
		return $this->_buffStaticData;
	}
}
