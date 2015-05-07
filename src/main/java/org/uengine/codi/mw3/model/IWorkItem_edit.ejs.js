var org_uengine_codi_mw3_model_IWorkItem_edit = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.commandTrigger = null;
	this.commandActivityAppAlias = null;
	this.commandParameters = null;
	this.type = null;

	var value = mw3.objects[objectId];
	
	this.instanceFirst = true;
	if( value.instId != null && value.instId != 0){
		this.instanceFirst = false;
	}

	$("#post_" + this.objectId).focus();
	if($("#post_" + this.objectId).focus()){

		var obj = $("#post_" + this.objectId)[0];
		if(obj){
			var pos = obj.textLength;
			
			if ( obj.setSelectionRange ) { 
				obj.focus(); 
				obj.setSelectionRange(pos,pos); 
			} else if ( obj.createTextRange ) { 
				var c = obj.createTextRange(); 
				c.move("character",pos); 
				c.select(); 
			}
		}
	}
	
	//$("#post_" + this.objectId).keydown()
	
	$("#commandDiv_" + this.objectId + " textarea").bind("keyup", function(event){
		var h=$(this);
		h.height(30).height(h[0].scrollHeight - 4);//where 60 is minimum height of textarea
	});
	
	if(value.type=="file" || value.type == "document"){
		setTimeout(function(){
			var fileUplodaerObjectId = mw3.getChildObjectId(objectId, "file");
			var fileUploaderFaceHelper = mw3.getFaceHelper(fileUplodaerObjectId);
			fileUploaderFaceHelper.addFileChangeListener(function(file){
				
				var existingText = $("#post_" + objectId).val();
				
				if(existingText==null || existingText.trim().length==0){
					//value.title = file.filename;
					//var titleObjId = mw3.getChildObjectId(objectId, "title");
					$("#post_" + objectId).val(file.filename);
				}
			});
		}, 1000);
	}else if(value.type == 'comment' || value.type == 'remoteConf'){
		var session = mw3.getAutowiredObject("org.uengine.codi.mw3.model.Session");		
		if( session.ux != 'phone' &&  session.ux != 'pad' ){
			$("#post_" + this.objectId).bind("keydown", {instanceFirst: this.instanceFirst}, function(event){
				if (event.keyCode == 13 && !event.shiftKey) {
					event.preventDefault();
					if(  !event.data.instanceFirst ){
						window.event.cancelBubble = true;
						window.event.returnValue = false;
					}
				}
			});
			
			$("#post_" + this.objectId).bind("keyup", {objectId: this.objectId}, function(event){
				mw3.getFaceHelper(event.data.objectId).press();
			});		
		}
	}
	

	
	this.sending = false;
	// process
	this.processFirst = true;	
	// user names
	this.userAddCommands = {};
	// dates
	this.initialize();
	this.dateFirst = true;
	this.dueDate = null;
};

