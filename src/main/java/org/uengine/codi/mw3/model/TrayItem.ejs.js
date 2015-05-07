var org_uengine_codi_mw3_model_TrayItem = function(objectId, className){

	this.objectId = objectId;
	this.className = className;	
	this.divId = '#objDiv_' + this.objectId;
	
	
	$(this.divId).hover(
		function(){
			$(this).find('.top_window_btn_line').show();
			$(this).find('#navigationTitle span div').css("margin-right","60px")
		},
		function(){
			$(this).find('.top_window_btn_line').hide();
			$(this).find('#navigationTitle span div').css("margin-right","13px")
		}	
	);
	
	
	
	var object = mw3.objects[this.objectId];
	
	if(object && object.metaworksContext && object.metaworksContext.how == 'down'){
		$(this.divId + ' .topcontentcontainer').show().width(500);
		var layoutId = $(this.divId).closest('.mw3_layout').attr('objectId');
	}
};