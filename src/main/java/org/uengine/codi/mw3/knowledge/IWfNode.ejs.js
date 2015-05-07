var org_uengine_codi_mw3_knowledge_IWfNode = function(objectId, className) {
	var thisFaceHelper = this;

	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	this.windowObjectId = $('#' + this.divId).closest('.mw3_window').attr('objectId');

	this.obj = $('#workflowy_node_' + this.objectId);
	this.mw3Obj = mw3.objects[objectId];

	if (this.mw3Obj) {
		this.prevName = this.mw3Obj.name;
		this.keyword = this.mw3Obj.name;
		this.timeout;
		this.process = false;

		var content = this.content = $('#wfnode_content_' + this.objectId);

		if (this.mw3Obj.metaworksContext.when != 'read') {
			content.bind('focus', {
				objectId : this.objectId
			},
					function(event) {
						$('.wfnode_current')
								.removeClass('wfnode_current_focus').hide();
						$('#wfnode_current_' + event.data.objectId).addClass(
								'wfnode_current_focus').show();

						var faceHelper = mw3.getFaceHelper(objectId);
						if (faceHelper && faceHelper.mw3Obj.type != 'img') {
							var value = content.val();

							if (faceHelper.change)
								faceHelper.change(value);
						}

					});
			content.bind('blur', {
				objectId : this.objectId
			}, function() {
				var faceHelper = mw3.getFaceHelper(objectId);
				if (faceHelper && faceHelper.mw3Obj.type != 'img') {
					var value = content.val();

					if (faceHelper.timeout) {
						clearTimeout(faceHelper.timeout);
					}

					if (faceHelper.prevName != value) {
						faceHelper.prevName = value;

						faceHelper.mw3Obj.name = value;
						faceHelper.mw3Obj.save();
					}
				}
			});
			
			/*
			content.bind('dblclick', {
				objectId : this.objectId
			}, function() {
				mw3.call(objectId, 'drillInto');
			});
			*/

			if (this.mw3Obj && this.mw3Obj.focus) {
				content.focus();

				var how = this.mw3Obj.metaworksContext.how;
				var name = (this.mw3Obj.name ? this.mw3Obj.name : '');
				var nameNext = (this.mw3Obj.nameNext ? this.mw3Obj.nameNext
						: '');

				if (how == "add" || how == "new") {
					content.selectRange(0, 0);
				} else if (how == "remove" || how == "indent"
						|| how == "outdent") {
					var pos = name.length - nameNext.length;

					content.selectRange(pos, pos);
				}

			}
			
			/**  지식맵 드래그가 안되서 기능 막아놓습니다 수정:최지선(cjs)
			$('#objDiv_' + objectId).bind('dragstart', 
				function(e){
					e.preventDefault();
					if (console && console.log) console.log('dragstart');
					e.stopPropagation();
				}
			);

			$('#objDiv_' + objectId).bind('dragend', 
					function(e){
						e.preventDefault();
						if (console && console.log) console.log('dragend');
						e.stopPropagation();
					}
				);

			*/
			
			$('#wfnode_' + objectId).hover(
				// mouse in
				function() {
					if(mw3.dragging){
						if (console && console.log) console.log('in');
					}					
				},
				// mouse out
				function() {
					if(mw3.dragging){
						if (console && console.log) console.log('out');
					}
				}
			);

			/*
			var object = this.mw3Obj;
			$('#wfnode_' + this.objectId).droppable({
						hoverClass : "wfActive",
						drop : function(event, ui) {
							var dragNodeId = ui.draggable.attr("objectId");
							
							if(typeof dragNodeId != 'undefined'){
								var list = $('#objDiv_' + dragNodeId).find('.workflowy_node');
								for ( var i = 0; i < list.length; i++) {
									if ($(list[i]).attr('objectId') == objectId) {
										alert('옮길 수 없는 위치입니다.');

										return false;
									}
								}
							}

							mw3.call(objectId, "drop");
						}
					});

			$('#wfnode_' + objectId).hover(function() {
				if (!$('#move').hasClass("moving")) {
					var html = '';

					html += '<div id=\"controls\">';
					html += '  <div id=\"controlsRight\">';
					html += '    <a id=\"move\" title=\"Drag to move\">aaaaaaaaaaaaaaaa</a>';
					html += '  </div>';
					html += '</div>';

					$(this).append(html);

					$("#controls>#controlsRight>#move").draggable({
						helper : "clone",
						cursor : "move",
						distance : 1,
						axis : "y",
						start : function(event, ui) {
							mw3.dragging = true;
							
							$(this).attr("objectId", objectId);
							$(this).addClass("moving");
							
							mw3.call(objectId, 'drag');							
						},
						stop : function(event, ui) {
							mw3.dragging = false;
							
							$('#controls').remove();
						}
					});
				}
			}, function() {
				if (!$('#move').hasClass("moving"))
					$(this).find('#controls').remove();
			}

			);
			*/
			
		}
		
		/*
		this.obj.addClass("workflowy_node").attr("objectId", objectId).attr(
				"nodeId", this.mw3Obj.id)
				.attr('parentId', this.mw3Obj.parentId).css("position",
						"relative");
		*/
		
		if (this.mw3Obj.parentId == this.mw3Obj.rootNodeId) {
			this.obj.find('.wfNode').eq(0).addClass("first_wfNode");
		}
	}
	if(!(this.mw3Obj.childNode && this.mw3Obj.childNode.length > 0) && (this.mw3Obj.type == 'backlog' || this.mw3Obj.type == 'plan')){
		// type 이 brainstorm 일 경우만 탄다. 
		thisFaceHelper.recalc();
	}
		
};

