var org_uengine_codi_mw3_model_InstanceTooltip = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
    
	var object = mw3.objects[this.objectId];
    
    if ( object) {
	   if( !object.loaded && object.instanceDefId != null ){
	   	   mw3.call(this.objectId, 'eventTriggerCheck');
	   }
	}	
    
    
    
    	$('.tooltip-top').tooltip({
		    placement: 'top',
		    viewport: {selector: 'body', padding: 2}
    	});
   
};


