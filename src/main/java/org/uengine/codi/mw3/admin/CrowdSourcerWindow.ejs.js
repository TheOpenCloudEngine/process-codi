var org_uengine_codi_mw3_admin_CrowdSourcerWindow = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[objectId];
	
	object.send = function(){		
		var object = mw3.objects[objectId];
		
		if(object.defId){
			  var object = mw3.objects[objectId];
			  
			  var url = window.location;
			  var link = "http://localhost:8080/uengine-web/index.html?defId=" + object.defId;

			  if(object.loginUser.userId == null)
				  return;
			  
			  var feed = object.loginUser.userId + '/feed';
			  
			  FB.api(feed, 'post', {
				  message : object.message,
				  link : link
			  }, function(response) {
				  if (!response || response.error) {
				  } else {
					  alert("메세지 전송에 성공하였습니다");
					  $("#popup_" + mw3.recentOpenerObjectId).remove();
				  }								
			  });
		}else{
			alert("메세지 전송에 실패하였습니다. (defId = null)");
		}
	};
}