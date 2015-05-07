var org_uengine_codi_mw3_Loader = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	
	var storedId = getCookie("codi.id");
	
	if(storedId!=null){				
		this.object.userId = storedId;
		this.object.password = getCookie("codi.password");
		this.object.lastVisitPage = getCookie("codi.lastVisit");
	}
	
	mw3.call(this.objectId, 'auth');
};

org_uengine_codi_mw3_Loader.prototype = {

};