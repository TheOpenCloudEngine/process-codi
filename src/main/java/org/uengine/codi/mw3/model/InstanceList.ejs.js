//var lastMore;

var org_uengine_codi_mw3_model_InstanceList = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.divObj = $('#' + this.divId);
	
	this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');

	var object = mw3.objects[objectId];
	//if(object && object.instances == null && className == 'org.uengine.codi.mw3.model.InstanceList'){
	//	lastMore = objectId;
	//}
};

org_uengine_codi_mw3_model_InstanceList.prototype = {
	startLoading : function(){		
			$('.moreBtn5').hide();
			$('.moreBtn6').show();	
	},
	endLoading : function(){
			$('.moreBtn5').show();
			$('.moreBtn6').hide();
	},
	showStatus : function(message){
		
	},
	toPrepend : function(target){
		if($('#' + mw3._getObjectDivId(this.objectId)).length == 0){
			if(console) console.log('not exist org.uengine.codi.mw3.model.InstanceList : ' + this.objectId); 
			return false;
		}
		
		if(target.__className == 'org.uengine.codi.mw3.model.Instance' || target.__className == 'org.uengine.codi.mw3.model.IInstance'){
			var isAuth = false;

			var session = mw3.getAutowiredObject('org.uengine.codi.mw3.model.Session');
			var userId = session.employee.empCode;
			var partCode = session.employee.partCode;
			
			// 보안설정 전에 자기 자신이 올린글 (채팅, 새글쓰기)은 바로 붙음. 
			// 자기가 올린글을 보안으로 하였을때 followers가 null 이기 때문에 (target.initEp ==  userId) 추가함 2/18 김형국
			// 보안설정
			if(target.secuopt == '0' || target.secuopt == '2' || target.initEp ==  userId){
				isAuth = true;
				
			} else if(target.secuopt == '1'){			// follower
				for(var i=0; i<target.followers.followers.length; i++){
					var follower = target.followers.followers[i];
					
					if(follower.user.userId == userId){
						isAuth = true;
						break;
					}
				}
				
				if(!isAuth){
					for(var i=0; i<target.followers.followers.length; i++){
                        var follower = target.followers.followers[i];
						
						if(follower.dept.partCode == partCode){
							isAuth = true;
							break;
						}
					}					
				}

			}else if(target.secuopt == '3'){	// topic
				
			}
			
			if(isAuth){				
				var html = mw3.locateObject(target, null);
				
				$('<div>').prependTo(this.divObj).append(html);
			}
		}			
	}
};
