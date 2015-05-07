var org_uengine_codi_mw3_model_ProcessMapList = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = '#objDiv_' + this.objectId;
	
	$(this.divId).addClass("processMapList").attr('objectId', objectId);
}