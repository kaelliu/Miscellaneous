<?php

class clsSkillStaticData {
	public $id;
	public $buffs=array();
	public $value;// 即时伤害/治疗值 正负数表示
	// 作用范围
	// 1-单体
	// 2-竖排
	// 3-横排
	// 4-竖2排
	// 5-横2排
	// 6-全体
	// 7-十字
	public $effectZone;
	public $side;// 1-友方 2-敌方
}
