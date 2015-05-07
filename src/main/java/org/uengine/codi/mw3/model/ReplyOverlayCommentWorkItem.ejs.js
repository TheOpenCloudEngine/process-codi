var org_uengine_codi_mw3_model_ReplyOverlayCommentWorkItem = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	var objectDivId = mw3._getObjectDivId(this.objectId);
	var objectDiv = $('#' + objectDivId);
	
	var object = mw3.objects[this.objectId];
	
	
	objectDiv.hover(
			function(){
				$(this).find('.commentTable').css("background-color","#e9e9e9");
				$(this).find('.deleteCommentIcon').show()
			},
			function(){
				$(this).find('.commentTable').css("background-color","#fff");
				$(this).find('.deleteCommentIcon').hide();
			}
		);
	// WorkItemHandler.ejs.js 에서 검색으로 사용함
//	objectDiv.attr("taskId",object.taskId);
//	objectDiv.attr("parentTaskId",object.prtTskId);
//	objectDiv.attr("fieldName",object.ext1);
	
	if( object.prtTskId && !object.contentLoaded ){
		var fieldName = object.ext1;
		var parentWorkItem = mw3.getAutowiredObject("org.uengine.codi.mw3.model.WorkItemHandler@"+ object.prtTskId , true);
		
		if( parentWorkItem && parentWorkItem.parameters ){
			for(var i = 0; i < parentWorkItem.parameters.length; i++){
				
				var valObject = parentWorkItem.parameters[i].valueObject;
				var metadata = mw3.getMetadata(valObject.__className);
				if(metadata.fieldDescriptors){
					for (var j=0; j<metadata.fieldDescriptors.length; j++){
						var fd = metadata.fieldDescriptors[j];
						if(fieldName == fd.name){
							var valiableObjectId = mw3.getChildObjectId(valObject.__objectId, fieldName);
							if( valiableObjectId ){
								var valiableObjectDivId = mw3._getObjectDivId(valiableObjectId);
								var valiableObjectDiv = $('#' + valiableObjectDivId);
								valiableObjectDiv.append(objectDiv[0]);
							}
						}
						
					}
				}
			}
		}
//		// 요기 변경...
//		var valiableObjectId = mw3.getChildObjectId(parentWorkItem.__objectId, fieldName);
//		if( valiableObjectId ){
//			var valiableObjectDivId = mw3._getObjectDivId(valiableObjectId);
//			var valiableObjectDiv = $('#' + valiableObjectDivId);
//			valiableObjectDiv.append(objectDiv[0]);
//		}
//		var valiableObjectId = mw3.getChildObjectId(parentWorkItem.__objectId, fieldName);
//		// parentWorkItem 이 아직 로드가 안되어 있다면 hide 해놓는다.
//		if( !parentWorkItem ){
//			objectDiv.hide();
//			object.contentLoaded = true;
//		}
	}
	
};
