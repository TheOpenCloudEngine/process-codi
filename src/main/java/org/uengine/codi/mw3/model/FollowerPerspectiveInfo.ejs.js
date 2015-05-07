var org_uengine_codi_mw3_model_FollowerPerspectiveInfo = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	this.object = mw3.objects[this.objectId];

	this.objectDiv.find('.toolbar li').not('.right').bind('click', function(){
		$(this).siblings().removeClass('toolbar_current').end().addClass('toolbar_current');
	});
	
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
	
	var serviceMethods = mw3.getServiceMethodByGroup(this.objectId, 'menu');
	
	var menuItems = mw3.makeMenuItemsByServiceMethod(serviceMethods);
				   
	var menu = new YAHOO.widget.Menu(
		"_menu_" + this.objectId,
		{
			zindex: 2000,
			itemdata: menuItems,
			lazyload: true
		}
	);
		
	$('#menu_' + this.objectId).bind('click', {objectId: this.objectId}, function(event, ui){
		var menu = YAHOO.widget.MenuManager.getMenu("_menu_" + event.data.objectId);
			   		
		var xy = YAHOO.util.Event.getXY(event);
		menu.cfg.setProperty("xy", xy);
		menu.show();
	});		
}

org_uengine_codi_mw3_model_FollowerPerspectiveInfo.prototype = {
	destroy : function(){
		var menu = YAHOO.widget.MenuManager.getMenu("_menu_" + this.objectId);
		
		menu.destroy();
	}
}