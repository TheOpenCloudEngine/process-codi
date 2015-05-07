var org_uengine_codi_mw3_admin_PageNavigator= function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;	
	
	$('#objDiv_' + objectId).addClass('pageflip');	
	
	$('#objDiv_' + objectId + ' area').one('click', {objectId: this.objectId}, function(event, ui){
		$('#objDiv_' + event.data.objectId + ' area').unbind();
		
		// 존재하는 팝업 제거
		$('.target_popup').remove();	
	});
	
	//Page Flip on hover
	$(".pageflip").hover(
		function() {
			$(".pageflip img , .msg_block").stop()
				.animate({
					width: '307px', 
					height: '319px'
				}, 700); 
		}, 
		function() {
			$(".pageflip img").stop() 
				.animate({
					width: '42px', 
					height: '42px'
				}, 700);
			$(".msg_block").stop() 
				.animate({
					width: '42px', 
					height: '42px'
				}, 700);
		});	
	
	if(this.object.oce){
		$('.msg_block').css({'background':'url(images/waveStyle/pageNavigation_admin01.png) no-repeat right top'});
	}else{
		//지식맵 활성화시 페이지 플립 이미지 변경	
		if(this.object.sns) {
			if(this.object.ide){
				if(this.object.knowlege){
					$('.msg_block').css({'background':'url(images/waveStyle/pageNavigation_SIK.png) no-repeat right top'});
				}else{
					$('.msg_block').css({'background':'url(images/waveStyle/pageNavigation_SI.png) no-repeat right top'});
				}
			}else{
				if(this.object.knowlege){
					$('.msg_block').css({'background':'url(images/waveStyle/pageNavigation_SK.png) no-repeat right top'});
				}
			}
		}else {
			$('.msg_block').css({'background':'url(images/waveStyle/pageNavigation4.png) no-repeat right top'});
		}
	}
	
	/*
	$('.goProcess').hover(
			function(){$('.msg_block').css("background","url(images/waveStyle/pageNavigation_Process.png)");},
			function(){$('.msg_block').css("background","url(images/waveStyle/pageNavigation.png)")}	
	)
	$('.goKnowledge').hover(
			function(){$('.msg_block').css("background","url(images/waveStyle/pageNavigation_Knowledge.png)");},
			function(){$('.msg_block').css("background","url(images/waveStyle/pageNavigation.png)")}	
	)
	$('.goIDE').hover(
			function(){$('.msg_block').css("background","url(images/waveStyle/pageNavigation_IDE.png)");},
			function(){$('.msg_block').css("background","url(images/waveStyle/pageNavigation.png)")}	
	)
	*/
	
};

org_uengine_codi_mw3_admin_PageNavigator.prototype = {
	startLoading : function(){
		$('.logo').after('<div id=\"mw3_progress\" style=\"width:30px; height:15px; float:left; margin-top:20px;  margin-right:20px;"><img src=\"images/waveStyle/ajax-loader_t.gif\" /></div>');
	},
	endLoading : function(){
		setTimeout(function(){
			$('#mw3_progress').remove();
		}, 500);		
	},
	destroy : function(){
		setTimeout(function(){
			$('#mw3_progress').remove();
		}, 500);		
		
	},
	showStatus : function(){
		
	}
};