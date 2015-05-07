var org_uengine_codi_mw3_widget_IFrame = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
	
	if(!this.object.height)
		this.objectDiv.css('height', '100%');
	if(!this.object.width)
		this.objectDiv.css('width', '100%');
};