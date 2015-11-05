var org_uengine_codi_mw3_model_IUser = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	
	var user = mw3.objects[objectId];
	if(user == null) {
		user = {
			__className: 'org.uengine.codi.mw3.model.User',
			metaworksContext: {
				when: 'edit'
			}
		};

		mw3.setObject(objectId, user);
	}else
	
	if(user.metaworksContext && user.metaworksContext.when=='contacts'){
		var msg=$('#objDiv_' + objectId).find('.fontgray').text();
		var comp = msg.length;
		var count = 0;
		
		var typing = function(){ 
			if(count <= comp){ 
				$('#objDiv_' + objectId).find('.fontgray').text(msg.substring(0,count));
				count++ ;
				setTimeout(function(){typing();}, 100); 
			}
		};
		
		typing();
	}
};