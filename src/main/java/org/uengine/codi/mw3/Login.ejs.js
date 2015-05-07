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

var org_uengine_codi_mw3_Login = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.divId = mw3._getObjectDivId(this.objectId);
	
	this.object = mw3.objects[this.objectId];
	
	if(this.object == null)
		return true;	

	var lastVisitPage = getCookie("codi.lastVisit");
	if(lastVisitPage) 
		this.object.lastVisitPage = lastVisitPage;
		
	var lastVisitValue = getCookie("codi.lastVisitValue");
	if(lastVisitValue)
		this.object.lastVisitValue = lastVisitValue;

	if(this.object && this.object.metaworksContext && this.object.metaworksContext.where == 'index')
		return true;

	if(this.object && this.object.metaworksContext && this.object.metaworksContext.where == 'popup'){
		var popupObj = $("#objDiv_" + this.objectId).closest('.target_popup,.target_stick');

		var bodyHeight = $('body').height();
		var bodyWidth = $('body').width();

		
		popupObj.css({left:(bodyWidth-popupObj.width())/2 + 'px', top: (bodyHeight-popupObj.height())/2-50 + 'px'});
	}

	if(this.object && this.object.metaworksContext && this.object.metaworksContext.how == 'login'){
		var id = getCookie("codi.id");
		
		var input_email = mw3.getInputElement(objectId, "email");
		if(input_email)
			input_email.value = id;			
	
	
		var rememberMe = getCookie("codi.rememberMe");
		if(rememberMe!=null && rememberMe){
			var password = getCookie("codi.password");
			
			var input_password = mw3.getInputElement(objectId, "password");
			
			if(input_password)
				input_password.value = password;
		}
	
		var input_rememberMe = mw3.getInputElement(objectId, "rememberMe");
		if(input_rememberMe)
			input_rememberMe.checked = true;
	
			mw3.getInputElement(objectId, "email").focus();
	 	
		$('#' + this.divId).bind('keydown', function(event){
			mw3.getFaceHelper(objectId).keydown(event);
		});			
	
	
		var login = mw3.getObjectFromUI(this.objectId);
			var facebookSSO = getCookie("codi.facebookSSO");
			
			if(login.email && login.password && login.rememberMe){
				login.login();
			}else{
				var objectId = this.objectId;
				
				if(typeof FB == 'object'){
					$('#method_facebook_' + this.objectId).show();
				}else{
					window.fbAsyncInit = function() {
					    // init the FB JS SDK
					    FB.init({
					      appId      : '119159701538006', // App ID from the App Dashboard
					      //channelUrl : '//localhost:8080/uengine-web/index.html', // Channel File for x-domain communication
					      status     : true, // check the login status upon init?
					      cookie     : true, // set sessions cookies to allow your server to access the session?
					      xfbml      : true  // parse XFBML tags on this page?
					    });
		
					    $('#method_facebook_' + objectId).show();
					    
					    // Additional initialization code such as adding Event Listeners goes here
						// facebook login status
						FB.getLoginStatus(function(response) {
							if (response.status == 'connected'){
							    var uid = response.authResponse.userID;
								
								FB.api('/' + uid, function(response) {
									if(login.email && login.rememberMe && facebookSSO)
										login.getFaceHelper().facebookSSO(response);
								});
							}else if('not_authorized' == response.status){
								login.getFaceHelper().loginFacebook();
							}	
		
						}, true);
					};
				}
			}
	}
};

org_uengine_codi_mw3_Login.prototype = {
	loaded : function(){
		
	},
	keydown : function(e){
		if(e.keyCode == 13){
			e.returnValue = false;
			if( this.object && this.object.status == 'forgotpassword'){
				mw3.call(this.objectId, 'forgotPassword');
			}else if(this.object && this.object.status == 'signup'){
				mw3.call(this.objectId, 'signUp');
			}else{
				mw3.call(this.objectId, 'login');
			}
		}
	},
	getValue : function(){
		var login = mw3.getObjectFromUI(this.objectId);
		
		var lastVisitPage = getCookie("codi.lastVisit");
		if(lastVisitPage) 
			login.lastVisitPage = lastVisitPage;

		return login;
	},
	startLoading : function(){
		$('body').prepend('<div id=\"mw3_progress\" style=\"position:absolute; z-index:9999999; top:50%; left:50%; margin-top:-12px; margin-left:-12px;\"><div id=\"info_div_box\"><img src=\"dwr/metaworks/images/circleloading.gif\"></div></div>');
	},
	endLoading : function(){	
		setTimeout(function(){
			$('#mw3_progress').remove();
		}, 100);		
	},
	destroy : function(){
		$('#' + this.divId).unbind('keydown');
		
		setTimeout(function(){
			$('#mw3_progress').remove();
		}, 100);		
		
	},
	showStatus : function(message){
		if('login DONE.' == message){
			if (this.object.rememberMe){
				setCookie("codi.id", this.object.email, 10, "/", "", "");
				setCookie("codi.password", this.object.password, 10, "/", "", "");			
				setCookie("codi.rememberMe", true, 10, "/", "", "");
				setCookie("codi.facebookSSO", true, 10, "/", "", "");
				
			}else{
				delCookie("codi.id", "/","");
				delCookie("codi.password", "/","");
				delCookie("codi.rememberMe", "/","");
				delCookie("codi.facebookSSO", "/","");
			}
		}
	},
	facebookSSO : function(response){
		var objectId = this.objectId;
		
		if(response.email){
			var object = mw3.getAutowiredObject('org.uengine.codi.mw3.Login@login');
			
			mw3.getInputElement(object.__objectId, "email").value = response.email;
			
			var isAuth = object.checkAuthSocial();
			
			
			if(!isAuth){
				var employee = {
					__className : 'org.uengine.codi.mw3.model.Employee',
					metaworksContext : {when : 'new'},
					empName : response.name,						
					email   : response.email,
					facebookId   : response.id
				};
				mw3.locateObject(employee, employee.className);
				
				employee.facebookSSO();
			}
		}
		
	},
	loginFacebook : function(){		
		if(typeof FB != 'object'){
			alert('not loaded facebook api');
		}
		
		var objectId = this.objectId;
		FB.login(function(response) {
			if (response.status === 'connected') {
			    var uid = response.authResponse.userID;				
				FB.api('/' + uid, function(response) {
					mw3.getFaceHelper(objectId).facebookSSO(response);
				});
			}
			
		}, {scope:'email,user_checkins,publish_stream,user_likes,export_stream'});	
	}
};