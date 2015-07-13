var org_uengine_codi_mw3_model_PerspectivePanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;	
	this.divId = '#objDiv_' + this.objectId;
	
//	$('#navigator .depth2 a').click(function(){
//		$('#navigator .depth2 .fist_menu li').removeClass('selected_navi');
//		$('.idept').removeClass('selected_navi2');
//		$('.iemployee').removeClass('selected_navi2');
//		$(this).parent().addClass('selected_navi');
//	});
	var session = mw3.getAutowiredObject("org.uengine.codi.mw3.model.Session");
	
	$(this.divId).parent().css({"background":"#2B2E33","overflow":"hidden"});
	if('phone' != session.ux){
		$(this.divId).parent().css({"width":"100%"});
	};
	$(this.divId).parent().hover(function(){$(this).css({"overflow":"auto","margin-right":"0px"});},function(){$(this).css({"overflow":"hidden"});});
	
	$(this.divId).find('.contact_panel').hover(function(){
		$(this).height('300')
	},function(){
		$(this).height('130')
	});
	//$(this.divId).parent().parent().parent().parent().parent().append('<div class=\"hide_left_btn\" onclick=\""></div>')
	
};