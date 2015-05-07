var org_uengine_codi_mw3_knowledge_WorkflowyNode = function(objectId, className){
	this.objectId = objectId;
	this.className = className;

	this.wfInfoDiv = "#wfinfo_" + objectId;
	
	var object = mw3.objects[this.objectId];
	
	if(object.metaworksContext.when != 'read'){
		$('#wfnode_content_' + objectId).blur(function() {
			
			var name = $("#wfnode_content_" + objectId).val();
			
			if(name)
				object.name = name;
			
			object.metaworksContext.when = mw3.WHEN_VIEW;
		});
		
		$('#wfnode_content_' + objectId).focus(function() {
			object.metaworksContext.when = mw3.WHEN_EDIT;
		});	
		
		if(object.metaworksContext.when == mw3.WHEN_EDIT){
			var how = object.metaworksContext.how;
			var name = (object.name?object.name:'');
			var nameNext = (object.nameNext?object.nameNext:'');
			
			if(how == "add"){
				$('#wfnode_content_' + objectId).selectRange(0, 0);
			}else if(how == "remove" || how == "indent" || how == "outdent"){
				var pos = name.length - nameNext.length;
				
				$('#wfnode_content_' + objectId).selectRange(pos, pos);
			}
		
		}
		
		$('#wfnode_' + this.objectId).droppable({
			hoverClass: "ui-state-active",
			drop: function( event, ui ) {			
				var dragNodeId = ui.draggable.attr("nodeid");
				
				object.dragNode = mw3.objects[dragNodeId]; 
				
				mw3.call(objectId, "move");
			}
		});
		
		$('#wfnode_' + objectId).hover(			
			function () {
				if(!$('#move').hasClass("moving")){
					var html = '';
					
					html += '<div id=\"controls\">';  
					html += '  <div id=\"controlsRight\">';
					html += '    <a id=\"move\" title=\"Drag to move\"></a>';
					html += '  </div>'; 
					html += '</div>';
					
					$(this).append(html);
					
					$("#controls>#controlsRight>#move").draggable({
					        helper: "clone",
					        cursor: "move",
					        distance: 1,
					        start: function(event, ui) {
					        	$(this).attr("nodeid", objectId);				        	
					        	$(this).addClass("moving");
					        },
					        stop: function(event, ui) {
					        	$('#controls').remove();
					        }
					});
				}
			  }, 
			  function () {	
				  if(!$('#move').hasClass("moving"))
					  $(this).find('#controls').remove();			  
			  }
			
		);	
	}
	
	$('#objDiv_' + objectId).addClass("workflowy_node");
	$('#objDiv_' + objectId).attr("nodeid", objectId);
	$('#objDiv_' + objectId).css("position", "relative");
}

org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.getValue = function(){
	
	var object = mw3.objects[this.objectId];
	
	object.name = $('#wfnode_content_' + this.objectId).val();
		
	return object;
}

org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.up = function(isRight){
	   var searchObj = $('#objDiv_' + this.objectId).prev();
  
   if(!searchObj.length){
	   searchObj = $('#objDiv_' + this.objectId).parent().parent().parent().parent('div');
   }	   
  
   if(searchObj.length){
	   if(searchObj.attr('id').indexOf('info_') == 0){
		   searchObj = searchObj.prev();
		   
		   if(searchObj.find('.workflowy_node:last').length)
			   searchObj = searchObj.find('.workflowy_node:last');
	   }
	   
	   searchObj = searchObj.find('input:first');		   
		   searchObj.focus();
		   
		   if(isRight){
			   var pos = searchObj.val().length;
			   
			   searchObj.selectRange(pos,pos);
		   } else {
			   searchObj.selectRange(0,0);
		   }
	   }
}



org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.down = function(){
	
   // 자식 검색
   var searchObj = $('#objDiv_' + this.objectId).find('.workflowy_node:first');
   
   // 자식 미존재, 동일 노드 하위 검색(존재하지 않으면 부모의 동일노드 하위 검색)
   if(!searchObj.length){
	   var info = false;
	   var exist = false;   
	   
	   searchObj = $('#objDiv_' + this.objectId);
   
   while(!exist && searchObj.length && searchObj.hasClass('workflowy_node')){
	   var temp = searchObj.nextUntil('.workflowy_node');
	   		   
	   if(temp.attr('id').indexOf('info_') == 0)
		   info = true;
	   else
		   info = false;
	   
	   if(info){
		   exist = (temp.next().length > 0);
	   }else
		   exist = (temp.length > 0);	   
	   
	   if(exist){
		   if(info)
			   searchObj = temp.next();
		   else
			   searchObj = temp;
	   }else{
		   searchObj = temp.parent().parent().parent().parent('div');
		   }		   
	  }
   } 
   
   if(searchObj.length && searchObj.hasClass('workflowy_node')){
   searchObj = searchObj.find('input:first');
	   
	   searchObj.focus();
	   searchObj.selectRange(0,0);
   }		   
}


