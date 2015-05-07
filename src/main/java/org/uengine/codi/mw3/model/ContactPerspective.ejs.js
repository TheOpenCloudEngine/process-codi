var org_uengine_codi_mw3_model_ContactPerspective = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
	this.object = mw3.objects[this.objectId];
	
	this.divId = mw3._getObjectDivId(this.objectId);
	this.div = $('#' + this.divId);		

	if(this.object.loader && !this.object.loaded)
		mw3.call(this.objectId, 'refresh');
	else{
		this.div.hover(
			function(){
				$(this).find('.west_more_btn').show();
			},
			function(){
				$(this).find('.west_more_btn').hide();
			}
		)
	}	
};
