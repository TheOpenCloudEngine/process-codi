var org_uengine_codi_mw3_model_ProcessMapColor = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = '#objDiv_' + this.objectId;
	
	var object = mw3.objects[this.objectId];
	
	if(object.value)
		$(this.divId + ' .' + object.value).attr('checked', 'checked');
}

org_uengine_codi_mw3_model_ProcessMapColor.prototype = {
	getValue : function(){
		var object = mw3.objects[this.objectId];

		object.value = $(this.divId + ' :input:radio[name=iconbg]:checked').val(); 
		
		return object;
	}
}