org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.press = function(inputObj){
	
	if(!mw3.loaded){
		window.event.returnValue = false;
		
		return;
	}		
	
	var event = window.event;	
	var object = mw3.objects[this.objectId];
	
	switch (event.keyCode) {

	  case 37   :   // left
	   var t = inputObj.value, s = this.getSelectionStart(inputObj), e = this.getSelectionEnd(inputObj)
	   
	   if(s == 0){
		   window.event.returnValue = false;
		   
		   this.up(true);			   
	   }
	   
	   break;
  case 38   :   // up		   
	   this.up();
	   
	   window.event.returnValue = false;
	   
	   break;
	   
  case 39   :   // right
	   var t = inputObj.value, s = this.getSelectionStart(inputObj), e = this.getSelectionEnd(inputObj)
	  
	   if(s == t.length){
		   window.event.returnValue = false;
		   
		   this.down();
	   }
	   
	   break;
  case 40   :   // down
	   window.event.returnValue = false;
	   this.down();
	  
	   break;

  case 9    :	// tab
	   var t = inputObj.value, s = this.getSelectionStart(inputObj), e = this.getSelectionEnd(inputObj)
	  
	   if(s == t.length){
		   object.nameNext = "";
       }else if(s == 0){
    	   object.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';		    	  			   
	   }else{
		   object.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';
	   }
	   
	   window.event.returnValue = false;
	 
	   if(event.shiftKey)
		   mw3.call(this.objectId, 'outdent');
	   else
		   mw3.call(this.objectId, 'indent');
	  	
       break;
 
  case 13    :	// enter		  
	   var t = inputObj.value, s = this.getSelectionStart(inputObj), e = this.getSelectionEnd(inputObj)
	   
	   if(s == t.length){
		   object.name = t;
		   object.nameNext = "";
       }else if(s == 0){
    	   object.name = "";
    	   object.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';		    	  			   
	   }else{
		   object.name = t.substring(0, s).replace(/ /g, '\xa0') || '\xa0';
		   object.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';
	   }
	   
	   inputObj.value = object.name;
	   
	   window.event.returnValue = false;
	   			   
	   mw3.call(this.objectId, 'add');
	   
	   break;
  case 8     :	// back
	  var t = inputObj.value, s = this.getSelectionStart(inputObj), e = this.getSelectionEnd(inputObj)
	  
	  if(s == e && s == 0){
	      if(s == t.length){
	    	  object.name = t;
	    	  object.nameNext = "";
	      }else if(s == 0){
	    	  object.name = "";
	    	  object.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';		    	  
	      }else{
	    	  object.name = t.substring(0, s).replace(/ /g, '\xa0') || '\xa0';
	    	  object.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';
	      }
		  		      		      
		  inputObj.value = object.name;

		  window.event.returnValue = false;
   		  
		  if(object.name == "")
			  this.up(true);
			  
		  mw3.call(this.objectId, 'remove');
	  }
	  
	  break;
  case 46    :	// del
	   var info = false;
	   var exist = false;   
	   
	   searchObj = $('#objDiv_' + this.objectId);
	   
	   var temp = searchObj.nextUntil('.workflowy_node');
		   		   
	   if(temp.attr('id').indexOf('info_') == 0)
		   info = true;
	   else
		   info = false;
	   
	   if(info){
		   exist = (temp.next().length > 0);
	   }else
		   exist = (temp.length > 0);	   
		   
	   if(exist){
		   if(info)
			   searchObj = temp.next();
		   else
			   searchObj = temp;
		   
		   object.name = $('#wfnode_content_' + this.objectId).val();
		   
		   targetObjectId = searchObj.attr('nodeid');			   
		   targetObject = mw3.objects[targetObjectId];
		   
		   targetObject.nameNext = targetObject.name;
				  
		   mw3.call(targetObjectId, 'remove');
		   }		   
	  default    :
	        break;
	}
}

org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.getSelectionStart = function(o) {
	if (o.createTextRange) {
		var r = document.selection.createRange().duplicate()
		r.moveEnd('character', o.value.length)
	if (r.text == '') return o.value.length
		return o.value.lastIndexOf(r.text)
	} else return o.selectionStart
}

org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.getSelectionEnd = function(o) {
	if (o.createTextRange) {
		var r = document.selection.createRange().duplicate()
		r.moveStart('character', -o.value.length)
		return r.text.length
	} else return o.selectionEnd
}

org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.endLoading = function(){
	$(this.wfInfoDiv).html("");
}
org_uengine_codi_mw3_knowledge_WorkflowyNode.prototype.startLoading = function(){
	$(this.wfInfoDiv).html("<img src='dwr/metaworks/images/circleloading.gif' align=middle> LOADING ...");
}