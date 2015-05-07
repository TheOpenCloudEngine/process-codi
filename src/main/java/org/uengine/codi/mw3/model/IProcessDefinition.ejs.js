//alert('xxxx');

var org_uengine_codi_mw3_model_IProcessDefinition = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;

	var theDiv = $("#objDiv_" + objectId);
	var object = mw3.objects[objectId];
	
    document.addEventListener(
		"mouseup",

   		function(e) {
			
			if(e.srcElement.parentElement.parentElement==theDiv[0] && e.which == 3){
				object.contextMenu();
			}
		},
		
		true
	);
	
}