var org_uengine_codi_mw3_model_Session = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;
	
	var session = mw3.objects[objectId];


	// session heartbeat is disabled.

	//var jobId = setInterval(function(){
	//	if(!mw3.objects[session.__objectId]){
	//		clearInterval(jobId);
	//		return;
	//	}
	//
	//	var messageFromServer = session.heartbeat();
	//	if(messageFromServer){
	//		mw3.locateObject(messageFromServer, null, "body");
	//	}
	//
	//}, 10000);

	//this.jobId = jobId;

};

org_uengine_codi_mw3_model_Session.prototype = {
	destroy : function(){
		clearInterval(this.jobId);
	},
	
	fire : function(){
		mw3.call(this.objectId, 'logout');
		
		alert('Your Session is disconnected since you have connected by another browser.');
	}
};

mw3.fn.getSession = function(){
	var session = mw3.getAutowiredObject('org.uengine.codi.mw3.model.Session');
	
	if(session == null || typeof session == 'undefined')
		return null;
	
	return session;
};

mw3.fn.getEmployee = function(){
	var session = mw3.fn.getSession();
	
	if(session == null )
		return null;
	
	if(session.employee == null || typeof session.employee == 'undefined')
		return null;
	
	return session.employee;
};

mw3.fn.getLoginUserId = function(){
	var employee = mw3.fn.getEmployee();
	
	if(employee == null)
		return null;
	
	return employee.empCode;
};

mw3.fn.getPreferUx = function(){
	var employee = mw3.fn.getEmployee();
		
	if(employee == null)
		return null;
	
	return employee.preferUX;
};

/*
function loadTest(){
	for(var i=0; i<10; i++){
		var newInst = window.open(window.location, 'popup'+i, "");
		
		var jobId = setInterval(function(){
			try{newInst.test(); clearInterval(jobId);}catch(e){}
		}, 2000)
	}
}

function test(){
	mw3.startTest('testScenario');
}
*/