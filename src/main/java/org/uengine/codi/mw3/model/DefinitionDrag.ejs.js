var org_uengine_codi_mw3_model_DefinitionDrag = function(objectId, className){

}

org_uengine_codi_mw3_model_DefinitionDrag.prototype.cut = function(object, autowiredObjects){
    var session = mw3.getAutowiredObject("org.uengine.codi.mw3.model.Session");

    session.clipboard = (object);
}