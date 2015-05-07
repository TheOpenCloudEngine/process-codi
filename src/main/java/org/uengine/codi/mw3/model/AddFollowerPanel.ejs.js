var org_uengine_codi_mw3_model_AddFollowerPanel = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];
	this.divId = '#objDiv_' + this.objectId;
	
	
	$(this.divId).css({'height':'100%'});
	
}

