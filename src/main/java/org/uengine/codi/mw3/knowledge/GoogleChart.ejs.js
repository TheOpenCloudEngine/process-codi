function loadChartApis(){
	try{
		google.load("visualization", "1", {packages:["corechart", "gauge", "table", "treemap", "geochart"], callback:chartApisLoaded});
	}catch(e){
		if (console) console.log(e);
	}
}

function chartApisLoaded(){
	$('div[classname="org.uengine.codi.mw3.knowledge.GoogleChart"]').each(function( index ) {
		var chartObjectId = $(this).attr('objectid');
		var object = mw3.objects[chartObjectId];
		if( !object.loaded ){
			object.getFaceHelper().drawChart();
			object.loaded = true;
		}
	});
}

var org_uengine_codi_mw3_knowledge_GoogleChart = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	
	var script = document.createElement("script");
	script.src = "https://www.google.com/jsapi?callback=loadChartApis";	
	script.type = "text/javascript";
	document.getElementsByTagName("head")[0].appendChild(script);
};

org_uengine_codi_mw3_knowledge_GoogleChart.prototype = {
	drawChart : function(){
		var object = mw3.objects[this.objectId];
		
		if(object.data != null){
			var data = google.visualization.arrayToDataTable(object.data);
			
			var options = {width: 450, height: 400, title: object.title};

	        var chart = null;
	    	
	        if(object.type == 'pie')
	        	chart = new google.visualization.PieChart(document.getElementById(this.objectDivId));
	        else if(object.type == 'line')
	        	chart = new google.visualization.LineChart(document.getElementById(this.objectDivId));
	        else if(object.type == 'column')
	        	chart = new google.visualization.ColumnChart(document.getElementById(this.objectDivId));
	        
	        if(chart != null)
	        	chart.draw(data, options);			
		}
	}
};


