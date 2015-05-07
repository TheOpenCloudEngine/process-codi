var org_uengine_codi_mw3_model_INotification = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
	
	$('#see_' + this.objectId).bind('click', {objectId : this.objectId}, function(event){
		var objectId = event.data.objectId;
		mw3.call(objectId, 'see');
	});
};
	
org_uengine_codi_mw3_model_INotification.prototype = {
	destroy : function(){
		$('#see_' + this.objectId).unbind();
	}
};