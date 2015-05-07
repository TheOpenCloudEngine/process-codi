var org_uengine_codi_mw3_knowledge_WfPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.divObj = $('#' + this.divId);
	
	if(mw3.objects[objectId] == null)
		return true;
	
	this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');
	
	$('#' + this.divId).addClass('wf_panel').attr('objectId', this.objectId);
	
	
	rootNodeId = mw3.objects[objectId].rootNodeId;
	
	var object = mw3.objects[this.objectId];
	
	if(object.first){
		setTimeout(function(){
			mw3.call(objectId, 'load');
		},1);
	}
};

org_uengine_codi_mw3_knowledge_WfPanel.prototype = {		
	startLoading : function(){
		if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).startLoading)
			mw3.getFaceHelper(this.windowObjectId).startLoading();
	},
	endLoading : function(){
		if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
			mw3.getFaceHelper(this.windowObjectId).endLoading();
	},
	destroy : function(){
		this.endLoading();
	},
	toAppend : function(target){
		var appendTarget = $(this.divObj).find('.workflowy_node:first').children(':first');
		
		var html = mw3.locateObject(target, null);
		
		appendTarget.append(html);
		
	},
	refresh : function(target){
		var object = mw3.objects[this.objectId];
		if(object.rootNodeId != target.rootNodeId){
			mw3.setObject(this.objectId, target);
		}
	}
};

var rootNodeId;
