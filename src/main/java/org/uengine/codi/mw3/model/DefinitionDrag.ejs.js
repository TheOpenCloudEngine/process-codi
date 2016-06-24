var org_uengine_codi_mw3_model_InstanceDrag = function(objectId, className){

}

org_uengine_codi_mw3_model_InstanceDrag.prototype.drag = function(object, autowiredObjects){
    var clipboard = mw3.getAutowiredObject("org.metaworks.widget.Clipboard");

    clipboard.content = (object);
}