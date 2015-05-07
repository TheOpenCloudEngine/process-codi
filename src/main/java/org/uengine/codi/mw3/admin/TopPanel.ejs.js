var org_uengine_codi_mw3_admin_TopPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = '#objDiv_' + objectId;
	this.div = $(this.divId);
	
};

org_uengine_codi_mw3_admin_TopPanel.prototype = {
	
	toggleShowPerspective : function(){
		var sns = mw3.getAutowiredObject('org.uengine.codi.mw3.model.SNS');
		if( sns ){
			var layoutFaceHelper = mw3.getFaceHelper(sns.layout.__objectId);
			var isHidden = $(layoutFaceHelper.divId + ' .show_west_btn').is(':hidden');
						
			if(isHidden){
				$(layoutFaceHelper.divId + ' .hide_west_btn').trigger('click');
			}else{
				$(layoutFaceHelper.divId + ' .show_west_btn').trigger('click');
			}
				
		}		

	}
};