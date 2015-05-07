var org_uengine_codi_mw3_common_MainPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	
	$("#objDiv_" + objectId).css("height", "100%");
	
	var session = mw3.getAutowiredObject("org.uengine.codi.mw3.model.Session");
	
	if('phone' == session.ux){
		$("#objDiv_" + objectId +" .top_panel_mobile_menu").click(function(){
			$("#objDiv_" + objectId + " #main_container").css({"position":"relative","height":"100%","right":"initial"});
			$("#objDiv_" + objectId + " #main_container").animate({left:260},200);
			$("#objDiv_" + objectId + " #mobile_left_container").show().animate({width:260},200);
			$("#main_container").prepend("<div class='overlay_right' style='position:absolute;top:0px;left:0px;width:100%;height:100%;z-index:9999'></div>")
			
		});
		
		$("#objDiv_" + objectId +" .top_panel_mobile_friend").click(function(){
			$("#objDiv_" + objectId + " #main_container").css({"position":"relative","height":"100%","left":"initial"});
			$("#objDiv_" + objectId + " #main_container").animate({right:260},200);
			$("#objDiv_" + objectId + " #mobile_right_container").show().animate({width:260},200);
			$("#main_container").prepend("<div class='overlay_right' style='position:absolute;top:0px;left:0px;width:100%;height:100%;z-index:9999'></div>")
			
		});
		
		$('.overlay_right').live('click', function(){
				$("#objDiv_" + objectId + " #main_container").css({"position":"relative","left":"0px","right":"0px","height":"100%"});
				$("#objDiv_" + objectId + " #mobile_left_container").animate({width:0}).hide();	
				$("#objDiv_" + objectId + " #mobile_right_container").animate({width:0}).hide();	
				$(".overlay_right").remove();
			
		});
		
	}

}