var org_uengine_codi_mw3_model_AddContactPanel = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;

	var friendsObjId = mw3.getChildObjectId(this.objectId, 'friends');
	var faceHelper = this;

	if(typeof FB == 'object'){
		FB.init({
		      appId      : '201078716741890', // App ID from the App Dashboard
		      //channelUrl : '//localhost:8080/uengine-web/index.html', // Channel File for x-domain communication
		      status     : true, // check the login status upon init?
		      cookie     : true, // set sessions cookies to allow your server to access the session?
		      xfbml      : true  // parse XFBML tags on this page?
		    });
		
		FB.getLoginStatus(function(response) { //is facebook logged?
			if (response.status === 'connected') { 
				faceHelper.readComment(friendsObjId);
			}else{
				FB.login(function(response) {
					if (response.status === 'connected') {
						faceHelper.readComment(friendsObjId);
					}else{
						$('#objDiv_' + objectId).closest('.mw3_popup').remove();

						mw3.removeObject(objectId);
					}

				}, {scope:'email,user_checkins,publish_stream,user_likes,export_stream'});
			}
		});
	}
};

org_uengine_codi_mw3_model_AddContactPanel.prototype = {
		readComment : function(objectId){	

			var friends = [];

			FB.api(
					{	method: 'fql.query',
						query: 'SELECT uid, first_name,email, pic FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me())',
				}, 
				
				function(response) {
					if(!response || response.error){
						alert('Error!');

						return;
					}

					for(var i in response){
						friends[i] = {
								userId : response[i].uid,
								name : response[i].first_name,
								friendEmail : response[i].email,
								network: 'fb',
								__className : "org.uengine.codi.mw3.model.IUser",
						};

					}
					
					friends.metaworksContext = {when:'addContact'};
					mw3.setObject(objectId, friends);
					mw3.onLoadFaceHelperScript();				  
				}
			);	
		}
		
};