org_uengine_codi_mw3_knowledge_IWfNode.prototype = {
	loaded : function(){
		if(this.mw3Obj && this.mw3Obj.first){			
			//var expanded = getCookie('bpm_knol.expanded.' + this.mw3Obj.id);
			
			//if(this.mw3Obj.parentId == null || expanded == 'true'){
				mw3.call(this.objectId, 'load');
			//}
		}		
	},
	getPrev : function() {
		var searchObj = this.obj;

		do {
			searchObj = searchObj.parent().parent().prev().children().eq(0).children('div');

			if (searchObj.hasClass('workflowy_node'))
				break;
		} while (searchObj.length > 0)

		return searchObj;

	},
	getNext : function() {
		var searchObj = this.obj;
		
		do {
			searchObj = searchObj.parent().parent().next().children().eq(0).children('div');

			if (searchObj.hasClass('workflowy_node'))
				break;
		} while (searchObj.length > 0)

		return searchObj;

	},
	getParent : function() {
		var searchObj = this.obj;

		do {
			searchObj = searchObj.parent();

			if (searchObj.hasClass('workflowy_node'))
				break;
		} while (searchObj.length > 0)

		return searchObj;
	},
	getFirstChild : function() {
		return this.obj.find('.workflowy_node:first');
	},
	getLastChild : function() {
		var child = this.obj.find('.workflowy_node:first');
		while (child.length > 0) {
			var childId = child.attr('objectId');

			var next = mw3.getFaceHelper(childId).getNext();
			if (next.length > 0)
				child = next;
			else
				break;
		}
		return child;
	},
	focus : function() {
		this.content.focus();
	},
	up : function(isRight) {
		var focus = this.getPrev();
		if (focus.length == 0) {
			focus = this.getParent();
		} else {
			var objectId = focus.attr('objectId');

			var child = mw3.getFaceHelper(objectId).getLastChild();

			while (child.length > 0) {
				focus = child;

				var childId = child.attr('objectId');
				child = mw3.getFaceHelper(childId).getLastChild();
			}
		}

		if (focus.length > 0) {
			var objectId = focus.attr('objectId');

			mw3.getFaceHelper(objectId).focus();

			// 커서 위치 조정
			var input = focus.find('#wfnode_content_' + objectId);
			if (isRight) {
				var pos = input.val().length;

				input.selectRange(pos, pos);
			} else {
				input.selectRange(0, 0);
			}
		}

	},
	down : function() {
		var focus = this.getFirstChild();
		if (focus.length == 0) {
			focus = this.getNext();
		}

		var parent = this.obj;
		while (focus.length == 0 && parent.length > 0) {
			focus = parent;
			var objectId = focus.attr('objectId');

			parent = mw3.getFaceHelper(objectId).getParent();
			if (parent.length > 0) {
				var parentId = parent.attr('objectId');
				focus = mw3.getFaceHelper(parentId).getNext();
			}
		}

		if (focus.length > 0) {
			var objectId = focus.attr('objectId');

			mw3.getFaceHelper(objectId).focus();

			// 커서 위치 조정
			var input = focus.find('#wfnode_content_' + objectId);
			input.selectRange(0, 0);
		}
	},
	change : function(value) {
		this.keyword = value;

		if (this.timeout) {
			clearTimeout(this.timeout);
		}

		var objectId = this.objectId;

		this.timeout = setTimeout(
				function() {

					var mashupToolNames = [ "GoogleImage", "LMS", "KMS", "Wiki" ,"Video", "Slideshare" ];

					for ( var i in mashupToolNames) {
						var mashupToolName = mashupToolNames[i];

						var mashup = mw3
								.getAutowiredObject('org.uengine.codi.mw3.knowledge.Mashup'
										+ mashupToolName);
						var mashupTool = null;
						if (mashup) {

							if (mashup.search) {
								mashup.keyword = value;
								mashup.targetNodeId = mw3.objects[objectId].id;

								mw3.setObject(mashup.__objectId, mashup); // since
																			// mashup
																			// has
																			// it's
																			// UI

								mashup.search();
							} else {
								mashupTool = mashup.__getFaceHelper();
								if (mashupTool) {
									if (value == '') {
										mashupTool.clear();
									} else {
										mashupTool.search(value);
									}
								}

							}

						}
					}
				}, 1000);

	},
	calcValue : function(inputObj) {
		var t = inputObj.value, s = this.getSelectionStart(inputObj), e = this
				.getSelectionEnd(inputObj);

		if (s == t.length) {
			this.mw3Obj.name = t;
			this.mw3Obj.nameNext = "";
		} else if (s == 0) {
			this.mw3Obj.name = "";
			this.mw3Obj.nameNext = t.substring(s).replace(/ /g, '\xa0')
					|| '\xa0';
		} else { // 커서의 위치에 따라서 글을 자르는 로직
			this.mw3Obj.name = t.substring(0, s).replace(/ /g, '\xa0')
					|| '\xa0';
			this.mw3Obj.nameNext = t.substring(s).replace(/ /g, '\xa0')
					|| '\xa0';
		}
	},
	keyup : function(inputObj) {
		var keyword = inputObj.value;

		if (this.keyword == keyword)
			return true;

		this.change(keyword);
	},
	keydown : function(inputObj) {
		if (this.process) {
			window.event.returnValue = false;

			return;
		}

		var event = window.event;
		
		switch (event.keyCode) {
		case 37: // left
			var s = this.getSelectionStart(inputObj);

			if (s == 0) {
				window.event.returnValue = false;

				this.up(true);
			}

			break;
		case 38: // up
			this.up();

			window.event.returnValue = false;

			break;

		case 39: // right
			var s = this.getSelectionStart(inputObj);

			if (s == inputObj.value.length) {
				window.event.returnValue = false;

				this.down();
			}

			break;
		case 40: // down
			window.event.returnValue = false;
			this.down();

			break;

		case 9: // tab
			window.event.returnValue = false;

			this.calcValue(inputObj);

			if (event.shiftKey) {
				if (this.mw3Obj.parentId != '-1') {
					var parent = this.getParent();
					if (parent.length > 0) {
						var parentId = parent.attr('objectId');

						if (parentId != '-1') {
							if (console && console.log) console.log('outdent');
							this.mw3Obj.outdent();
						}
					}
				}
			} else {
				mw3.call(this.objectId, 'indent');
			}

			break;

		case 13: // enter
			window.event.returnValue = false;

			this.calcValue(inputObj);
			this.mw3Obj.add();

			break;
		case 8: // back
			this.calcValue(inputObj);

			var s = this.getSelectionStart(inputObj), e = this
					.getSelectionEnd(inputObj);

			if (s == e && s == 0) {
				if (this.mw3Obj.no > 0 || this.mw3Obj.parentId != '-1') {
					window.event.returnValue = false;

					this.mw3Obj.remove();
				}
			}

			break;
		case 46: // del
			var info = false;
			var exist = false;

			searchObj = $('#objDiv_' + this.objectId);

			var temp = searchObj.nextUntil('.workflowy_node');

			if (temp.attr('id').indexOf('info_') == 0)
				info = true;
			else
				info = false;

			if (info) {
				exist = (temp.next().length > 0);
			} else
				exist = (temp.length > 0);

			if (exist) {
				if (info)
					searchObj = temp.next();
				else
					searchObj = temp;

				// object.name = $('#wfnode_content_' + this.objectId).val();

				targetObjectId = searchObj.attr('objectId');
				targetObject = mw3.objects[targetObjectId];

				targetObject.nameNext = targetObject.name;

				mw3.call(targetObjectId, 'remove');
			}
		default:
			break;
		}
	},
	getSelectionStart : function(o) {
		if (o.createTextRange) {
			var r = document.selection.createRange().duplicate();
			r.moveEnd('character', o.value.length)
			if (r.text == '')
				return o.value.length;
			return o.value.lastIndexOf(r.text);
		} else
			return o.selectionStart;
	},
	getSelectionEnd : function(o) {
		if (o.createTextRange) {
			var r = document.selection.createRange().duplicate();
			r.moveStart('character', -o.value.length);
			return r.text.length;
		} else
			return o.selectionEnd;
	},
	destroy : function() {
		if (this.windowObjectId && mw3.getFaceHelper(this.windowObjectId)
				&& mw3.getFaceHelper(this.windowObjectId).endLoading)
			mw3.getFaceHelper(this.windowObjectId).endLoading();
	},
	startLoading : function() {
		this.process = true;

		if (this.windowObjectId && mw3.getFaceHelper(this.windowObjectId)
				&& mw3.getFaceHelper(this.windowObjectId).startLoading)
			mw3.getFaceHelper(this.windowObjectId).startLoading();
	},
	endLoading : function() {
		this.process = false;
		if (this.windowObjectId && mw3.getFaceHelper(this.windowObjectId)
				&& mw3.getFaceHelper(this.windowObjectId).endLoading)
			mw3.getFaceHelper(this.windowObjectId).endLoading();
	},
	showStatus : function(message) {
		this.process = false;

	},
	showInfo: function(message) {
	
	},
	showError : function(message) {
		this.process = false;

		alert(message);
	},

	insertNodeAfter : function(googleData) {
		this.mw3Obj.nameNext = googleData.tbUrl;
		this.mw3Obj.typeNext = "img";
		this.mw3Obj.add();
	},
	insertNodeVideo : function(googleData) {
		var playUrl = googleData.playUrl;
		var newUrl = "";
		if (playUrl.indexOf("&autoplay=1") != -1) {
			newUrl = playUrl.replace("&autoplay=1", "");
		} else {
			newUrl = playUrl;
		}
		this.mw3Obj.nameNext = newUrl;
		this.mw3Obj.typeNext = "video";
		this.mw3Obj.add();
	},
	
	checkLoaded : function(){
		var loaded = true;
		
		var parent = this.getParent();
		var parentId = parent.attr('objectId');
		
		var faceHelper = mw3.getFaceHelper(parentId);
		
		if(faceHelper){
			var child = faceHelper.getFirstChild();
			while(child.length > 0){
				var childId = child.attr('objectId');
				var childObj = mw3.objects[childId];
				
				var childFaceHelper = null;
				if(childObj.id == this.mw3Obj.id)
					childFaceHelper = this;
				else
					childFaceHelper = childObj.getFaceHelper();
				
				if(childFaceHelper){
					child = childFaceHelper.getNext();
				}else{
					loaded = false;
					break;
				}
			}
		}
		
		return loaded;
	},
	getTotalBV : function(){
		var totBV = 0;
		
		var parent = this.getParent();
		var parentId = parent.attr('objectId');
		
		var faceHelper = mw3.getFaceHelper(parentId);
		
		if(faceHelper){
			var child = faceHelper.getFirstChild();
			while(child.length > 0){
				var childId = child.attr('objectId');
				var childObj = mw3.objects[childId];
				
				totBV += childObj.bv;
								
				var childFaceHelper = null;
				if(childObj.id == this.mw3Obj.id)
					childFaceHelper = this;
				else
					childFaceHelper = childObj.getFaceHelper();
					
				if(childFaceHelper)
					child = childFaceHelper.getNext();
				else
					break;
			}
		}

		return totBV;
	},
	
	getEffort : function(){
		var effort = this.mw3Obj.effort;		
		if(this.getFirstChild().length > 0)
			effort = this.mw3Obj.totEffort;
		
		return effort;
	},
	
	getTotalEffort : function(){
		var totEffort = 0;
		
		var parent = this.getParent();
		var parentId = parent.attr('objectId');
		
		var faceHelper = mw3.getFaceHelper(parentId);
		
		if(faceHelper){
			var child = faceHelper.getFirstChild();
			while(child.length > 0){
				var childId = child.attr('objectId');
				var childObj = mw3.objects[childId];
				
				var childFaceHelper = null;
				if(childObj.id == this.mw3Obj.id)
					childFaceHelper = this;
				else
					childFaceHelper = childObj.getFaceHelper();
				
				totEffort += childFaceHelper.getEffort();
				
				if(childFaceHelper)
					child = childFaceHelper.getNext();
				else
					break;
			}
		}

		return totEffort;		
	},
	calcBV : function(){
		this.mw3Obj['bv'] = this.mw3Obj.benefit + this.mw3Obj.penalty;
	},
	
	calcROI : function(){
		var effort = this.getEffort();
		
		var roi = 0;		
		if(effort > 0)			
			roi = this.mw3Obj.bv / effort;
		
		this.mw3Obj['roi'] = roi;
		
	},
	recalc : function(){
		this.calcBV();
		this.calcROI();
		
		if(this.checkLoaded()){
			var parentDivObj = this.getParent();
			if(parentDivObj.length > 0){
				var parentId = parentDivObj.attr('objectId');
				var parentObj = mw3.objects[parentId];
				
				var parentFaceHelper = parentObj.getFaceHelper();
				
				if(parentFaceHelper){
					var totBV = this.getTotalBV();
					var totEffort = this.getTotalEffort();
					
					if(this.mw3Obj.metaworksContext.how != 'ROOT'){	
						parentObj.totEffort = totEffort;
						parentFaceHelper.recalc();
					}
				
					var childs = [];
					
					var child = parentFaceHelper.getFirstChild();
					while(child.length > 0){
						var childId = child.attr('objectId');
						var childObj = mw3.objects[childId];
						
						if(childObj){
							childs.push(childObj);
							
							var childFaceHelper = null;
							if(childObj.id == this.mw3Obj.id)
								childFaceHelper = this;
							else
								childFaceHelper = childObj.getFaceHelper();
														
							if(childFaceHelper){
								childObj['rBV'] = (childObj.bv / totBV) * 100;					

								childFaceHelper.calcRefresh();
								child = childFaceHelper.getNext();
							}else
								break;
						}else{
							break;
						}
					}	
					
					if(parentObj.metaworksContext.how != 'ROOT'){
						var effortOver = totEffort - parentObj.budget;
						var compare = function (a,b) {
						  if (a.roi < b.roi)
						     return 1;
						  if (a.roi > b.roi)
						    return -1;
						  return 0;
						};
						
						childs.sort(compare);
						
						for(var i=childs.length-1; i>=0; i--){
							var childObj = childs[i];
							var childFaceHelper = childObj.getFaceHelper();
							if(childObj.id == this.mw3Obj.id)
								childFaceHelper = this;
							else
								childFaceHelper = childObj.getFaceHelper();
							
							var effort = childFaceHelper.getEffort();
							
							if(effortOver > 0 && effort > 0){
								effortOver -= effort;
								
								childFaceHelper.effortOverCheck();
							}else{
								childFaceHelper.effortOverUnCheck();
							}							
						}
					}
				}
			}
		}
	},
	
	calcRefresh : function(){
		var rBV = this.mw3Obj.rBV;
		if(isNaN(rBV) || typeof rBV == 'undefined')
			rBV = 'N/A';
		else
			rBV.toFixed(1);
		
		var roi = this.mw3Obj.roi;
		if(isNaN(roi) || typeof roi == 'undefined')
			roi = 0;

		var effort = this.getEffort();		
		
		var startDate = this.mw3Obj.startDate;
		if(typeof startDate == 'undefined' || startDate == null)
			startDate = 'N/A';
		else
			startDate = startDate.format('yyyy-MM-dd');
		
		var endDate = this.mw3Obj.endDate;
		if(typeof endDate == 'undefined' || endDate == null)
			endDate = 'N/A';
		else
			endDate = endDate.format('yyyy-MM-dd');
		
		this.obj.find('#bv_' + this.objectId).html('bv:' + this.mw3Obj.bv);
		this.obj.find('#effort_' + this.objectId).html('effort:' + effort);
		this.obj.find('#budget_' + this.objectId).html('budget:' + this.mw3Obj.budget);
		this.obj.find('#rBV_' + this.objectId).html('rBV:' + rBV);
		this.obj.find('#roi_' + this.objectId).html('ROI:' + roi.toFixed(1));
		
		this.obj.find('#startDate_' + this.objectId).html('startdate:' + startDate);
		this.obj.find('#endDate_' + this.objectId).html('enddate:' + endDate);
		this.obj.find('#progress_' + this.objectId).html('progress:0');		
	},
	
	toOpener : function(target){
		this.mw3Obj.budget = target.budget;
		this.mw3Obj.effort = target.effort;
		this.mw3Obj.benefit = target.benefit;
		this.mw3Obj.penalty = target.penalty;
		this.mw3Obj.startDate = target.startDate;		
		this.mw3Obj.endDate = target.endDate;
		
		var firstChild = this.getFirstChild();
		
		if(firstChild.length > 0){
			var firstChildObjId = firstChild.attr('objectId');
			
			mw3.getFaceHelper(firstChildObjId).recalc();
		}else{
			this.recalc();
		}
	},
	
	toPrepend : function(target){
		var html = mw3.locateObject(target, null);
		
		$('<div>').appendTo(this.obj.find('#wfnode_' + this.objectId).next()).prepend(html);
	},
	toPrev : function(target){
		var html = mw3.locateObject(target, null);
		
		$('<div>').insertBefore(this.obj.parent().parent()).append(html);
		
	},
	toNext : function(target){
		var html = mw3.locateObject(target, null);
		
		$('<div>').insertAfter(this.obj.parent().parent()).append(html);
	},
	toAppend : function(target){
		var html = mw3.locateObject(target, null);
		
		$('<div>').appendTo(this.obj).append(html);
	},
	
	effortOverCheck : function(){
		this.obj.find('#nodeId_' + this.objectId).html($('<strike>').css('color', 'red').append(this.mw3Obj.id));
	},
	
	effortOverUnCheck : function(){
		this.obj.find('#nodeId_' + this.objectId).html(this.mw3Obj.id);
	}
};