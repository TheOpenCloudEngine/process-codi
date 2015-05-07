var org_uengine_codi_mw3_model_Invitation = function(objectId, className) {
	
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	this.nodeDiv = this.objectDiv.children('div');
	
	this.startLoading = function(){
		$('.positionRight > a').attr("onclick", $('.positionRight > a').attr("onclick") + " return false");
		
	};
	this.endLoading = function(){
		$('.positionRight > a').attr("onclick", $('.positionRight > a').attr("onclick").replace(' return false',''));
	};
	this.showStatus = function(){
		
	};
	
};