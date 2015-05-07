var org_uengine_codi_mw3_admin_WebEditor = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.object = mw3.objects[this.objectId];
	
	//$('#' + this.divId).css('height', '640px');
	
	this.editorId = 'ckEditor_' + this.objectId;
	
	if($('#' + this.editorId).length > 0){
		this.editor = CKEDITOR.replace(this.editorId, {
			fullPage : true,
			extraPlugins : 'docprops',
			htmlEncodeOutput : false,
			entities : false		
		});
		
		this.editor.setData(this.object.contents);		
	}
};

org_uengine_codi_mw3_admin_WebEditor.prototype.getValue = function() {
	
	if(this.editor) {
		var editor = {
			__className : 'org.uengine.codi.mw3.admin.WebEditor',
			contents : this.editor.getData()
		};
		
		return editor;
	}
	
};