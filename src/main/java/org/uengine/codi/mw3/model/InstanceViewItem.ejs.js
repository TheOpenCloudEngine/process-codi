var org_uengine_codi_mw3_model_InstanceViewItem = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
};

org_uengine_codi_mw3_model_InstanceViewItem.prototype = {
	loaded : function(){
		var object = mw3.objects[this.objectId];
		if( !object.loaded ){
			object.load();
		}
	}
};