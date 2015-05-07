var org_uengine_codi_mw3_model_PerspectiveTopPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	var browser = mw3.browserCheck();
	if(browser == "MSIE 7"){
		$('.searchboxarea').css("margin-left","20");
	}else{
		searchBarMagin = $('.newprocessbtn').width() + 20;
		$('.searchboxarea').css("margin-left",searchBarMagin);
	}
}