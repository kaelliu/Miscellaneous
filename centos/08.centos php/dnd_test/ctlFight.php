<?php
//require_once('common.php');
// 自动加载
//spl_autoload_register(array(
//    "ctlFight",
//    "autoload"
//));

class ctlFight {
	const MAXROUND = 50;
	public static function autoload($clsName) {
		$pref = substr($clsName, 0, 3);
		$dirs = array(
			'cls'     => DIR_FILE_PATH,
			'ctl'     => DIR_FILE_PATH,
            'skc'     => DIR_SKILL_LOGIC . "/condition",
            'skl'     => DIR_SKILL_LOGIC . "/compute",
            'bul'     => DIR_BUFF_LOGIC . "/compute"
        );
        $dir = isset($dirs [$pref]) ? $dirs [$pref] : DIR_ROOT;
		$path = $dir . "/{$clsName}.php";
        if (file_exists($path)) {
            include $path;
        }
	}
	public function doFight(){
		$uid1 = $_REQUEST["u1"];
		$uid2 = $_REQUEST["u2"];
		echo "fight start,get both side data!<br>";
		// get data from model,use static here
		$data = new clsFightData;
		$data->initDataForTest();

		while(!($data->winaid=$data->isover())){
			if($data->needRearrangeSequence){
				$data->rearrangeSequence();
			}
			$actioner = $data->getNextMover();
			$oneAction = new clsRoleBehaviour($actioner);
			$oneAction->TemplateMethod($data);
			echo "current in round:".$data->currentRound."<br>";
			$data->currentRound++;
			if($data->currentRound >= self::MAXROUND){
				break;
			}
		}

		echo "fight over!<br>";
	}
}

