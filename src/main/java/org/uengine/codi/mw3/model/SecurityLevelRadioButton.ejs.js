var org_uengine_codi_mw3_model_SecurityLevelRadioButton = function(objectId, className) {
	
	
	
	this.objectId = objectId;
	this.className = className;
	
	
	

};

org_uengine_codi_mw3_model_SecurityLevelRadioButton.prototype = {
		checkSecuopt : function(a, b){
			check_img(a,b);
			var object = mw3.objects[this.objectId];	
			object.selected = a;
		}
};

function check_img(a,b){
	a="choiceImg"+a
	for (i=0;i<3;i++ )
	   {
	    b = "choice" + i;
	    // 선택 안했을때의 이미지 경로
	    if(document.all[b] ){
	    	document.all[b].src = "images/waveStyle/security_btn_off_"+i+".png";
	    }
	   }
	  // 선택 했을때 의 이미지 경로
	 $("#"+a).attr("src","images/waveStyle/security_btn_"+a+".png");	
}

/*
org_uengine_codi_mw3_model_SecurityLevelRadioButton.prototype= {
	check_img : function(a,b) {
		alert("dd")
		for (i=0;i<3;i++ )
		   {
		    b = "choice" + i;
		    // 선택 안했을때의 이미지 경로
		   document.all[b].src = "images/waveStyle/security_btn_off_"+i+".png";

		   }
		  // 선택 했을때 의 이미지 경로
		 document.all[a].src="images/waveStyle/security_btn_"+a+".png";
		
		
	}
	
}	
	
*/