org_uengine_codi_mw3_model_IWorkItem_edit.prototype = {
	getValue : function(){
		var object = mw3.objects[this.objectId];
		
		if(this.commandTrigger!=null){
			object.title = this.commandTrigger;
			object.activityAppAlias = this.commandActivityAppAlias;
			
			for(var i=0; i<this.commandParameters.length; i++){
				var parameterValue = this.commandParameters[i];
				parameterValue.valueObject = mw3.getObject(parameterValue.objectId);
			}
			
			object.parameters = this.commandParameters;
		}else{
			var text = $("#post_" + this.objectId).val();
			if(text)
				object.title = text;
		}
		
		if( this.commandActivityAppAlias != null){
			object.activityAppAlias = this.commandActivityAppAlias;
		}
		var initialFollowers = [];
		for(var idx in this.userAddCommands){
			if(this.userAddCommands[idx])
				initialFollowers[initialFollowers.length] = idx;
		}
		
		if(initialFollowers.length>0){
			object.initialFollowers = initialFollowers;
		}
		if( this.dueDate != null){
			object.dueDate = this.dueDate; 
		}
		
		return object;		
	},
	destroy : function(){
		$("#post_" + this.objectId).unbind();
	},
	
	showStatus : function(message){
		if(window.console) console.log(message);
	},
	
	sendComment : function(){
		var value = mw3.getObject(this.objectId);
		
		this.type = value.type;
		
		if(!this.sending){			
			this.sending = true;

			if(value.type=='comment' && value.metaworksContext.when == 'new' && value.instId){
				if(value.title && value.title.trim().length){
					var newComment = JSON.parse(JSON.stringify(value));
					newComment.metaworksContext = {};
					newComment.metaworksContext.when = 'view';
					newComment.__objectId = null;
					newComment.startDate = new Date();
					
					if( value && value.metaworksContext && value.metaworksContext.how != null){
						newComment.metaworksContext.how = value.metaworksContext.how;
					}
					
					var instanceViewThreadPanel = {
						__className : 'org.uengine.codi.mw3.model.InstanceViewThreadPanel',
						instanceId : value.instId
					};
					
	
					var toAppend = mw3.locateObject(
						{
							__className	:'org.metaworks.ToAppend',
								target	: newComment,
								parent	: instanceViewThreadPanel,
								match   : true
						}, null, 'body'
					);
					
					//may problematic. TODO: 'toAppend.target' should point to the newly acquired object with the __objectId exists.
					var newCommentObjectId = toAppend.targetObjectId + 1;
					
					mw3.onLoadFaceHelperScript();
		
					newComment = mw3.objects[newCommentObjectId];
					newComment.metaworksContext.when = 'new';
		
					try{
						newComment.add();
					}catch(e){
						//handle when fail
					}
		
					value.title = "";
					mw3.setObject(value.__objectId, value);
					
					$("#post_" + this.objectId).focus();
				}
			}else{
				if(value.title != null)
					value.add();
			}
		}
		
		var thisFaceHelper = this;
		setTimeout(function(){thisFaceHelper.sending=false;}, 1000);
	}
};

org_uengine_codi_mw3_model_IWorkItem_edit.prototype.showCommandForm = function(processDef){
	var thisFaceHelper = this;

	var text = $("#post_" + this.objectId).val();
	var fullText = text;
	
	if(fullText==null  ||  fullText.trim().length == 0)
		fullText = processDef.cmTrgr;
	
	if(':' == fullText.charAt(fullText.length - 1))
		fullText = fullText.substring(0, fullText.length-1);	
	
	var divId = "#commandDiv_" + this.objectId;

	$(divId).html("<b>" + fullText + ": </b>");
	$(divId).keydown(function(e){
		if(e.keyCode==13){
			thisFaceHelper.sendComment();	
		}
	
	});			

	var entryAndPhrases = processDef.cmPhrase.split("$");
	
	this.commandTrigger=processDef.cmPhrase;
	this.commandActivityAppAlias=processDef.defId;
	
	mw3.setWhen('edit');
	
	this.commandParameters = [];//{__className: 'org.uengine.codi.mw3.model.ParameterValue[]'};

	var parameterIndex = 0;

	if(entryAndPhrases.length>1)
	for(var entryIndex=1; entryIndex<entryAndPhrases.length; entryIndex++){
		var entry = entryAndPhrases[entryIndex];
		var entryNameAndClassName = entry.split("<");
		var entryName = entryNameAndClassName[0];
		var classNameAndConnector = entryNameAndClassName[1].split(">");
		var className = classNameAndConnector[0];
		var connector = classNameAndConnector[1];
		
	//        			alert(entryName);
	//        			alert(className);
		
		
		var object = new MetaworksObject({
	        __className : className//,
	        //metaworksContext: {when:'edit'}
	    }, divId);
		
	
		var valueObject = 0+(object.object ? object.object.__objectId : object.__objectId);
		
		this.commandParameters[parameterIndex] = {
				__className: 'org.uengine.codi.mw3.model.ParameterValue',
				variableName: entryName,
				objectId: valueObject  
		};
		
		this.commandParameters[parameterIndex]['objectId'] = 0+(object.object ? object.object.__objectId : object.__objectId);
		
		parameterIndex++;
		
		$(divId).append(connector);
	}
	
	mw3.onLoadFaceHelperScript();
	mw3.setWhen('view');
	
};



