var org_uengine_codi_mw3_model_ProcessInstanceMonitor = function(objectId, className) {
	
	var object = mw3.objects[objectId];	
	
	this.objectId = objectId;
	this.className = className;
	
	if(object){
		var faceHelper = this;
		
		if(mw3.importScript('scripts/flowchart/flowchartDefinition.js')){
			mw3.importStyle('style/popupTooltip.css');
			mw3.importStyle('style/flowchart.css');
			
			mw3.importScript('scripts/raphael/raphael-min.js');
			mw3.importScript('scripts/ajax/ajaxCommon.js');
			mw3.importScript('scripts/crossBrowser/elementControl.js');
			mw3.importScript('scripts/flowchart/flowchartUtil.js');			
			mw3.importScript('scripts/popupTooltip.js');
			mw3.importScript('scripts/loopDrawForMW3.js', function(){mw3.getFaceHelper(objectId).load();});
			
		}else{
			var faceHelper = this;
			
			faceHelper.load();
		}
	}
};

org_uengine_codi_mw3_model_ProcessInstanceMonitor.prototype = {
	load : function(){
		var object = mw3.objects[this.objectId];	
		
		$('#divFlowchartArea').html(object.chartHTML);
		
		cleanAll();
		drawAll();
	},
	destroy : function() {
		var object = mw3.getObject(this.objectId);
		
		if(object){
			//alert("destroy" + this.objectId);
			$("#divDrawAreainstance" + object.instanceId).remove();
			
		}		
	}		
};
