<?php
class clsBuffStaticData {
	public $id;
	public $buffOrNerf;//增或减
	public $targetProperty;//属性
	public $value;//属性值
	public $leftRound;// 剩余回合
	public $addAnotherBuffOnFinish;// 结束时附加其他BUFF(如果可能)
	public $percentOrValue;// 按值或百分比变化 1-值 2-百分比
	public $paddingEffectType;// 附加效果类型(晕,减速,毒,睡等)
	public $target;// 附加目标对象 1-敌方 2-自己
}
