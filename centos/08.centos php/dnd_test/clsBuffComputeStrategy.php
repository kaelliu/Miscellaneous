<?php

interface clsBuffComputStrategy{
	// skid for if use some skill can trigger buff effect
	public function onBuffLogic($obj,$fd,$bd,$skid);
	// what u do on logic,pay back on remove
	// when hp is less,pow is more add for example
	// hp change every round,you should have a formula
	// to get back the initial pow,if have more than 2 buff
	// effect on same property
	public function onBuffRemove($obj,$bd);
}
