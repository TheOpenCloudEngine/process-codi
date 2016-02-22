var org_uengine_codi_mw3_model_TopMenuPanel = function(objectId, className) {
    this.objectId = objectId;
    this.className = className;
    this.divId = mw3._getObjectDivId(this.objectId);
    this.divObj = $('#' + this.divId);
    
    this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');
    
};

var west_layout = $('#navigator').parent().parent().parent().parent().parent().parent();
var west2_layout = $('#navigator').parent().parent().parent().parent();
var center_layout = west_layout.prev();
var center2_layout = center_layout.find(".ui-layout-center").eq(0);
var bar_layout = west_layout.next();

function toggleLeft(){

	if($('#navigator').hasClass('m_left')){
		
		$('#navigator').removeClass('m_left');
		$('#navigator').addClass('s_left');
		west_layout.css("width","50");
		west2_layout.css("width","50");
		center_layout.css({"left":"51px","width":center_layout.width()+50});
		center2_layout.css("width",center2_layout.width()+50);
		bar_layout.css("left","50px");
		$('.contact_panel').hide();
		
	}else if($('#navigator').hasClass('s_left')){
		
		$('#navigator').removeClass('s_left');
		west_layout.css("width","185");
		west2_layout.css("width","185");
		center_layout.css({"left":"186px","width":center_layout.width()-136});
		center2_layout.css("width",center2_layout.width()-136);
		bar_layout.css("left","185px");
		$('.contact_panel').show();
		
	}else{
		
		$('#navigator').addClass('m_left');
		west_layout.css("width","100");
		west2_layout.css("width","100");
		center_layout.css({"left":"101px","width":center_layout.width()+85});
		center2_layout.css("width",center2_layout.width()+85);
		bar_layout.css("left","100px");
		$('.contact_panel').hide();
		
	}
}