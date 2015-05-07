var org_uengine_codi_mw3_model_TodoBadge = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	this.object = mw3.objects[this.objectId];
	
	if(this.object.loader){
		mw3.call(this.objectId, 'refresh');
	}
};
