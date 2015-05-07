/*var richText = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.editorId = 'ckEditor_' + this.objectId;

	
	this.editor = CKEDITOR.replace(this.editorId, {
		fullPage : true,
		extraPlugins : 'docprops',
		htmlEncodeOutput : false,
		entities : false
	});
}

richText.prototype.getValue = function() {	
	if(this.editor) 
		return this.editor.getData();
}*/