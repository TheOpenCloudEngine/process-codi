var org_uengine_codi_mw3_model_InstanceNameChanger = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	var object = mw3.objects[this.objectId];
	
	if(object){
		this.instanceName = object.instanceName;
		
		//$('#instanceName_' + this.objectId).focus(function(){
		//	$(this).css("border","solid 1px #ccc");
		//});
		
		$('#instanceName_' + this.objectId).bind('blur', function(){
			mw3.getFaceHelper(objectId).change();
			$(this).css("border","none");
	
		});
		
		$('#instanceName_' + this.objectId).bind('keydown', function(event){
			if(event.keyCode == 13 && !event.shiftKey){
				event.preventDefault();
				this.blur();
			}
		});
		
		var autoSizing = function(){
			var h=$('#instanceName_' + objectId);
			
			if(h.length > 0)
				h.height(21).height(h[0].scrollHeight);//where 60 is minimum height of textarea
		};  
		
		//$('#instanceName_' + this.objectId).live("keyup keydown", autoSizing);
		$('#instanceName_' + this.objectId).on("keyup keydown", autoSizing);

		autoSizing();
	}
};

org_uengine_codi_mw3_model_InstanceNameChanger.prototype = {
	getValue : function(){
		var object = mw3.objects[this.objectId];
		
		if(object)
			object.instanceName = $('#instanceName_' + this.objectId).val();
	},
	change : function(){
		var instanceName = $('#instanceName_' + this.objectId).val();
		
		if(this.instanceName != instanceName){
			mw3.call(this.objectId, 'change');			
		}
	}
};

