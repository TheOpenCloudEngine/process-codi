var org_uengine_codi_mw3_knowledge_TopicPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;	
	
	
};

org_uengine_codi_mw3_knowledge_TopicPanel.prototype.loaded = function(){
	$('#navigator .depth2 a').click(function(){
		$('#navigator .depth2 .fist_menu li').removeClass('selected_navi');
		$(this).parent().addClass('selected_navi');
		$(".overlay_right").trigger('click');
	});
};
