var SelectBox = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	this.objectDivId = '#' + mw3._getObjectDivId(this.objectId);
};

SelectBox.prototype = {
	getValue : function(){
		var value = $(this.objectDivId + ' option:selected').val(); 
		
		return value;
	}
};