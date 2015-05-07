var org_uengine_codi_mw3_model_FacebookFeedback = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
	var facebookFeedback = mw3.objects[objectId];
	var postId = facebookFeedback.postId;
	
	var query = FB.Data.query( "SELECT text, time, fromid FROM comment WHERE post_id = '"+postId+"'" );
	
	
	var instaceViewThreadPanel = mw3.getAutowiredObject("org.uengine.codi.mw3.model.InstanceViewThreadPanel");
	
	query.wait(function(data){
		for( var i=data.length-1; i>-1; i-- ) {
			for(var threadIdx in instaceViewThreadPanel.thread){
				
				var thread = instaceViewThreadPanel.thread[threadIdx];
				
				var createdTime = new Date("1970/01/01");
				createdTime.setSeconds(createdTime.getSeconds() + data[i].time);
				
				var nextThread = (instaceViewThreadPanel.thread.length < threadIdx + 2) ? null : instaceViewThreadPanel.thread[threadIdx + 1];
				if(nextThread==null || (thread.startedDate < createdTime && nextThread.startedDate > createdTime)){
					
					mw3.locateObject({
						__className: "org.metaworks.ToNext",
						target: {
							__className: "org.uengine.codi.mw3.model.CommentWorkItem",
							title: data[i].text,
							startDate: createdTime,
							writer: {
								__className: "org.uengine.codi.mw3.model.IUser",
								userId: data[i].fromid,
								network: 'fb'
							} ,
							type: 'comment'
						},
							previous: thread
						
					}, null, 'body');
					
					break;
				}
				
			}
		}
	});

}