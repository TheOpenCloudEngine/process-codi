var org_uengine_codi_mw3_knowledge_VisualizationType = function(objectId, className){
	this.objectId = objectId;
	
}

org_uengine_codi_mw3_knowledge_VisualizationType.prototype = {
		getValue : function(){
			var object = mw3.getObjectFromUI(this.objectId);//objects[this.objectId];
			
			var visType = $('#fwStyle_'+this.objectId +':checked').val();
			if(visType)
				object.visType = visType;
			
			return object;
		}
}