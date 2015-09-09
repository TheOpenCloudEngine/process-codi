var org_uengine_codi_mw3_model_TopSearchBox = function(objectId, className) {
    this.objectId = objectId;
    this.className = className;
    this.divId = mw3._getObjectDivId(this.objectId);
    this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');

}

org_uengine_codi_mw3_model_TopSearchBox.prototype = {
    getValue : function() {
        var object = mw3.objects[this.objectId];

        object.keyword = $("#search_" + this.objectId).val();

        return object;
    }
}