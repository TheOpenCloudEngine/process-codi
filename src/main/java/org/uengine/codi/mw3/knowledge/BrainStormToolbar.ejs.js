var org_uengine_codi_mw3_knowledge_BrainStormToolbar = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
	
	this.objectDiv.find('ul:first li:first-child').addClass('toolbar_current');
	
	
	this.showStatus = function(message){
		var methodName = message.substring(0,message.indexOf(' '));
		
		this.objectDiv.find('#' + methodName + '_' + this.objectId).siblings().removeClass('toolbar_current').end().addClass('toolbar_current');
	};
};

