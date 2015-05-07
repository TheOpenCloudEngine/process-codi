var org_uengine_codi_mw3_model_ProcessDesigner = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];
	
	window.frames['process_' + this.objectId].location.href = mw3.base + "/processmanager/ProcessDesigner.jnlp?defId=" + object.alias + "&defVerId=" + object.alias + "&userId=" + mw3.getAutowiredObject('org.uengine.codi.mw3.model.Session').user.userId;
}