org_uengine_codi_mw3_model_IWorkItem_edit.prototype.press = function(){
	var e = window.event;
	
	// enter 이고 인스턴스를 발행해야 하는 경우가 아니라면
	if (e.keyCode == 13 && !e.shiftKey) {
		this.sendComment();
    }else{
    	// 입력된 값 parsing
    	var text = $("#post_" + this.objectId).val();    	
    	var tokens  = text.split(" ");
    	
    	if(tokens.length > 1)
    		text = tokens[tokens.length-1];

    	var recommendDivId = "#commandRecommendDiv_" + this.objectId;
    	var processDivId 		= "commandProcessDiv_" + this.objectId;
    	var followerDivId 	= "commandFollowerDiv_" + this.objectId;
    	var dateDivId 			= "commandDateDiv_" + this.objectId;    	    	
    	var recommendFirst = true;
    	
    	$(recommendDivId).empty();
    	
    	//////// assists about process initiation /////////    	
    	var processMapPanel = mw3.getAutowiredObject("org.uengine.codi.mw3.model.ProcessMapPanel");
    	
    	if(processMapPanel != null && typeof processMapPanel != 'undefined'){
    		var processMap = processMapPanel.processMapList;
    		
    		if(text && text.length>0 && processMap){
    			for(var i=0; i<processMap.processMapList.length; i++){
    				var commandTrigger = processMap.processMapList[i].cmTrgr + ':';
    				var commandPhrase = processMap.processMapList[i].cmPhrase;
    				
    				var processName = processMap.processMapList[i].name;
    				if( processName == null || processName == "" ){
    					break;
    				}
    				
    				// 프로세스 명이 일부 같을시에 하단의 프로세스맵을 움직이게 가이드
    				if(processName.indexOf(text)==0){
    					var theAppDiv = "#objDiv_" + processMap.processMapList[i].__objectId;
    					
    					if(!theAppDiv['__inEffect']){
    						theAppDiv['__inEffect'] = true;
    						$(theAppDiv).effect("bounce", {times: 3}, 300);
    					}	
    				}
    				
    				// 프로세스명 보다 트리거명이 우선 작용
    				if(commandTrigger.length > 1 && commandTrigger.indexOf(text)==0){	    			
    					// 트리거 명과 완전희 같으면
    					if(commandTrigger == text){
    						this.showCommandForm(processMap.processMapList[i]);
    						
    						break;	    				
    					}else{
    						if(recommendFirst){
    							var innerHtmlStr = 	"<div>";
    							innerHtmlStr		+=	" " + mw3.localize('$RecommendedCommand') + ": \"<b>" ;
    							innerHtmlStr		+=	"<span id=\"trigerDivSpan\">" + commandTrigger + "</span>" ;
    							innerHtmlStr		+=	"</b>\"";
    							innerHtmlStr		+=	"</div>";
    							
    							$(recommendDivId).append(innerHtmlStr);
    							recommendFirst = false;
    						}else{
    							$("#trigerDivSpan").append(", \"<b>" + commandTrigger + "</b>\"");
    						}
    					}
    					
    				}else{
    					// 프로세스명을 정확히 입력시 프로세스 추천 목록이 보임
    					if(processName == text){
    						if(this.processFirst){
    							var innerHtmlStr = 	"<div id=\"" + processDivId + "\" >";
    							innerHtmlStr		+=	" " + mw3.localize('$AddWithProcess') + ": \"<b>" ;
    							innerHtmlStr		+=	"<span id=\"processDivSpan\">" + processName + "</span>" ;
    							innerHtmlStr		+=	"</b>\"";
    							innerHtmlStr		+=	" | <a href=\"#\" onClick=\"mw3.getFaceHelper('"+this.objectId+"').removeDiv('"+processDivId+"')\" style=\"cursor:pointer;\">" + mw3.localize("$NO") + "</a> ";
    							innerHtmlStr		+=	"<br>";
    							innerHtmlStr		+=	"</div>";
    							
    							this.commandActivityAppAlias = processMap.processMapList[i].defId;
    							
    							$(recommendDivId).append(innerHtmlStr);
    							this.processFirst = false;
    						}else{
    							$("#processDivSpan").html(processName);
    							this.commandActivityAppAlias = processMap.processMapList[i].defId;	    				
    						}	    				
    					}
    				}
    			}
    		}
    	}
		
    	if(  this.instanceFirst ){
			//////// assists about dates ////////
    		/*
			if(text && text.length>1){
				date = Date.parseHangul(text) || Date.parse(text);
		          if (date !== null && this.dateFirst ) {
		        	  var innerHtmlStr = 	"<div id=\"" + dateDivId + "\" >";
		        	  innerHtmlStr		+=	" " + mw3.localize('$AddDate') + ": \"<b>" ;
		        	  innerHtmlStr		+=	"<span id=\"dateDivSpan\">" + date.toString(Date.CultureInfo.formatPatterns.fullDateTime) + "</span>" ;
		        	  innerHtmlStr		+=	"</b>\"";
		        	  innerHtmlStr		+=	" | <a href=\"#\" onClick=\"mw3.getFaceHelper('"+this.objectId+"').removeDiv('"+dateDivId+"')\" style=\"cursor:pointer;\">" + mw3.localize("$NO") + "</a> ";
		        	  innerHtmlStr		+=	"<br>";
		        	  innerHtmlStr		+=	"</div>";
		        	  
		        	  $(recommendDivId).append(innerHtmlStr);
		        	  this.dateFirst = false;
		        	  this.dueDate = date;
		          }else if(date !== null && !this.dateFirst ){
		        	  $("#dateDivSpan").html(date.toString(Date.CultureInfo.formatPatterns.fullDateTime));
		        	  this.dueDate = date;
		          }
			}
			*/
    	}
    	
		/////// assists about user names //////
		if(!this.contacts){
			var contactPespective = mw3.getAutowiredObject('org.uengine.codi.mw3.model.ContactPerspective');
			if( contactPespective ){
				if(contactPespective.more)
					this.contacts = contactPespective.child;
				else
					this.contacts = contactPespective.loadAllContact();
			}
		}
		
		if(text && text.length>0 && this.contacts){
			var exisingFollowers = {};
			var value = mw3.objects[this.objectId];
			
			if(value.instId){
				var followers = mw3.getAutowiredObject('org.uengine.codi.mw3.model.Followers@instance_' + value.instId);
				if( followers != null && followers.followers != null){ 
					for(var i=0; i<followers.followers.length; i++){
						var follower = followers.followers[i];
						
						if(follower.assigntype == 0)
							exisingFollowers[follower.endpoint] = true;
					}
				}
			}
			
			for(var i=0; i<this.contacts.length; i++){
				var contact = this.contacts[i];
				
				if(contact.friend && contact.friend.userId && contact.friend.name && text.indexOf(contact.friend.name) > -1 && !this.userAddCommands[contact.friend.userId] && !exisingFollowers[contact.friend.userId]){
					var innerHtmlStr = 	"<div id=\"" + followerDivId + '_' + contact.friend.userId + "\" >";
					innerHtmlStr		+=	"" + mw3.localize('$AddUserAsFollower') + ": <b>" + contact.friend.name + "</b>";
					innerHtmlStr		+=	" | <a href=\"#\" onClick=\"mw3.getFaceHelper('" + this.objectId + "').hateFollower('" + followerDivId + "','" + contact.friend.userId + "');\" style=\"cursor:pointer;\">" + mw3.localize("$NO") + "</a> ";
					innerHtmlStr		+=	"</div>";
					
					$('#' + followerDivId).append(innerHtmlStr);
					
    				//$(followerDivId).append("" + mw3.localize('$AddUserAsFollower') + ": \"<b>" + contact.friend.name + "</b>\"<br>");
					
    				this.userAddCommands[contact.friend.userId] = contact.friend.userId;
				}
			}
			
			
		}
		
		//////// assists about url ////////
		
		

    }
};
org_uengine_codi_mw3_model_IWorkItem_edit.prototype.removeDiv = function(divId){
	$("#" + divId ).remove();
	if( divId.indexOf("commandDateDiv_") == 0  ){
		this.dateFirst = true;
		this.dueDate = null;
	}else if( divId.indexOf("commandProcessDiv_") == 0  ){
		this.processFirst = true;
		this.commandActivityAppAlias = null;
	}
};
org_uengine_codi_mw3_model_IWorkItem_edit.prototype.hateFollower = function(followerDivId, userId){
	
	var id = userId.replace("@", "\\@");
	id = id.replace(".", "\\.");
	
	$('#'+followerDivId + '_' + id).remove();
	
	this.userAddCommands[userId] = null;
};

