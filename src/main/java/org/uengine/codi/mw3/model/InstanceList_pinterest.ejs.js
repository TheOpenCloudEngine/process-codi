var pinterest_list_firstLoad = true;


var org_uengine_codi_mw3_model_InstanceList_pinterest = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
    var $container = $('#pinterest_container');
    
    var reload = null;
    
    if(pinterest_list_firstLoad){
	   	reload = function(){
		    $container.masonry({
			      itemSelector: '.box',
			      columnWidth: 100,
			      isAnimated: !Modernizr.csstransitions
			//      cornerStampSelector: '.corner-stamp'
			    })};
			    
		pinterest_list_firstLoad = false;
	}else{

	    reload = function(){
	
	    	$container.masonry( 'reload' );
	    };
	    
	}

    reload();

    setTimeout(reload, 2000);
    

}

