var todoBadgeLastMore;
var org_uengine_codi_mw3_model_TodoBadgeInstanceList = function(objectId, className) {
    this.objectId = objectId;
    this.className = className;
    this.divId = mw3._getObjectDivId(this.objectId);
    this.divObj = $('#' + this.divId);
    
    this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');
    var object = mw3.objects[objectId];
	if(object && object.instances == null){
        todoBadgeLastMore = objectId;
    }
    
	if (object) {
		var scrollDiv = $('#objDiv_' + this.objectId).parent();
		var prelastMore = todoBadgeLastMore;
		scrollDiv.scroll(function(e){
			var instanceListObjectId = object.__objectId;
			var instanceListObjectDivId = mw3._getObjectDivId(instanceListObjectId);
			
			if (scrollDiv.scrollTop() > $('#' + instanceListObjectDivId).height() - scrollDiv.height() - 100) {
				if (prelastMore != todoBadgeLastMore) {
					prelastMore = todoBadgeLastMore;
					mw3.objects[todoBadgeLastMore].more();
				}
			}
		});
		
	}
};

org_uengine_codi_mw3_model_TodoBadgeInstanceList.prototype = {
	startLoading : function(){
        if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).startLoading)
            mw3.getFaceHelper(this.windowObjectId).startLoading();
    },
    endLoading : function(){
        if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
            mw3.getFaceHelper(this.windowObjectId).endLoading();
    },
    showStatus : function(message){
        
    }
};