/**
 * datejs.js using method
 * @url       http://www.firejune.com/1343
 * @usage:    Date.parseHangul(value); 
 */
org_uengine_codi_mw3_model_IWorkItem_edit.prototype.initialize = function(){
	this.datePatterns = {
        '-3':/^그끄제|그끄저께/i,'-2':/^그제|그저께/i,'+2':/^모레/i,'+3':/^글피/i,'+4':/^그글피/i,
        '1':/^하루|일일/i,'2':/^이틀|이일/i,'3':/^사흘|삼일/i,'4':/^나흘|사일/i,'5':/^닷세|오일/i,
        '6':/^엿세|육일/i,'7':/^이레|칠일/i,'8':/^여드레|팔일/i,'9':/^아흐레|구일/i,
        '10':/^열흘|십일/i,'11':/^열하루|열하레|십일일/i,'12':/^열이틀|십이일/i,
        '13':/^열사흘|열산날|십삼일/i,'14':/^열나흘|열낫날|십사일/i,'15':/^보름|열닷세|십오일/i,
        '16':/^열엿세|십육일/i,'17':/^열이레|십칠일/i,'18':/^열여드레|십팔일/i,
        '19':/^열아흐레|십구일/i,'20':/^스무날|이십일/i,'21':/^스무하루|스무하레|이십일일/i,
        '22':/^스무이틀|이십이일/i,'23':/^스무사흘|이십삼일/i,'24':/^스무나흘|스무낫날|이십사일/i,
        '25':/^스무닷세|이십오일/i,'26':/^스무엿세|이십육일/i,'27':/^스무이레|이십칠일/i,
        '28':/^스무여드레|이십팔일/i,'29':/^스무아흐레|이십구일/i,'30':/^설흔날|서른날|삼십일/i,
        '31':/^서른하루|삼십일일/i
      };

      this.regex = {
        time: /[0-9]+시|[0-9]+분|[0-9]+초|오후|저녁|밤/g,
        date: /[0-9]+년|[0-9]+월|[0-9]+일/g,
        years: /[0-9]+년/i,
        months: /[0-9]+월/i,
        days: /[0-9]+일/i,
        second: /^[0-9]+초/i,
        minute: /^[0-9]+분/i,
        hour: /^[0-9]+시(간)?/i,
        pm: /^오후|저녁|밤/i,
        am: /^오전|아침/i,
        after: /(이|\s)?후/i,
        befroe: /(이|\s)?전/i
      };

	  //Date.parseHangul = this.analyze.bind(this);	      
};

