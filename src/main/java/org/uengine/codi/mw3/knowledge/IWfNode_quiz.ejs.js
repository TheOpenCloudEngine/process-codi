var org_uengine_codi_mw3_knowledge_IWfNode_quiz = function(objectId, className){
	var thisFaceHelper = this;
	
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');
	
	
	this.obj = $('#objDiv_' + this.objectId);
	this.mw3Obj = mw3.objects[objectId];
	 
	
	this.prevName = this.mw3Obj.name;	
	this.keyword = this.mw3Obj.name;
	this.timeout;
	this.process = false;
		
	var content = this.content = $('#wfnode_content_' + this.objectId);
	
	if(this.mw3Obj.metaworksContext.when != 'read'){
		content.bind('focus', {objectId: this.objectId}, function(event) {		
			$('.wfnode_current').removeClass('wfnode_current_focus').hide();			
			$('#wfnode_current_' + event.data.objectId).addClass('wfnode_current_focus').show();			
			
			var faceHelper = mw3.getFaceHelper(objectId);			
			if(faceHelper && faceHelper.mw3Obj.type != 'img'){
				var value = content.val();
							
				if(faceHelper.change) 
					faceHelper.change(value);			
			}
			
		});
		content.bind('blur', {objectId: this.objectId}, function() {
			var faceHelper = mw3.getFaceHelper(objectId);			
			if(faceHelper && faceHelper.mw3Obj.type != 'img'){
				var value = content.val();
				
				if (faceHelper.timeout) {
					clearTimeout(faceHelper.timeout);
				}	
				
				if (faceHelper.prevName != value){
					faceHelper.prevName = value;
					
					faceHelper.mw3Obj.name = value;			
					faceHelper.mw3Obj.save();
				}
			}
		});
		content.bind('dblclick', {objectId: this.objectId}, function() {
			mw3.call(objectId, 'drillInto');
		});
		
		if(this.mw3Obj && this.mw3Obj.focus){
			content.focus();
			
			var how = this.mw3Obj.metaworksContext.how;
			var name = (this.mw3Obj.name?this.mw3Obj.name:'');
			var nameNext = (this.mw3Obj.nameNext?this.mw3Obj.nameNext:'');

			if(how == "add" || how == "new"){
				content.selectRange(0, 0);
			}else if(how == "remove" || how == "indent" || how == "outdent"){
				var pos = name.length - nameNext.length;

				content.selectRange(pos, pos);
			}

		}

		var object = this.mw3Obj;
		$('#wfnode_' + this.objectId).droppable({
			hoverClass: "wfActive",
			drop: function( event, ui ) {
				var dragNodeId = ui.draggable.attr("objectId");
				
				var list = $('#objDiv_' + dragNodeId).find('.workflowy_node');
				for(var i=0; i<list.length; i++){
					if($(list[i]).attr('objectId') == objectId){
						alert('옮길 수 없는 위치입니다.');
						
						return false;
					}
				}
				
				object.dragNode = mw3.objects[dragNodeId]; 
				
				mw3.call(objectId, "move");
			}
		});

		$('#wfnode_' + objectId).hover(			
				function () {
					/*$(this).css("background","#F2F8FD");*/
					if(!$('#move').hasClass("moving")){
						
						var object = mw3.objects[objectId];
						var authorId = object.authorId;
						
						var html = '';

						html += '<div id=\"controls\">'; 						
						html += '  <div id=\"controlsRight\">';
						//html += '    <div id=\"authorId\" style=\"top:-50px\" ><img src=\"images/portrait/'+ authorId +'.jpg\" width=\"30\" height=\"30\"></div>';
						html += '    <a id=\"move\" style=\"top:-42px\" title=\"Drag to move\"></a>';
						html += '  </div>'; 
						html += '</div>';

						$(this).append(html);

						$("#controls>#controlsRight>#move").draggable({
							helper: "clone",
							cursor: "move",
							distance: 1,
							axis: "y",
							start: function(event, ui) {								
								$(this).attr("objectId", objectId);				        	
								$(this).addClass("moving");
							},
							stop: function(event, ui) {
								$('#controls').remove();
							}
						});
					}
				}, 
				function () {	
					/*$(this).css("background","none");*/
					if(!$('#move').hasClass("moving"))
						$(this).find('#controls').remove();			  
				}

		);	
	}
	this.obj.addClass("workflowy_node").attr("objectId", objectId).attr("nodeId", this.mw3Obj.id).attr('parentId', this.mw3Obj.parentId).css("position", "relative");
	
	
	
	var listCount = 0;
	var slideList = function(){	
		
		if($('#objDiv_' + objectId + ' .wfQuiz').eq(0).find('li.quizList').length > 0){
			if(listCount <= $('#objDiv_' + objectId + ' .wfQuiz').eq(0).find('li.quizList>div>div').length){
				
				$('#objDiv_' + objectId + ' .quizList>div>div').eq(listCount).height(27).show("slide",500);
				listCount++;
				setTimeout(function(){slideList();},500);
			}
		}		
	}
	

	$('#objDiv_' +this.objectId + ' .quizList>div>div').hide();
	slideList();
	
	$('td.radioTr').eq(0).find('input').hide();
	$('#objDiv_' +this.objectId + ' #quizRadio').eq(0).click(function(){alert("정답입니다")})
	
	if(this.mw3Obj.first){
		setTimeout(function(){
			mw3.call(objectId, 'load');
		},1);
	}

}

