var org_uengine_codi_mw3_model_CollapsePerspective = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.object =  mw3.objects[this.objectId];
	
	if(this.object.loader && !this.object.loaded)
		mw3.call(this.objectId, 'refresh');
		
};

org_uengine_codi_mw3_model_CollapsePerspective.prototype = {
	loaded : function(){
		$('#' + this.divId + ' a').click(function(){
			$('#navigator .depth2 .fist_menu li').removeClass('selected_navi');
			$(this).parent().addClass('selected_navi');
		});
	}
};