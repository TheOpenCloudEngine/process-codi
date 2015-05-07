var MoneyField = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
}

MoneyField.prototype.keydown = function(){
	 var e = window.event;

	 if (e.keyCode == 8 || e.keyCode == 9 || e.keyCode == 13) {
		 return true;
	 } else if (e.keyCode >= 33 && e.keyCode <= 46) {
		 return true;
	 } else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
    	 e.returnValue = true;
     } else { 
    	 window.event.returnValue = false;
    	 
    	 return false;
     }	
}

MoneyField.prototype.getValue = function(){
	
	var strValue = mw3.getObjectFromUI(this.objectId);
	
	//change to number
	return eval(strValue);
}