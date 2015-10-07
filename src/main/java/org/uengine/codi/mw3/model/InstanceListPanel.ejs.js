var org_uengine_codi_mw3_model_InstanceListPanel = function(objectId, className) {
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
			height:   '100%',
			overflow: 'auto'
		});
	
	this.divId = mw3._getObjectDivId(this.objectId);
	
	// set window title
	this.windowObjectId = this.objectDiv.closest('.mw3_window').attr('objectId');
	
	var session = mw3.fn.getSession();
	if(this.object && this.object.title || session.windowTitle){
		var title = session.windowTitle;
		if(this.object && this.object.title)
			title = this.object.title;
			
		if(this.windowObjectId)
			mw3.getFaceHelper(this.windowObjectId).setTitle(mw3.localize(title));
	}

//	$("#project_Info").click(function(){
//		if($(this).hasClass('current')){
//			$('.projectInfo').slideUp(250);
//			$(this).removeClass('current').text("Project info. ���");
//		}else{
//			$('.projectInfo').slideDown(250);
//			$(this).addClass('current').text("Project info. ���");
//		}
//	})

	if(this.object){
		// instanceListPanelDiv
		var instanceListPanelDiv = $('#objDiv_' + this.objectId);

		instanceListPanelDiv.scroll(function (e) {
			if (instanceListPanelDiv.scrollTop() > instanceListPanelDiv.find('[name="instanceList"]').height() - instanceListPanelDiv.height() - 50) {
				// find instanceListObjectId
				var instanceListObjectId = $('#objDiv_' + $(this).attr('objectid')).find('[name="instanceList"]').attr('objectid');
				// find moreInstanceListSize
				var moreInstanceListSize = $('#objDiv_' + $(this).attr('objectid')).find('[name="instanceList"]').find('[name="moreInstanceList"]').length;
				// moreInstanceListSize is null object. because moreInstanceListSize - 2
				// eq start 0, length start 1. then size - 2
				var targetObjectId = $('#objDiv_' + $(this).attr('objectid')).find('[name="instanceList"]').find('[name="moreInstanceList"]').eq(moreInstanceListSize - 2).attr('objectid');
				mw3.getObject(targetObjectId).more();
			}
		});
		/*
	 	$('#' + this.divId + ' .ui-layout-content').mCustomScrollbar({
			callbacks:{
				
				onTotalScroll:function(){
					mw3.objects[lastMore].more();
					//setTimeout(function(){$("#method_37_switchToScheduleCalendar .ui-layout-content").mCustomScrollbar("update");},2000);
				}
			}
		});*/
		
		
		$('#' + mw3._getObjectDivId(mw3.getChildObjectId(this.objectId, 'topicFollowers')) + " #searchbox").css("border-bottom","#9EBFC4 1px solid ");
		$('#' + mw3._getObjectDivId(mw3.getChildObjectId(this.objectId, 'documentFollowers')) + " #searchbox").css("border-bottom","#9EBFC4 1px solid ");
	}
};

org_uengine_codi_mw3_model_InstanceListPanel.prototype = {
	startLoading : function(){
		if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).startLoading)
			mw3.getFaceHelper(this.windowObjectId).startLoading();
	},
	endLoading : function(){
		if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
			mw3.getFaceHelper(this.windowObjectId).endLoading();
	},
	showStatus : function(message){
		
	},
	destroy : function(){
		$('#objDiv_' + this.objectId).unbind();
		$('#objDiv_' + this.objectId).scrollTop(0);
	}
};