var org_uengine_codi_mw3_model_IProcessMap = function(objectId, className){
	this.objectId = objectId;
	this.className = className;	
	this.divId = 'objDiv_' + this.objectId;
	
	this.obj = $('#' + this.divId);
	//this.obj.css('padding', '1px 1px 2px 1px');
	
	this.windowObjectId = this.obj.closest('.mw3_window').attr('objectId');
	
	var object = mw3.objects[this.objectId];
	
	if(object){
		this.obj.addClass(object.iconColor.value).attr('objectId', this.objectId);
		this.obj.parent().css({'border':'none'});
		
		var session = mw3.getAutowiredObject('org.uengine.codi.mw3.model.Session');
		
		var admin = true;
//		if(session && session.employee)
//			admin = session.employee.isAdmin;		
		
		if(admin){
			$( ".process_map2" ).sortable({
				connectWith: "ul",
				cancel: ".sptitle",
				stop:function(){
					var changed = false;
					var children = $(this).children('li');
					
					for(var i=0; i<children.length; i++){
						var child = children[i];				
						var objectId = $(child).attr('objectId');
						
						var object = mw3.objects[objectId];
						
						if(object.no != i){
							object.no = i;
							
							changed = true;
						}
					}
					
					if(changed){
						var objectId = $(this).closest('.processMapList').attr('objectId');
						
						mw3.call(objectId, 'save');
					}
				   
			   }
			});
			
			$(".process_map2 li").hover(
				function(){
					$(this).find(".editicon").css("display","block");
					
				},
				function(){
					$(this).find(".editicon").css("display","none");
				}			
			);
		}
	}
};

org_uengine_codi_mw3_model_IProcessMap.prototype = {
		
		whenClick : function(){
			var processMap = mw3.objects[this.objectId];
			if(processMap.cmTrgr!=null){
				
				var workItemHandlerFH = mw3.getAutowiredObject("org.uengine.codi.mw3.model.CommentWorkItem@-1").getFaceHelper();
				
				workItemHandlerFH.showCommandForm(processMap);
				var popup = mw3.getAutowiredObject("org.uengine.codi.mw3.model.Popup").getFaceHelper();
				if( popup ){
					var popupObjectId = popup.__objectId;
					popup.destoryPopup(popupObjectId);
				}
			}else{
				processMap.startWithRoleMapping();
			}
		},
		
		
		startLoading : function(){
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).startLoading)
				mw3.getFaceHelper(this.windowObjectId).startLoading();
			else{
				mw3.startLoading(this.objectId);
			}	
		},
		endLoading : function(){
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
				mw3.getFaceHelper(this.windowObjectId).endLoading();
			else{
				mw3.endLoading(this.objectId);
			}	
		},
		showStatus : function(message){
			
		}		
};



			