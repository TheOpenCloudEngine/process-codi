var org_uengine_codi_mw3_model_UnstructuredProcessInstanceStarter = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	
	var object = mw3.objects[this.objectId];
	
	if(object.metaworksContext && object.metaworksContext.when=='chat'){
		// 팝업창을 찾아서 팝업의 div 사이즈를 늘려줌
		// 팝업창을 띄울적에  popup.animate 가 false 여야함
		var popupDiv = $('#'+this.divId).parentsUntil("div[classname='org.uengine.codi.mw3.model.Popup']").parent().eq(0);
		if( popupDiv && popupDiv.length > 0){
			var scrollDiv = popupDiv.find('#addcontact-con');
			scrollDiv.height($('#'+this.divId).height() + 50);
		}
	}
	
	if(object && object.friend && object.friend.network == 'fb'){
		FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				$('#chat_' + objectId).show();
				
				mw3.getInputElement(objectId, 'title').focus();
			}else{
				FB.login(function(response) {
					if (response.status === 'connected') {
						$('#chat_' + objectId).show();
						
						mw3.getInputElement(objectId, 'title').focus();
					}else{
						$('#objDiv_' + objectId).closest('.mw3_popup').remove();
						
						mw3.removeObject(objectId);
					}
					
				}, {scope:'email,user_checkins,publish_stream,user_likes,export_stream'});
			}
		});
	}else{
		mw3.getInputElement(this.objectId, 'title').focus();
	}
}