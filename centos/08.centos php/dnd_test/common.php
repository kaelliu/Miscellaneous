<?php
define("DIR_FILE_PATH",dirname(__FILE__));// 运行文件目录
define("DIR_ROOT", dirname(dirname(__FILE__))); // 根目录
define("DIR_SKILL_LOGIC",DIR_FILE_PATH . "/skill");
define("DIR_BUFF_LOGIC",DIR_FILE_PATH . "/buff");

// fight system constant type,buff effect
// "BAOJI" - 1
// "DUOSHAN" - 2
// "GEDANG" - 3
// "FANJI" - 4
// "LILIANG" - 5
// "MINJIE" - 6
// "ZHILI" - 7
// "ZUIDAHP" - 8
// "XUANYUN" - 9
// "SHUIJIAO" - 10
// "JINGTONG" - 11
// "MINGZHONG" - 12
// "JISU" - 13
// "MIANSHANG" - 14
// "JIANSHANG" - 15
// "XISHOU" - 16
// "FANTAN" - 17
// "BIZHONG" - 18
// "POJI" - 19
// "HARM" - 20
// "XIXUE" - 21
// "ALLMAIN" - 22 全部主属性int/dex/pow

const BAOJI = 1;
const DUOSHAN = 2;
const GEDANG = 3;
const FANJI = 4;
const LILIANG = 5;
const MINJIE = 6;
const ZHILI = 7;
const ZUIDAHP = 8;
const XUANYUN = 9;
const SHUIJIAO = 10;
const JINGTONG = 11;
const MINGZHONG = 12;
const JISU = 13;
const MIANSHANG = 14;
const JIANSHANG = 15;
const XISHOU = 16;
const FANTAN = 17;
const BIZHONG = 18;
const POJI = 19;
const HARM = 20;
const XIXUE = 21;
const ALLMAIN = 22;

const TRIGGER_AUTO = 1;
const TRIGGER_BATT = 2;
const TARGET_ENEMY = 1;
const TARGET_SELF  = 2;

require('ctlFight.php');

// 自动加载
spl_autoload_register(array(
    "ctlFight",
    "autoload"
));
