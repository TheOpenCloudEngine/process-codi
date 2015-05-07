// Load the SDK's source Asynchronously
// Note that the debug version is being actively developed and might 
// contain some type checks that are overly strict. 
// Please report such bugs using the bugs tool.
(function(d, debug){
	var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	if (d.getElementById(id)) {return;}
	js = d.createElement('script'); js.id = id; js.async = true;
	js.src = "//connect.facebook.net/ko_KR/all" + (debug ? "/debug" : "") + ".js";
	ref.parentNode.insertBefore(js, ref);
}(document, /*debug*/ false));

var org_uengine_codi_mw3_model_InstanceViewPublic = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
};

org_uengine_codi_mw3_model_InstanceViewPublic.prototype = {
	loaded : function(){
		var objectId = this.objectId;
		
		window.fbAsyncInit = function() {
			try{
				// init the FB JS SDK
				FB.init({
					appId      : facebookAppId, // App ID from the App Dashboard
					//channelUrl : '//localhost:8080/uengine-web/InstanceView.html', // Channel File for x-domain communication
					status     : true, // check the login status upon init?
					cookie     : true, // set sessions cookies to allow your server to access the session?
					xfbml      : true  // parse XFBML tags on this page?
				});
				
				FB.getLoginStatus(function(response) {
					if ('connected' == response.status){
					    var uid = response.authResponse.userID;
						
						FB.api('/' + uid, function(response) {
							mw3.getFaceHelper(objectId).facebookSSO(response);
						});
					}else if('not_authorized' == response.status){
						mw3.getFaceHelper(objectId).loginFacebook();
					}
				});
						
			}catch(e){}
		};
	},
	facebookSSO : function(response){
		var object = mw3.objects[this.objectId];
		
		if(response.email){
			var login = {
					__className : 'org.uengine.codi.mw3.Login',
					facebookSSO : true,
					userId : response.email
			};
			
			mw3.locateObject(login, login.__className);

			var isAuth = login.checkAuthSocial();
			if(!isAuth){
				if (console && console.log) console.log(response);
				
				var employee = {
					__className : 'org.uengine.codi.mw3.model.Employee',
					metaworksContext : {when : 'new'},
					empCode : response.email,
					empName : response.name,
					email   : response.email,
					facebookId : response.id,
					locale  : response.locale.substring(0,2)								
				};
				
				mw3.locateObject(employee, employee.className);
				
				employee.saveMe();							
			}
			
			object.session = login.makeSession();
			object.load();
		}
	},
	loginFacebook : function(){
		var objectId = this.objectId;		

		if(typeof FB == 'object'){
			FB.login(function(response) {
				if (response.status === 'connected') {
				    var uid = response.authResponse.userID;				
					FB.api('/' + uid, function(response) {											
						mw3.getFaceHelper(objectId).facebookSSO(response);
					});
				}else{
					$('#method_facebook_' + objectId).show();
				}
				
			}, {scope:'email,user_checkins,publish_stream,user_likes,export_stream'});	
		};
	}		
};

