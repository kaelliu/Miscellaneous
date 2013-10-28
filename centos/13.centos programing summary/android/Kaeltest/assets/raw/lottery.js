$(function() {
	$(".lot-btn").click(function() {
			var resultValue = Math.floor((Math.random()*360)+1); 
			$("#result").html("摇中的角度是："+resultValue);
			$("#imgs").rotate(0);
			window.setTimeout(function(){
				$("#imgs").rotate({ angle:0,animateTo:1800+resultValue,easing: $.easing.easeInOutQuart,duration:10000});	
			},500);
			
	});
});