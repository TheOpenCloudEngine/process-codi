var org_uengine_codi_mw3_SignUp = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.objectDiv.css({'height':'100%','overflow':'auto'})
	
	rolling_banner(5000);
	
};

function rolling_banner(settime){
	setTimeout(function(){
		$(".bn01" ).effect("fade",2000);
		$(".bn02" ).fadeIn(2000);
		setTimeout(function(){
			$(".bn02" ).effect("fade",2000);
			$(".bn03" ).fadeIn(2000);
			setTimeout(function(){
				$(".bn03" ).effect("fade",2000);
				$(".bn01" ).fadeIn(2000);
				rolling_banner(settime);
			},settime);
		},settime);
	},settime)
	
}