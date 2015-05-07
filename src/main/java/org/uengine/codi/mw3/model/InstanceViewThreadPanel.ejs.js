var org_uengine_codi_mw3_model_InstanceViewThreadPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;	
};

org_uengine_codi_mw3_model_InstanceViewThreadPanel.prototype = {
	toAppend : function(target){
		var threadId = mw3.getChildObjectId(this.objectId, 'thread');
		var threadDivId = mw3._getObjectDivId(threadId);
		
		var html = mw3.locateObject(target, null);
		$('#' + threadDivId + ' .workitem_list:eq(0)').append(html);
	}
};