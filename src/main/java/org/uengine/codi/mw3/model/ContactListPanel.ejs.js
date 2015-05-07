var org_uengine_codi_mw3_model_ContactListPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;	
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	this.object = mw3.objects[this.objectId];
	
	$('#objDiv_' + this.objectId).css({'height':'100%'}).addClass('mw3_resize').attr('objectId', this.objectId);
	$('#accordion_' + this.objectId).accordion({ fillSpace:	true });
	
		
};

org_uengine_codi_mw3_model_ContactListPanel.prototype = {
	resize : function(){
		$('#accordion_' + this.objectId).accordion('resize');
	
	},
	getValue : function(){
		// get check nodes
		var checkNodes = [];

		if(this.object){
			this.objectDiv.find('input[type=checkbox]:checked').each(function(){
				var mappedId = $(this).parentsUntil("div[classname='org.uengine.codi.mw3.model.IUser']").parent("div[classname='org.uengine.codi.mw3.model.IUser']").attr('id');
				if( mappedId.length > 2 ){
					var idStr = mappedId.split('_');
					var ObjId = idStr[idStr.length -1];
					var node = mw3.objects[ObjId];
					checkNodes.push(node);
				}
			});
		}
		this.object.checkNodes = checkNodes;
		return this.object;
	}
};