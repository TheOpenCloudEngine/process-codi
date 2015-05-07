var CheckBox = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	this.objectDivId = '#' + mw3._getObjectDivId(this.objectId);
};

CheckBox.prototype = {
		getValue : function(){
			var object = mw3.objects[this.objectId];
			var checkBoxName = "checkBox_" + this.objectId;
			var value = '';			
			$("input:checkbox[name='"+checkBoxName+"']:checked").each(function(i){
				var tmp = $(this).val();
				if( i > 0 ){
					value += ",";
				}				
				value += tmp;
			});
			
			if(value == '')
				value = $('input#checkedList').val();
			
			return value;
		}
};