var org_uengine_codi_mw3_model_MoreViewPerspective = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	this.object = mw3.objects[this.objectId];
	
	this.divId = mw3._getObjectDivId(this.objectId);
	this.div = $('#' + this.divId);		

	if(this.object.loader && !this.object.loaded)
		mw3.call(this.objectId, 'refresh');
	else{
		this.div.hover(
			function(){
				$(this).find('.west_more_btn').show();
			},
			function(){
				$(this).find('.west_more_btn').hide();
			}
		);
	}
};

org_uengine_codi_mw3_model_MoreViewPerspective.prototype.loaded = function(){
	$('#navigator .depth2 a').click(function(){
		$('#navigator .depth2 .fist_menu li').removeClass('selected_navi');
		$(this).parent().addClass('selected_navi');
	});
};