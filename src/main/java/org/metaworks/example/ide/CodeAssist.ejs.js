var org_metaworks_example_ide_CodeAssist = function(objectId, className){
	this.objectId = objectId;

	var sel = document.getElementById("assist_" + objectId );
	sel.selectedIndex = 1; 
	sel.focus();
	
	var thisFaceHelper = this;
	
	var object = mw3.objects[this.objectId];
	
	if(object==null || object.assistances.length==0){
		var sourceCode = mw3.objects[object.srcCodeObjectId];		
		var editor = sourceCode.__getFaceHelper().editor;
		
		$("#" + mw3.popupDivId).remove();
		editor.focus();
	}else{
		document.addEventListener(
			"keyup",
	
	   		function(e) {
				if (e && e.keyCode==13 && sel == e.srcElement){
					thisFaceHelper.enter(sel);
				}
			},
			
			false
		);
	}

}

org_metaworks_example_ide_CodeAssist.prototype.enter = function(sel){
	var object = mw3.objects[this.objectId];
	var sourceCode = mw3.objects[object.srcCodeObjectId];
	
	var value = sel.options[sel.selectedIndex].value;
	
	var editor = sourceCode.__getFaceHelper().editor;
	editor.insert(value);
	editor.focus();
	
	$("#" + mw3.popupDivId).remove();
}

var docRequestedTime = 0;

org_metaworks_example_ide_CodeAssist.prototype.requestDoc = function(sel){
	
//TODO: please implement these commented part (reducing undesired request) later:
//	setTimeout("realRequestDoc('" + this.objectId +"', " + sel.selectedIndex + ", sel)", 1500);
//}
//
//
//function realRequestDoc(objectId, selectedIndexThatTime, sel){
//	if(seletedIndexThatTime != sel.selectedIndex) return;

	var object = mw3.objects[this.objectId];

	var value = sel.options[sel.selectedIndex].value;

	var sourceCode = mw3.objects[object.srcCodeObjectId];
	var theEditor = sourceCode.__getFaceHelper().editor;

	var whereEnd = theEditor.getCursorPosition();
	var whereStart = {column: 0, row: whereEnd.row};
	
	
	var line = theEditor.getSession().doc.getTextRange({start: whereStart, end: whereEnd});

	object.selectedItem = value;
	object.lineAssistRequested = line; 
	object.showDoc();
	

}