var org_uengine_codi_mw3_model_NewInstancePanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;	
	this.divId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.divId);
	this.object = mw3.objects[this.objectId];
	
	if(this.object && this.object.title){
		var title = this.object.title;
			
		// set window title
		this.windowObjectId = this.objectDiv.closest('.mw3_window').attr('objectId');
	
		if(this.windowObjectId)
			mw3.getFaceHelper(this.windowObjectId).setTitle(mw3.localize(title));
	}
	
	
	var securityBox = $('#openmind_box');
	
	if(securityBox.length > 0){
		var securityLevel = this.object.securityLevel.selected;
		
		$('#openmind_btn').click(function(){
			securityBox.toggle();
			
			if(securityBox.is(':visible')){
				securityBox.hover(
					// mouse in
					function() {
						$(this).attr('focus', 'in');
					},
					// mouse out
					function() {
						$(this).attr('focus', 'out');
					}
				).attr('focus', 'out');
				
				$('body').bind('mousedown', function(){
					if('in' !=  securityBox.attr('focus'))
						$('#openmind_btn').trigger('click');
				});		
			}else{
				securityBox.unbind('hover');
				
				$('body').unbind('mousedown');
			}
		});
		
		$('#openmind_box li').bind('mousedown', {objectId : this.objectId}, function(){
			$(this).siblings().removeClass('selected').end().addClass('selected');

			if(mw3.getChildObjectId(objectId, 'securityLevel') == null){
				var object = mw3.getObject(objectId);
				object.securityLevel.selected = $(this).attr('value');
			}
				
			$('#openmind_btn').trigger('click');
		}).filter(function(){
			if(securityLevel == $(this).attr('value'))
				return true;
			
		}).addClass('selected');
	}
};