var org_uengine_codi_mw3_knowledge_SeeFilter = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;	
	
	$(mw3.getInputElement(this.objectId, 'nodeType')).bind('change', {objectId : this.objectId}, function(event){
		mw3.call(event.data.objectId, 'applyNode');
	});
	
	$(mw3.getInputElement(this.objectId, 'chartType')).bind('change', {objectId : this.objectId}, function(event){
		mw3.call(event.data.objectId, 'applyChart');
	});
};
