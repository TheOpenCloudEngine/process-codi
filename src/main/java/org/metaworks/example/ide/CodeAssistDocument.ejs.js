var org_metaworks_example_ide_CodeAssistDocument = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.divObj = $('#' + this.divId);
	
	var codeAssist = mw3.getAutowiredObject('org.uengine.codi.mw3.admin.JavaCodeAssist');
	var codeAssistDiv = $('#' + mw3._getObjectDivId(codeAssist.__objectId)).parent();
	
	this.divObj.parent().css('left', codeAssistDiv.position().left + 500).css('top', codeAssistDiv.css('top'));
}

org_metaworks_example_ide_CodeAssistDocument.prototype = {
	destroy : function(){
		this.divObj.parent().remove();
	}
}