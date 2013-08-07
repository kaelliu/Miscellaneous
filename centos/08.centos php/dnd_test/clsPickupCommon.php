<?php

// can also be a strategy
class clsPickupCommon {
	public function pick($rd,$fd,$sd){
		$tmp;
		if($sd->side == 1){
			$tmp = $fd->teams[$rd->side];
		}else{
			$sidestr="";
			if($rd->side == "me"){
				$sidestr="ota";
			}else{
				$sidestr="me";
			}
			$tmp = $fd->teams[$sidestr];
		}
		$result = array();
		if($sd->effectZone == 1){ // 单体
			foreach($tmp as $val){
				$result[]=$val;break;
			}
		}else if($sd->effectZone == 6){// 全体
			foreach($tmp as $val){
				$result[]=$val;
			}
		}else if($sd->effectZone == 2){// 竖排 -- 数组列
		}else if($sd->effectZone == 7){// 自身
			$result[]=$rd;
		}
		return $result;
	}
}
