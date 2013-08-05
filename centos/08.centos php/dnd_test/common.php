<?php
define("DIR_FILE_PATH",dirname(__FILE__));// 运行文件目录
define("DIR_ROOT", dirname(dirname(__FILE__))); // 根目录
define("DIR_SKILL_LOGIC",DIR_FILE_PATH . "/skill");
define("DIR_BUFF_LOGIC",DIR_FILE_PATH . "/buff");

// fight system constant type
// "BAOJI" - 1
// "DUOSHAN" - 2
// "GEDANG" - 3
// "FANJI" - 4
// "LILIANG" - 5
// "MINJIE" - 6
// "ZHILI" - 7
// "ZUIDAHP" - 8
// "XUANYUN" - 9
// "SLEEP" - 10
// "JINGTONG" - 11
// "MINGZHONG" - 12
// "JISU" - 13
// "MIANSHANG" - 14
// "JIANSHANG" - 15
// "XISHOU" - 16
// "FANTAN" - 17
// "BIZHONG" - 18
// "POJI" - 19

require('ctlFight.php');

// 自动加载
spl_autoload_register(array(
    "ctlFight",
    "autoload"
));
