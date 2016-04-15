var org_uengine_codi_mw3_StartCodi = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
};

org_uengine_codi_mw3_StartCodi.prototype = {
	loaded : function(){
		var lastVisitPage = getCookie("codi.lastVisit");
		if(lastVisitPage){ 
			this.object.lastVisitPage = lastVisitPage;
			
			var lastVisitValue = getCookie("codi.lastVisitValue");
			
			if(lastVisitValue)
				this.object.lastVisitValue = lastVisitValue;	
		}
		
		if(this.object.key == 'loader'){
			this.object.load();
		}else if(this.object.key == 'logout'){
			delCookie("codi.lastVisit");
		
			// 존재하는 팝업 제거
			$('.target_popup,.target_stick').remove();
	
			delCookie("codi.rememberMe", "/","");
			delCookie("codi.facebookSSO", "/","");
		
			this.object.logout();
		}else if(this.object.key == 'login')
			this.object.login();
	}
};