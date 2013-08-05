<?php
class clsRoleStaticData {
	public $roleId;
	public $mainSkill = array(); // -1 means this slot is empty
	public $passiveSkill = -1;
	public $leaderSkill = -1;
	public $rage = 0;
	public $hp = 0;
	public $int = 0;
	public $pow = 0;
	public $dex = 0;
	public $buffsOnBody = array();// buffid=>lastround
	public $currentTarget = array();
	public $buffAddingValue = array();// buff effect value,type=>convert to value
}
