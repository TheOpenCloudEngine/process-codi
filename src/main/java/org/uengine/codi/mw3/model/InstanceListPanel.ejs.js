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
		var scrollDiv = $('#objDiv_' + this.objectId);
		var instanceList = this.object.instanceList;
		var prelastMore = lastMore;

		if(this.object.id == 'instancelist') {
			scrollDiv.scroll(function (e) {
				if (instanceList) {
					var instanceListObjectId = instanceList.__objectId;
					var instanceListObjectDivId = mw3._getObjectDivId(instanceListObjectId);

					if (scrollDiv.scrollTop() > $('#' + instanceListObjectDivId).height() - scrollDiv.height() - 100) {
						if (prelastMore != lastMore) {
							prelastMore = lastMore;
							mw3.objects[lastMore].more();
						}
					}
				}
			});

		// if searchInstanceList
		}

		if(this.object.id == 'searchInstancelist') {
			console.log(this.object.id);
			scrollDiv.scroll(function (e) {
				if (instanceList) {
					var instanceListObjectId = instanceList.__objectId;
					var instanceListObjectDivId = mw3._getObjectDivId(instanceListObjectId);

					if (scrollDiv.scrollTop() > $('#' + instanceListObjectDivId).height() - scrollDiv.height() - 100) {
						if (prelastMore != lastMore) {
							prelastMore = lastMore;
							mw3.objects[lastMore].searchMore();
						}
					}
				}
			});

		}

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