org_uengine_codi_mw3_model_IWorkItem_edit.prototype.analyze = function(value){
    if (!value) return null;

    var time = this.searchTime(value);
    var date = this.searchDate(value);

    if (time) {
      if (!date) {
        date = new Date();
        date = new Date(date.getFullYear(), date.getMonth() + 1, date.getDate());
      }
      date = new Date(date.getTime() + time);
    }

    return isNaN(date) ? null : date;
};
org_uengine_codi_mw3_model_IWorkItem_edit.prototype.searchTime = function(str){
	 var pattern = str.match(this.regex.time), times = 0;
     if (pattern && pattern.length > 1 || 
       (str.match(this.regex.am) || str.match(this.regex.pm)) && str.match(this.regex.time)) {
       for (var i = 0; i < pattern.length; i++) {
         if (pattern[i].match(this.regex.pm)) times += 43200000;
         if (pattern[i].match(this.regex.hour)) times += parseInt(pattern[i]) * 3600000;
         if (pattern[i].match(this.regex.minute)) times += parseInt(pattern[i]) * 60000;
         if (pattern[i].match(this.regex.second)) times += parseInt(pattern[i]) * 1000;
       }

       return times || null;
     } else return null;
};
org_uengine_codi_mw3_model_IWorkItem_edit.prototype.searchDate = function(str){
	var pattern = str.match(this.regex.date);
    if (pattern && pattern.length == 3)
      for (var i = 0; i < pattern.length; i++)
        pattern[i] = parseInt(pattern[i]);
    else {
      pattern = [];
      var years = str.match(this.regex.years),
      months = str.match(this.regex.months),
      days = str.match(this.regex.days);
      
      pattern.push(years ? parseInt(years[0]) : new Date().getFullYear());
      pattern.push(months ? parseInt(months[0]) : new Date().getMonth() + 1);
      days = days ? parseInt(days[0]) : null;

      if (!days)
        for (var regex in this.datePatterns)
          if (str.match(this.datePatterns[regex])) days = regex;

      if (!days) return null;
      else if (str.match(this.regex.befroe) || str.match(this.regex.after)) {
        if (str.match(this.regex.befroe)) days = - days;
        pattern.push(new Date().getDate() + days  * 1);
      } else {
        if (typeof days == 'string' && days.match(/\+|\-/))
          days = new Date().getDate() + days * 1;
        pattern.push(days);
      }
    }

    return new Date(pattern[0], pattern[1] - 1, pattern[2]);
};
