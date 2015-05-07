var org_uengine_codi_mw3_model_AddEmployeeInDept = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
};
org_uengine_codi_mw3_model_AddEmployeeInDept.prototype = {
		checkList : function(){
			var empCodeList = "";
			$("input:checkbox[name='empCheckBox']:checked").each(function(i){
				var empcode = $(this).attr("id");
				if( i > 0 ) empCodeList += ",";
				empCodeList += empcode;
			});
			if( empCodeList == "" ){
				alert('선택된 값이 없습니다.');
				return;
			}
			var object = mw3.objects[this.objectId];
			object.empCodeList = empCodeList;
			object.saveEmployeeInDept();
		}
};

