var org_uengine_codi_mw3_model_InstanceView = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
	
	this.objectDiv
		.css({
			position: 'relative',
			height:   '100%'
		});
	
	// window 의 title 설정
	this.windowObjectId = this.objectDiv.closest('.mw3_window').attr('objectId');
	
	var object = mw3.objects[this.objectId];
	if(object && object.instanceName){
		if(this.windowObjectId){
			mw3.getFaceHelper(this.windowObjectId).setTitle(mw3.localize(object.instanceName));
		}
	}
	
	
//		var posting = [];
//		
//		var postIds = object.crowdSourcer.postIds;	
//		
//		if(postIds != null){
//			var cnt = 0;
//			
//			for(var i=0; i<postIds.length; i++){
//				var num = 20;
//				 
//				FB.api(postIds[i], {
//					limit : num
//				}, function(response) {
//					if (!response || response.error) {
//						//console.debug(response);
//						
//						if(typeof response.error != "undefined")
//							alert("error : " + response.error.message);
//						
//					} else {
//						for(var j=0; j<response.comments.count; j++){
//							var comment = response.comments.data[j];
//							var user = {
//									userId : comment.from.id,
//									name : comment.from.name,
//									__className : "org.uengine.codi.mw3.model.IUser"
//							};
//							
//							posting[cnt] = {    type : "postings",
//									           title : comment.message,
//									        endpoint : comment.from.id,
//									          writer : user,
//									     __className : "org.uengine.codi.mw3.model.PostingsWorkItem"
//									  
//							  };
//							
//							cnt++;					
//						}
//						
//						if(i == postIds.length){
//							mw3.setObject(object.threadPosting.__objectId, posting);
//						}
//					}
//				});	
//			}
//		}
//	}

};

org_uengine_codi_mw3_model_InstanceView.prototype = {
		startLoading : function(){
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).startLoading)
				mw3.getFaceHelper(this.windowObjectId).startLoading();
		},
		endLoading : function(){
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
				mw3.getFaceHelper(this.windowObjectId).endLoading();
		},
		showStatus : function(message){
			
		}		
};