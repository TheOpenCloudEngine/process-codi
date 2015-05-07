var org_uengine_codi_mw3_model_IInstance = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.objDiv = $('#' + this.divId);
	this.object = mw3.objects[this.objectId];
	
	if(this.object == null)
		return false;

	this.windowObjectId = this.objDiv.closest('.mw3_window').attr('objectId');

	this.objDiv.parent().css("background","#DFE1E3");
	this.objDiv.addClass('instance_li');
	this.objDiv.addClass('instance').addClass('mw3_resize');
	this.objDiv.attr('status' , this.object.status);
	
	if( this.object.status == 'Completed'){
		this.objDiv.css("background", "#f2f3f4");
	}

	this.objDiv.hover(function(){				
		$('#objDiv_' + objectId + ' .instanceBtn').show();
	},function(){	
		$('#objDiv_' + objectId + ' .instanceBtn').hide();
	});

	var object = this.object;
	if(object && object.metaworksContext && object.metaworksContext.when == 'blinking'){
		this.blinking();
	}

	if( object && object.metaworksContext && object.metaworksContext.how == 'sns'){
		$('#' + this.divId).css('border-top','1px solid #E3E3E3');
		$('.tbl_type td').css('border','none');
	}

	this.objDiv.click({objectId : this.objectId}, function(event){	 	
		var objectId = event.data.objectId;

		mw3.call(objectId, 'view');
	});
	
	var faceHelper = this;
	
	faceHelper.resize();	
};



org_uengine_codi_mw3_model_IInstance.prototype = {
		destroy : function(){
			$('#' + this.divId);
		},
		startLoading : function(svcNameAndMethodName){
			if(svcNameAndMethodName == 'view'){
				mw3.getFaceHelper(this.objectId).unBlinking();
				
				$(".tbl_type").parent().each(function(){
					if( $(this).attr("status") == 'Completed' ){
						$(this).addClass('instance_complated').removeClass('instance_normal instance_select');
					}else{
						$(this).addClass('instance_normal').removeClass('instance_complated instance_select');
					}
				});
				this.objDiv.addClass('instance_select');
				
				 var divId = mw3._getObjectDivId(this.objectId);
				 var offset = $('#' + divId).offset();                    
                 
                 if(offset){
	                    var offsetRight = offset.left + $('#' + divId).width() - 20;                    
	                    var offsetMiddle = offset.top + ($('#' + divId).height()/2- 11);
	                    
	                    var html;
	                    html = "<div id='loading_div_" + divId + "' style='position:absolute; z-index:9999999; top:"+offsetMiddle+"px; left:"+offsetRight+"px;'><div id='info_div_box' ><img src='dwr/metaworks/images/circleloading.gif'></div></div>";
	                    
	                    $('body').append(html);
                 }
			}
		
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).startLoading)
				mw3.getFaceHelper(this.windowObjectId).startLoading();
		},
		endLoading : function(){
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
				mw3.getFaceHelper(this.windowObjectId).endLoading();
			 $('body>#loading_div_' + this.divId).remove();
		},
		showStatus : function(message){

		},
		unBlinking : function(){
			$('#' + this.divId + ' .innerNewInst').hide();
		},

		blinking : function(){
			var blinkDiv = $('#' + this.divId + ' .innerNewInst');

			blinkDiv.show( 'pulsate' ,  1500 ).height(blinkDiv.parent().height());		
		},
		resize : function(){
			var phone_size = 300;
			
			if(phone_size <= this.objDiv.width()){
				if(this.objDiv.hasClass('phone')){
					this.objDiv.removeClass('phone');
				}				
			}else{
				if(!this.objDiv.hasClass('phone')){
					this.objDiv.addClass('phone');
				}
			}
		}
};
