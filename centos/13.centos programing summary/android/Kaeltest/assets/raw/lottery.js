$(function() {
	$(".lot-btn").click(function() {
			var resultValue = Math.floor((Math.random()*360)+1); 
			$("#result").html("ҡ�еĽǶ��ǣ�"+resultValue);
			$("#imgs").rotate(0);
			window.setTimeout(function(){
				$("#imgs").rotate({ angle:0,animateTo:1800+resultValue,easing: $.easing.easeInOutQuart,duration:10000});	
			},500);
			
	});
});