<?php

interface clsBuffComputeStrategy{
	// skid for if use some skill can trigger buff effect
	// tar 单体目标
	// 
	public function onBuffLogic($obj,$tar,$fd,$bd);
	// what u do on logic,pay back on remove
	// when hp is less,pow is more add for example
	// hp change every round,you should have a formula
	// to get back the initial pow,if have more than 2 buff
	// effect on same property
	public function onBuffRemove($obj,$bd);
}
