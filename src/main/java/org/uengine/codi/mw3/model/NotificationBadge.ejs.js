var org_uengine_codi_mw3_model_NotificationBadge = function(objectId, className){
	this.objectId = objectId;
	this.className = className;	
	this.divId = 'objDiv_' + this.objectId;
	
	this.object = mw3.objects[this.objectId];
	
	if(this.object.loader)
		mw3.call(this.objectId, 'refresh');
	else{
		$('.badgeRed').show( 'pulsate' ,  500 );	
		
		var badge = mw3.objects[objectId];
		
		if(badge && badge.newItemCount == -1)
			mw3.objects[objectId].refresh();
		
		window.document.title = (badge.newItemCount > 0 ? "("+ badge.newItemCount + ")":"") + " Codi"; 
	}
};

org_uengine_codi_mw3_model_NotificationBadge.prototype = {
	callNoti : function(notify){
		if(typeof notify != 'undefined' && notify){
			if(!mw3.windowFocus){
				var prevTitle = document.title;
				var session = mw3.getAutowiredObject('org.uengine.codi.mw3.model.Session');
				
				if(window.trayAlert)
					window.trayAlert = clearInterval(window.trayAlert);
				
				window.trayAlert = setInterval(function(){
					if(document.title == prevTitle)
						document.title = '메세지가 도착했습니다.';
					else
						document.title = prevTitle;
				},1000);
				
				
				var notifyFile = '';
				
				if(session.employee.locale =='ko'){
					notifyFile = 'sound/noti_message_ko.wav';
				}else {
					notifyFile = 'sound/youVGotMail01_en.wav';
				}
				
				if(mw3.browser.indexOf('MSIE') > -1){
					var embed = document.createElement("embed"); 
					embed.setAttribute("src", notifyFile); 
					embed.setAttribute("hidden", true); 
					embed.setAttribute("autostart", true); 
					document.body.appendChild(embed);
					
					$(document).one('focusin', function(event){
						window.trayAlert = clearInterval(window.trayAlert);
						
						document.title = prevTitle;
					});
				}else{
					var audio = new Audio(notifyFile);
					audio.play();
					
					$(window).one('focus', function(event){
						window.trayAlert = clearInterval(window.trayAlert);
						
						document.title = prevTitle;
					});			
				}
			}
		}
	}
};