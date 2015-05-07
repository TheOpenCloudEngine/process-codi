var videoSearcher = null;

function loadVideoApis(){
	try{
		google.load('search', '1', {"callback" : videoApisLoaded});
	}catch(e){
		if (console) console.log(e);
	}
}

function videoApisLoaded(){
	mashupGoogleVideo_OnLoad();
}

function mashupGoogleVideo_OnLoad() {
	if(typeof google != 'undefined'){
		videoSearcher = new google.search.VideoSearch();
		videoSearcher.setResultSetSize(8);
		
		videoSearcher.setSearchCompleteCallback(this, function(searcher){
			var contentDiv = document.getElementById('mashup_video');
			contentDiv.innerHTML = '';
			
			if (searcher.results && searcher.results.length > 0) {
				var results = searcher.results;
				for (var i = 0; i < results.length; i++) {
					var result = results[i];
					
					var videoContainer = document.createElement('div');
					videoContainer.className = "videoContainer";						
					videoContainer.googleData = result;
//					videoContainer.onclick = function(){
//						selectedItem && (selectedItem.style.background = "none");
//						this.style.background = "#b2bdc1";
//						contentSelect(this);							
//					}
					videoContainer.ondblclick  = function() {
						var focusWfNode = $('.wfnode_current_focus');
						if(focusWfNode.length > 0){
							var objectId = focusWfNode.attr('objectId');
							mw3.getFaceHelper(objectId).insertNodeVideo(this.googleData);
						}
					}
					var newImg = document.createElement('img');
					newImg.src = result.tbUrl;
					
					videoContainer.appendChild(newImg);						
					contentDiv.appendChild(videoContainer);
				}

			}
		}, [videoSearcher]);		
	}
}

var org_uengine_codi_mw3_knowledge_MashupVideo = function(objectId, className){
	var script = document.createElement("script");
	script.src = "https://www.google.com/jsapi?callback=loadVideoApis";	
	script.type = "text/javascript";
	document.getElementsByTagName("head")[0].appendChild(script);
};

org_uengine_codi_mw3_knowledge_MashupVideo.prototype.clear = function(){
	var contentDiv = document.getElementById('mashup_video');
	contentDiv.innerHTML = '검색된 결과가 없습니다.';
};


org_uengine_codi_mw3_knowledge_MashupVideo.prototype.search = function(keyword){
	if(videoSearcher != null)
		videoSearcher.execute(keyword);
};