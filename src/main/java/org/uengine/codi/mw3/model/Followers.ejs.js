var org_uengine_codi_mw3_model_Followers = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.objectDiv.addClass('mw3_resize');
	
	this.windowObjectId = this.objectDiv.closest('.mw3_window').attr('objectId');
	
	var faceHelper = this;
	faceHelper.resize();
}

org_uengine_codi_mw3_model_Followers.prototype = {
	toAppend : function(appendObject) {
		$('<span>').css('float', 'left').appendTo(this.objectDiv.find('.usersAdded span:first').parent()).html(mw3.locateObject(appendObject, appendObject.__className, null));
	},
	showStatus : function(message){
		
	},
	
	resize : function(){
		
		if( $('#' + this.objectDivId + ' .followers_box>div>div>span').length * 44 > $('#' + this.objectDivId + " .adduserline").width()){
			$('#' + this.objectDivId + ' .followers_box').css("margin-right","40px")
			$('#' + this.objectDivId + ' .followers_addbtn').css({"position":"absolute","right":"10px"});
			if($('#' + this.objectDivId + ' #replyUsers>dl').hasClass('view_more_dl')){
				
			}else{
				$('#' + this.objectDivId + ' .moreFollowerBtn').show();
			}
			
		}else{
			$('#' + this.objectDivId + ' .followers_box').css("margin-right","0px")
			$('#' + this.objectDivId + ' .followers_addbtn').css({"position":"relative","right":"0px"});
			$('#' + this.objectDivId + ' .moreFollowerBtn').hide();
		}
	},
	
	moreFllowers : function(){
		$('#' + this.objectDivId + ' #replyUsers>dl').addClass('view_more_dl');
		$('#' + this.objectDivId + ' .adduserline').addClass('view_more_line')
		$('#' + this.objectDivId + ' .moreFollowerBtn').hide();
		$('#' + this.objectDivId + ' .closeFollowerBtn').show();
	},
	closeFllowers : function(){
		$('#' + this.objectDivId + ' #replyUsers>dl').removeClass('view_more_dl');
		$('#' + this.objectDivId + ' .adduserline').removeClass('view_more_line')
		$('#' + this.objectDivId + ' .closeFollowerBtn').hide();
		$('#' + this.objectDivId + ' .moreFollowerBtn').show();
	}
	
}
