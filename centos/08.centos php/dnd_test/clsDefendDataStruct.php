<?php

class clsDefendDataStruct {
	public $duoshan;// 目标是否躲闪
	public $juefang;// 目标是否绝防
	public $fanji;// 目标是否反击
	public $criticalHit;// 暴击值 
	public $criticalR;// 暴击比
	public $hit;// 是否命中
	public $bizhong;// 目标是否必中
	public $poji;// 目标是否破防攻击
	public $gedang;// 目标是否招架
	public $harm;// 
	// buff deside this
	//public $absorb;// 攻击吸收值
	//public $reduceHarm;// 减少伤害值
	//public $fantan;// 目标是否反弹伤害
	
	public function __construct(){
		$this->init();
	}

	public function init(){
		$this->harm = 0;
		$this->duoshan = false;
		$this->gedang=false;
		$this->juefang = false;
		$this->fanji=0;// bigger than 0 is fanji,fanji is always a normal attack
		$this->criticalHit = 0;
		$this->cirticalR = 0;
		$this->hit = false;
		$this->bizhong=false;
		$this->poji=false;
	}
}

