var org_uengine_codi_mw3_model_CrowdSourcerWorker = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	var faceHelper = this;
		
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			faceHelper.facebookPosting();
			faceHelper.remove();
		}else{
			FB.login(function(response) {
				if (response.status === 'connected') {
					faceHelper.facebookPosting();
					faceHelper.remove();
				}else{
					faceHelper.remove();				}
				
			}, {scope:'email,user_checkins,publish_stream,user_likes,export_stream'});
		}
	});
	
};

org_uengine_codi_mw3_model_CrowdSourcerWorker.prototype = {
	facebookPosting : function(){
		var object = mw3.getAutowiredObject('org.uengine.codi.mw3.model.CrowdSourcer');
		
		if(object)
			mw3.getFaceHelper(object.__objectId).posting();
	},
	remove : function(){		
		$('#objDiv_' + this.objectId).closest('.mw3_popup').remove();
		
		mw3.removeObject(this.objectId);		
	}
};