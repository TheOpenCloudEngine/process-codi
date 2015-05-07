var imageSearcher = null;

function loadImageApis(){
	try{
		google.load('search', '1', {"callback" : imageApisLoaded});
	}catch(e){
		if (console) console.log(e);
	}
}

function imageApisLoaded(){
	mashupGoogleImage_OnLoad();
}

function mashupGoogleImage_OnLoad() {
	if(typeof google != 'undefined'){
		imageSearcher = new google.search.ImageSearch();
		//imageSearcher.setRestriction(google.search.Search.RESTRICT_SAFESEARCH, google.search.Search.SAFESEARCH_OFF);
		imageSearcher.setResultSetSize(8);
		
		imageSearcher.setSearchCompleteCallback(this, function(searcher){
			var contentDiv = document.getElementById('mashup_image');
			contentDiv.innerHTML = '';
			
			if (searcher.results && searcher.results.length > 0) {					
				var results = searcher.results;
				
				var sepCount = 0;
				for (var i = 0; i < results.length; i++) {
					var result = results[i];
					var imgContainer = document.createElement('div');
					imgContainer.className = "imgContainer"; 
					//imgContainer.style.border = "none";
					imgContainer.googleData = result;
					imgContainer.ondblclick  = function() {
						var focusWfNode = $('.wfnode_current_focus');
						if(focusWfNode.length > 0){
							var objectId = focusWfNode.attr('objectId');
							mw3.getFaceHelper(objectId).insertNodeAfter(this.googleData);
						}
					}
					
					var newImg = document.createElement('img');
					newImg.src = result.tbUrl;
					
					imgContainer.appendChild(newImg);						
					contentDiv.appendChild(imgContainer);
				}

			}
			
		}, [imageSearcher]);    	  		
  	}
}
		
var org_uengine_codi_mw3_knowledge_MashupGoogleImage = function(objectId, className){
	var script = document.createElement("script");
	script.src = "https://www.google.com/jsapi?callback=loadImageApis";	
	script.type = "text/javascript";
	document.getElementsByTagName("head")[0].appendChild(script);
};

org_uengine_codi_mw3_knowledge_MashupGoogleImage.prototype.clear = function(){
	var contentDiv = document.getElementById('mashup_image');
	contentDiv.innerHTML = '검색된 결과가 없습니다.';
};


org_uengine_codi_mw3_knowledge_MashupGoogleImage.prototype.search = function(keyword){
	if(imageSearcher != null)
		imageSearcher.execute(keyword);
};

      

  
      
