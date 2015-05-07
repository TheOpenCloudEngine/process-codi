var org_uengine_codi_mw3_model_UserNicknameChanger = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	var object = mw3.objects[this.objectId];
	
	if(object){
		this.userNickname = object.userNickname;
		
		$('#userNickname_' + this.objectId).focus(function(){
			$(this).css("border","solid 1px #ccc");
		});
		
		$('#userNickname_' + this.objectId).bind('blur', function(){
			mw3.getFaceHelper(objectId).change();
			$(this).css("border","none");
	
		});
		
		var autoSizing = function(){
			var h=$('#userNickname_' + objectId);
			
			if(h.length > 0)
				h.height(21).height(h[0].scrollHeight);
		};  
		
		$('#userNickname_' + this.objectId).live("keyup keydown", autoSizing);
	
		autoSizing();
	}
};

org_uengine_codi_mw3_model_UserNicknameChanger.prototype = {
	getValue : function(){
		var object = mw3.objects[this.objectId];
		
		if(object)
			object.userNickname = $('#userNickname_' + this.objectId).val();
	},
	change : function(){
		var userNickname = $('#userNickname_' + this.objectId).val();
		
		if(this.userNickname != userNickname){
			mw3.call(this.objectId, 'change');			
		}
	}
};

