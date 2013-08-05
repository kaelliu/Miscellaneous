<?php

class clsBuffContext {
	private $_buffStaticData;
	private $_compute;

	public function __construct($bd,$comp){
		$_buffStaticData = $bd;
		$_compute = $comp;
	}

	public function doLogic($obj){
		// do and change obj's detail data
		$_comupte($obj);
	}
}
