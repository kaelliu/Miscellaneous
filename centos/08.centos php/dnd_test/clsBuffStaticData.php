<?php
class clsBuffStaticData {
	public $id;
	public $buffOrNerf;//增或减
	public $targetProperty;//属性
	public $value;//属性值
	public $leftRound;// 持续回合数
	public $addAnotherBuffOnFinish;// 结束时附加其他BUFF(如果可能)
	public $percentOrValue;// 按值或百分比变化 1-值 2-百分比
	public $paddingEffectType;// 附加效果类型(晕,减速,毒,睡等)
	public $target;// 附加目标对象 1-敌方 2-自己
	public $dotOrPersisit;// 每次跳值还是1次性增加
	public $autoOrBeattack;// 自动触发或者被攻击时触发 1-自动 2-被击
	
	public function __construct(){
		$autoOrBeattack = TRIGGER_AUTO;
	}
}
