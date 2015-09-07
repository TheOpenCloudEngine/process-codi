var org_uengine_codi_mw3_model_IWorkItem = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	this.object = mw3.objects[this.objectId];
	
	if(this.object == null)
		return true;	
	
	this.objectDiv.css('position', 'relative');

	var container = this.objectDiv.find('.formcontexttitle span').eq(0);
	var doc = container.html();
	
	if(doc){
		// Date: '13.06.21 
		// Modifier : jwlee@uengine.org
		// Description : 스크립트 정규 표현식에서 '&'를 '&amp;'로 수정
		var regURL = new RegExp("(http|https|ftp|telnet|news|irc)://([-/.a-zA-Z0-9_~#%$?=&amp;:200-377()]+)","gi");	
		
		try{
			container.html(doc.replace(regURL,"<a href='$1://$2' target='_blank' style='text-decoration:underline'>$1://$2</a>"));
		}catch(e){
			if(window.console) console.log(e);
		}
	}
	
	var workItem = mw3.objects[objectId];
	this.type = workItem.type;
	var contentLoad = false;
		
	if(workItem.type == 'process'){
		if(!workItem.contentLoaded && workItem.tool != 'formApprovalHandler' && (workItem.status == 'NEW' || workItem.status == 'CONFIRMED' || workItem.status == 'DRAFT')){
			contentLoad = true;
		}
		
		this.objectDiv.find('.wih_title').css({cursor: 'pointer', color: '#0000FF', 'text-decoration': 'underline'}).bind('click', {objectId: this.objectId}, function(event, ui){
			mw3.call(event.data.objectId, 'detail');
		});
		
	}else if(workItem.type == 'memo' && workItem.extFile!=null && !workItem.contentLoaded){
		contentLoad = true;		
	}else if(workItem.type == 'email' && !workItem.contentLoaded){
		contentLoad = true;
	}else if(workItem.type == 'src' && workItem.extFile!=null && !workItem.contentLoaded){
		contentLoad = true;
	}else if(workItem.type == 'remoteConf'){
		 
		 if(!workItem.contentLoaded){
		 	contentLoad = true;
	 	} 
	 	
	 	if(workItem.status =='FEED'){
		 	contentLoad = false;
		}else if(workItem.status =='Waited'){
			var workItemObjectId = this.objectId;
	
			var jobId = setInterval(function(){
			var workItemsss = mw3.objects[workItemObjectId];
				if( workItemsss.status !='Completed'){
					mw3.call(workItemObjectId, 'recodingCheck');
					
				}else{
					if(jobId != ''){ 
						clearInterval(jobId);
						return;
					}
				}
			
			}, 10000);
				
		}
	}
	
	if(contentLoad)
		mw3.call(this.objectId, 'loadContents');
	else{
		if(!this.object.more)
			this.objectDiv.trigger('loaded.workitem_' + workItem.taskId);
	}

	if(!workItem.childWorkItemLoaded){
		mw3.call(this.objectId, 'loadChildWorkItems');
	}
};

org_uengine_codi_mw3_model_IWorkItem.prototype = {
	openFormApprovalHandler : function(){
		var object = mw3.getObject(this.objectId);
		var location = window.location;
				
		var url = location.origin.replace('8080','7080') + '/uengine-web/processparticipant/worklist/workitemHandler.jsp?taskId='+object.taskId+'&instanceId='+object.instId+'&tracingTag='+object.trcTag;
		
		window.open(url);
	},
	
	
	conferenceWorkItemJoin : function(){
		
		var workItemObjectId = this.objectId;
	 	var workItem = mw3.objects[workItemObjectId];		
		
		mw3.call(workItemObjectId, 'join');
		
		if(workItem.ext5 == null){	
		 	var job = setInterval(function(){
		 	var workItemsss = mw3.objects[workItemObjectId];
				console.log("시작"+job);
				if(workItemsss.ext7 == null || workItemsss.ext7 =='0'){
					
					mw3.call(workItemObjectId, 'checkJoined');
					console.log("sss"+job);
				}else{
					console.log("111"+job);
					if(job != ''){ 
						clearInterval(job);
						return;
					}
				}
			
			}, 10000);
		}
			
	}
};
	