org_uengine_codi_mw3_knowledge_IWfNode_quiz.prototype = {
		getPrev : function(){
			var searchObj = this.obj;
				
			do{
				searchObj = searchObj.prev();
				
				if(searchObj.hasClass('workflowy_node'))
					break;
			}while(searchObj.length > 0)
				
			return searchObj;
			
		},
		getNext : function(){
			var searchObj = this.obj;
				
			do{
				searchObj = searchObj.next();
				
				if(searchObj.hasClass('workflowy_node'))
					break;
			}while(searchObj.length > 0)
				
			return searchObj;
			
		},
		getParent : function(){			
			var searchObj = this.obj;
				
			do{
				searchObj = searchObj.parent();
				
				if(searchObj.hasClass('workflowy_node'))
					break;
			}while(searchObj.length > 0)
				
			return searchObj;			
		},
		getFirstChild : function(){
			return this.obj.find('.workflowy_node:first');
		},
		getLastChild : function(){
			var child = this.obj.find('.workflowy_node:first');
			while(child.length > 0){
				var childId = child.attr('objectId');
				
				var next = mw3.getFaceHelper(childId).getNext();
				if(next.length > 0)
					child = next;
				else
					break;
			}
			return child;
		},		
		getValue : function(){			
			return this.mw3Obj;
		},
		focus : function(){
			this.content.focus();
		},
		up : function(isRight){
			var focus = this.getPrev();
			if(focus.length == 0){
				focus = this.getParent();
			}else{
				var objectId = focus.attr('objectId');
				
				var child = mw3.getFaceHelper(objectId).getLastChild();
				
				while(child.length > 0){
					focus = child;
					
					var childId = child.attr('objectId');
					child = mw3.getFaceHelper(childId).getLastChild();
				}
			}

			if(focus.length > 0){
				var objectId = focus.attr('objectId');
			
				mw3.getFaceHelper(objectId).focus();
				
				// 커서 위치 조정
				var input = focus.find('#wfnode_content_' + objectId);				
				if(isRight){
					var pos = input.val().length;

					input.selectRange(pos,pos);
				} else {
					input.selectRange(0,0);
				}				
			}

		},
		down : function(){
			var focus = this.getFirstChild();
			if(focus.length == 0){
				focus = this.getNext();
			}
			
			var parent = this.obj;			
			while(focus.length == 0 && parent.length > 0){
				focus = parent;
				var objectId = focus.attr('objectId');
				
				parent = mw3.getFaceHelper(objectId).getParent();
				if(parent.length > 0){
					var parentId = parent.attr('objectId');				
					focus = mw3.getFaceHelper(parentId).getNext();
				}
			}
			
			if(focus.length > 0){
				var objectId = focus.attr('objectId');
			
				mw3.getFaceHelper(objectId).focus();
				
				// 커서 위치 조정
				var input = focus.find('#wfnode_content_' + objectId);
				input.selectRange(0,0);
			}
		},
		change : function(value){
			this.keyword = value;
			
			if (this.timeout) {
				clearTimeout(this.timeout);
			}			
			
			var objectId = this.objectId;
			
			this.timeout = setTimeout(function() {
				
				var mashupToolNames = ["GoogleImage", "LMS", "KMS"];
				
				for(var i in mashupToolNames){
					var mashupToolName = mashupToolNames[i];
					
					var mashup = mw3.getAutowiredObject('org.uengine.codi.mw3.knowledge.Mashup' + mashupToolName);
					var mashupTool = null;
					
					if(mashup){
						
						if(mashup.search){
							mashup.keyword = value;
							mashup.targetNodeId = mw3.objects[objectId].id;
							
							mw3.setObject(mashup.__objectId, mashup); //since mashup has it's UI
							
							mashup.search();
						}else{
							mashupTool = mashup.__getFaceHelper();
							
							if(mashupTool){
								if(value == '')
									mashupTool.clear();
								else
									mashupTool.search(value);
							}					
							
						}
						
					}
				}
			}, 1000);
			
			
		},
		calcValue : function(inputObj){
			var t = inputObj.value, s = this.getSelectionStart(inputObj), e = this.getSelectionEnd(inputObj);

			if(s == t.length){
				this.mw3Obj.name = t;
				this.mw3Obj.nameNext = "";
			}else if(s == 0){
				this.mw3Obj.name = "";
				this.mw3Obj.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';		    	  			   
			}else{ //커서의 위치에 따라서 글을 자르는 로직
				this.mw3Obj.name = t.substring(0, s).replace(/ /g, '\xa0') || '\xa0';
				this.mw3Obj.nameNext = t.substring(s).replace(/ /g, '\xa0') || '\xa0';
			}			
		},
		keyup : function(inputObj){
			var keyword = inputObj.value;
			
			if(this.keyword == keyword)
				return true;
			
			this.change(keyword);
		},
		keydown : function(inputObj){
			if(this.process){
				window.event.returnValue = false;

				return;
			}
			
			var event = window.event;
			
			switch (event.keyCode) {
			case 37   :   // left
				var s = this.getSelectionStart(inputObj);

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
				var s = this.getSelectionStart(inputObj);

				if(s == inputObj.value.length){
					window.event.returnValue = false;

					this.down();
				}

				break;
			case 40   :   // down
				window.event.returnValue = false;
				this.down();

				break;

			case 9    :	// tab
				window.event.returnValue = false;
				
				this.calcValue(inputObj);

				if(event.shiftKey){					
					if(this.mw3Obj.parentId != '-1'){
						var parent = this.getParent();	
						if(parent.length > 0){
							var parentId = parent.attr('objectId');
														
							if(parentId != '-1'){
								this.mw3Obj.outdent();	
							}
						} 
					}
				}else{
					if(this.mw3Obj.no > 0)
						mw3.call(this.objectId, 'indent');
				}

				break;

			case 13    :	// enter		 
				window.event.returnValue = false;
				
				this.calcValue(inputObj);
				this.mw3Obj.add();

				break;
			case 8     :	// back
				this.calcValue(inputObj);
				
				var s = this.getSelectionStart(inputObj), e = this.getSelectionEnd(inputObj);

				if(s == e && s == 0){
					if(this.mw3Obj.no > 0 || this.mw3Obj.parentId != '-1'){
						window.event.returnValue = false;
						
						this.mw3Obj.remove();
					}
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

					//object.name = $('#wfnode_content_' + this.objectId).val();

					targetObjectId = searchObj.attr('objectId');			   
					targetObject = mw3.objects[targetObjectId];

					targetObject.nameNext = targetObject.name;

					mw3.call(targetObjectId, 'remove');
				}		   
			default    :
				break;
			}
		},
		getSelectionStart : function(o) {
			if (o.createTextRange) {
				var r = document.selection.createRange().duplicate()
				r.moveEnd('character', o.value.length)
				if (r.text == '') return o.value.length
				return o.value.lastIndexOf(r.text)
			} else return o.selectionStart
		},
		getSelectionEnd : function(o) {
			if (o.createTextRange) {
				var r = document.selection.createRange().duplicate()
				r.moveStart('character', -o.value.length)
				return r.text.length
			} else return o.selectionEnd
		},
		destroy : function(){
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
				mw3.getFaceHelper(this.windowObjectId).endLoading();
		},
		startLoading : function(){
			this.process = true;
			
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).startLoading)
				mw3.getFaceHelper(this.windowObjectId).startLoading();
		},
		endLoading : function(){
			this.process = false;
			if(this.windowObjectId && mw3.getFaceHelper(this.windowObjectId) && mw3.getFaceHelper(this.windowObjectId).endLoading)
				mw3.getFaceHelper(this.windowObjectId).endLoading();
		},
		showStatus : function(message){
			this.process = false;
			
		},
		showError : function(message){
			this.process = false;
			
			alert(message);
		},
				
		insertNodeAfter: function(googleData){						
			this.mw3Obj.nameNext = googleData.tbUrl;
			this.mw3Obj.typeNext = "img";
			this.mw3Obj.add();
		}

}