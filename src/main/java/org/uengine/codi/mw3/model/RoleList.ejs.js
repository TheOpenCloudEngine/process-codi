var org_uengine_codi_mw3_model_RoleList = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];
	
};

org_uengine_codi_mw3_model_RoleList.prototype = {
	toAppend : function(html){		
		html = html.replace('<div', '<li');
		html = html.replace('div>', 'li>');
		
		var ul = $('#' + this.divId + ' ul');
		ul.append(html);
	}
};