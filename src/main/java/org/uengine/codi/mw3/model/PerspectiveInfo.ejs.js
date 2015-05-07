var org_uengine_codi_mw3_model_PerspectiveInfo = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.objectDiv.find('.toolbar li').bind('click', function(){
		$(this).siblings().removeClass('toolbar_current').end().addClass('toolbar_current');
	});
}