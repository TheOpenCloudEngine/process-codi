var org_uengine_codi_mw3_model_WorkItemListener = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];

	if(this.object){
		mw3.removeObject(this.objectId);
		
		var value = this.object.applyItem;
		
		// value 는 새로 만들어진 workItem
		var preferUx = mw3.fn.getPreferUx();	
		var instanceId = value.instId;
		var type = value.type;
		
		value.metaworksContext.when = 'view';
		
		if('sns' == preferUx ){
			// @? 부분은 id값을 부여하여 정확한 객체를 찾기 위함
//			var instanceListObjectId = mw3.getAutowiredObject("org.uengine.codi.mw3.model.InstanceList@1").__objectId;
			value.metaworksContext.how = "sns";
//			$("#objDiv_" + instanceListObjectId).prepend(html);			
		}else{
			value.metaworksContext.how = "normal";
			
		}
		
		//
//		var instanceObject = mw3.getAutowiredObject("org.uengine.codi.mw3.model.Instance@"+instanceId);
		var instanceThreadObject = mw3.getAutowiredObject("org.uengine.codi.mw3.model.InstanceViewThreadPanel@"+instanceId);
		if( value.rootInstId && value.rootInstId != instanceId){
			instanceThreadObject = mw3.getAutowiredObject("org.uengine.codi.mw3.model.InstanceViewThreadPanel@"+value.rootInstId);
		}
		if( instanceThreadObject != null && ( instanceThreadObject.instanceId == instanceId || instanceThreadObject.instanceId == value.rootInstId)){ 
			// InstanceViewThreadPanel 이 열려있는경우
			if( type == "ovryCmnt" && this.object.command != 'remove'){
				var html = mw3.locateObject(value, null);
				
				// 댓글의 커맨트를 다는 경우
				var parentTaskId = value.overlayCommentOption.parentTaskId;
				var parentWorkitemObject = mw3.getAutowiredObject("org.uengine.codi.mw3.model.WorkItem@"+parentTaskId);
				$("#objDiv_" + parentWorkitemObject.__objectId ).append(html);	
			}else if( type == "replyCmnt" && this.object.command != 'remove'){
				if( value.prtTskId && !value.contentLoaded ){
					var fieldName = value.ext1;
					var parentWorkItem = mw3.getAutowiredObject("org.uengine.codi.mw3.model.WorkItemHandler@"+ value.prtTskId , true);
					
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
											var html = mw3.locateObject(value, null);
											valiableObjectDiv.append(html);
										}
									}
									
								}
							}
						}
					}
				}
			}else{
				if(this.object.command == 'refresh'){
					var workItem = mw3.getAutowiredObject("org.uengine.codi.mw3.model.WorkItem@"+value.taskId);
					if(workItem != null && typeof workItem != 'undefined')
						mw3.setObject(workItem.__objectId, value);
				}else if(this.object.command == 'remove'){
					var workItem = mw3.getAutowiredObject("org.uengine.codi.mw3.model.WorkItem@"+value.taskId);
                    if(workItem != null && typeof workItem != 'undefined')
		              mw3.removeObject(workItem.__objectId);
				}else{
					instanceThreadObject.getFaceHelper().toAppend(value);
				}
			}
		}
		
		mw3.onLoadFaceHelperScript();
	}
	
};