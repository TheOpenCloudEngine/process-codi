var org_uengine_codi_mw3_model_Preview = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;	
	
	if( this.object.mimeType == 'image'){
		$("#imagePreview_" + this.objectId).addClass("current");
        $("#pdfPreview_" + this.objectId).removeClass("current");
	}else{
		$("#imagePreview_" + this.objectId).removeClass("current");
        $("#pdfPreview_" + this.objectId).addClass("current");
	}
};

org_uengine_codi_mw3_model_Preview.prototype = {
	onloadPdf : function(){
		if(this.object.convertStatus != null){
				$("#convert_progress_pdf_" + this.objectId).hide();
				$('#converted_pdf_' + this.objectId).show();
		}
	},
	viewAsImages: function(){
		$("#imagePreview_" + this.objectId).addClass("current");
		$("#pdfPreview_" + this.objectId).removeClass("current");
		this.object.viewAsImages();
	},
	viewAsPDF: function(){
		$("#imagePreview_" + this.objectId).removeClass("current");
		$("#pdfPreview_" + this.objectId).addClass("current");
		this.object.viewAsPDF();
	}
};