var org_forx_model_codi_SearchKeywordBox = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	this.timeout;
	
	$("#search_" + this.objectId).focus();
}

org_forx_model_codi_SearchKeywordBox.prototype.getValue = function() {
	var object = mw3.objects[this.objectId];

	object.keyword = $("#search_" + this.objectId).val();

	return object;
}

org_forx_model_codi_SearchKeywordBox.prototype.keydown = function() {
	var objectId = this.objectId;
	
	if (this.timeout) {
		clearTimeout(this.timeout);
	}
	
	this.timeout = setTimeout(function() {
		mw3.call(objectId, 'search');
	}, 500);
	
}