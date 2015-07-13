var org_uengine_codi_mw3_model_Tray = function(objectId, className){

	this.objectId = objectId;
	this.className = className;	
	this.divId = '#objDiv_' + this.objectId;
	
	var thisWidth = $(this.divId).width()-650;
	//var trayObject = $(this.divId).find('div.#sm01');
	var trayObject = $(this.divId).find('div#sm01');
	
	trayObject.width(thisWidth/trayObject.length);
	
	if(trayObject.width() > 160){
		trayObject.width(160);
	}

};

org_uengine_codi_mw3_model_Tray.prototype.addTray = function(title, instId, notify){
	
	 if(instId){
		/*
		 * 채팅 시: 트레이 바가 추가되고, 메시지 도착 음성 메시지 출력, 브라우저 화면 표시 기능 => 노티 뱃지에 업무 추가시 호출되도록 변경(이동)
		if(typeof notify != 'undefined' && notify){
			if(!mw3.windowFocus){
				var prevTitle = document.title;
				
				if(window.trayAlert)
					window.trayAlert = clearInterval(window.trayAlert);
				

				
				window.trayAlert = setInterval(function(){
					if(document.title == prevTitle)
						document.title = title + '님이 메세지를 보냈습니다.';
					else
						document.title = prevTitle;
				},1000);
				
				
				var notifyFile = 'sound/noti_message.wav';
				
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
		}*/
			
		var tray = mw3.getObject(this.objectId);
		 tray.targetItem = {
			 title: title,
			 instId: instId
		 };

		 tray.addTrayItem();
	 }
	
};
