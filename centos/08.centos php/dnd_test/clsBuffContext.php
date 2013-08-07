<?php

class clsBuffContext {
	private $_buffStaticData;
	private $_compute;

	public function __construct($bd,$comp){
		$this->_buffStaticData = $bd;
		$this->_compute = $comp;
	}

	public function removeBuff($obj){
		$this->_comupte->onBuffRemove($obj,$this->_buffStaticData);
	}

	public function doLogic($obj,$fd,$skid){
		// do and change obj's detail data
		$this->_comupte->onBuffLogic($obj,$fd,$this->_buffStaticData,$skid);
	}
}
