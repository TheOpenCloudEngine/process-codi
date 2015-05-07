var org_uengine_codi_mw3_knowledge_WfPanel_pt = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');
	
	$('#' + this.divId).parent().css("overflow","hidden");
	var dataXidx = 0;
	var dataZidx = 0;
	var dataRotateY = 0;
	$('.mainStep').each(function(index) {
		if( index%4 == 0 ){
			dataRotateY = 0;
			dataXidx = 0;
			dataZidx = -1000;
		}else	if( index%4 == 1){ 
			dataXidx = 500;
			dataZidx = -1500;
		}else	if( index%4 == 2){
			dataXidx = 0;
			dataZidx = -2000;
		}else	if( index%4 == 3){
			dataXidx = -500;
			dataZidx = -1500;
		}
		$(this).attr('data-x', dataXidx );
		$(this).attr('data-z', dataZidx );
		$(this).attr('data-rotate-y', dataRotateY );
		dataRotateY += 90;
	});
	$('.subStep').each(function(index) {
		if( index%4 == 0 ){
			dataRotateY = 0;
			dataXidx = 0;
			dataZidx = -1000;
		}else	if( index%4 == 1){ 
			dataXidx = 500;
			dataZidx = -1500;
		}else	if( index%4 == 2){
			dataXidx = 0;
			dataZidx = -2000;
		}else	if( index%4 == 3){
			dataXidx = -500;
			dataZidx = -1500;
		}
		$(this).attr('data-x', dataXidx );
		$(this).attr('data-z', dataZidx );
		$(this).attr('data-rotate-y', dataRotateY );
		dataRotateY += 90;
		$(this).attr('data-y', 1000 );
	});
	$('#global-next').click(function() {
		$('#jmpress').jmpress('next');
		return false;
	});
	$('#global-prev').click(function() {
		$('#jmpress').jmpress('prev');
		return false;
	});
	$("#global-next, #global-prev").addClass("ui-state-default ui-corner-all").hover(function() {
		$(this).addClass("ui-state-hover");
	}, function() {
		$(this).removeClass("ui-state-hover");
	});
	$('#jmpress').jmpress();
}