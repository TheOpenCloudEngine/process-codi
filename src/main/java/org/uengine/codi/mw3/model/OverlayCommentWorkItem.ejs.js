var org_uengine_codi_mw3_model_OverlayCommentWorkItem = function(objectId, className){
	
	this.objectId = objectId;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];
	
	var parentWorkItem = mw3.getAutowiredObject("org.uengine.codi.mw3.model.IWorkItem@"+ this.object.overlayCommentOption.parentTaskId);
	var div = $("#objDiv_" + parentWorkItem.__objectId);
	
	if('view' == mw3.objectContexts[this.objectId].__metaworksContext.when){
		if('comment' == this.object.metaworksContext.how){
			$('.overlay_comment').hover(
					function(){$(this).css({"z-index":"101",'opacity':'1'})},
					function(){$(this).css({"z-index":"100",'opacity':'0.5'})}
			);
			$('.overlay_comment_line').hover(
					function(){$(this).parent().find('.overlay_comment').css({'z-index':'101','opacity':'1'})},
					function(){$(this).parent().find('.overlay_comment').css({'z-index':'100','opacity':'0.5'})}
			);
		}else{
			if(div.length && mw3.MESSAGE_LOADING != div.html()){
				var faceHelper = this;
				
				faceHelper.draw();
			}else{
				this.windowObjectId = this.objectDiv.closest('.mw3_window').attr('objectId');
				
				$('#' + mw3._getObjectDivId(this.windowObjectId)).one(
					'loaded.workitem_' + this.object.overlayCommentOption.parentTaskId, 
					{objectId: this.objectId}, 
					function(event, ui){
						mw3.getFaceHelper(event.data.objectId).draw();
				});
			}
		}
	}else{
		if(this.object.taskId==null){
			this.object.overlayCommentOption.x = (mw3.mouseX - div.offset().left -22) / (div.width()/100);
			this.object.overlayCommentOption.y = (mw3.mouseY - div.offset().top -34) / (div.height()/100);
		}		

		var title = (this.object.metaworksContext.when == mw3.WHEN_EDIT) ? this.object.title :'';
		var comment = prompt('설명을 입력해주세요 : ', title);
		
		if(comment){
			this.object.rootInstId = parentWorkItem.rootInstId;
			this.object.title = comment;
			this.object.add();		
		}			
	}	

};

org_uengine_codi_mw3_model_OverlayCommentWorkItem.prototype = {
	draw : function(){
		var clone = mw3.clone(this.object);
		clone.metaworksContext.how = 'comment';

		var ovryCmntTarget = {
	    	__className : 'org.uengine.codi.mw3.model.IWorkItem',
	    	taskId : this.object.overlayCommentOption.parentTaskId
	    };
	    
		var toAppend = {
			__className : 'org.metaworks.ToAppend',
			parent : ovryCmntTarget,
			target : clone,
			match : true
		};
		
		//mw3.locateObject(toAppend, toAppend.__className, 'body');
		//mw3.onLoadFaceHelperScript();
	}		
}