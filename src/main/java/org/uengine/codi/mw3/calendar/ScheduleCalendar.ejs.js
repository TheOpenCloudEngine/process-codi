var org_uengine_codi_mw3_calendar_ScheduleCalendar = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;
	
	
	this.COLOR_EVENT_SELF 		= "#348017";
	this.COLOR_EVENT_ANOTHER 	= "#C48189";
	this.COLOR_EVENT_COMPLETE 	= "#808080";
	
	
	this.session = mw3.getAutowiredObject('org.uengine.codi.mw3.model.Session');
	
	this.KOREA = 'ko';
	this.ENGLISH = 'en';
	
};

org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype = {
	loaded : function(){
		var objectId = this.objectId;
		var object = mw3.objects[objectId];
		
		if(object){
			var facehelper = this;
			
			mw3.importStyle('scripts/jquery/fullcalendar/fullcalendar.css');
			mw3.importScript('scripts/jquery/fullcalendar/fullcalendar.min.js', function(){
				facehelper.load();
			});
		}
	},
	load : function(){
		var object = mw3.objects[this.objectId];
		var objectId = this.objectId;
		$('#objDiv_' + this.objectId).addClass('mw3_layout').attr('objectId', this.objectId);
		
		var calendar = $('#scheduleCalendar_' + this.objectId);
		var options = {
			height: 650,
			header: {
				left: 'prev,today,next' ,
				center: '',
				//right: 'month,agendaWeek,agendaDay,month,prevYear,nextYear'
				right: 'title'
			},
			allDayDefault: true,
			firstDay: 1,
			
			selectable: true,
			selectHelper: true,
			editable: true,
			events: object.data,
			select: function(start, end, allDay) {
				mw3.getFaceHelper(objectId).dayClick(start, end, allDay);
			},
			eventClick: function(calEvent, jsEvent, view) {	
				mw3.getFaceHelper(objectId).eventClick(calEvent.id, calEvent.callType, view.name);
			},
			eventDrop: function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ){
				mw3.getFaceHelper(objectId).moveEvent(event.id, event.callType, view.name, dayDelta);
			}
		};
		
		if(this.session.employee.locale == this.KOREA){
			options.titleFormat = {
				month: "yyyy년 MMMM",
				week: "[yyyy] MMM d일{ [yyyy] MMM d일}",
				day: "yyyy년 MMM d일 dddd"
			};
			options.monthNames = ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"];
			options.monthNamesShort = ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"];
			options.dayNames = ["일요일","월요일","화요일","수요일","목요일","금요일","토요일"];
			options.dayNamesShort = ["일","월","화","수","목","금","토"];
			options.buttonText = {
				today : "오늘",
				month : "월별",
				week : "주별"
			};
		}
		
		calendar.fullCalendar(options);	
		calendar.fullCalendar('gotoDate', object.selDate);
	}
};

org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype.dayClick = function(start, end, allDay){
	var calendar = mw3.getObject(this.objectId);
	calendar.selDate = start;
	calendar.endDate = end;
	calendar.allDay = allDay;
						
	calendar.linkScheduleDay();
};

org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype.eventClick = function(schdId, callType, viewMode){
	var object = mw3.getObject(this.objectId);
	object.schdId = schdId;
	object.callType = callType;
	object.viewMode = viewMode;

	object.linkScheduleEvent();
};

org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype.moveEvent = function(schdId, callType, viewMode, moveDay){
	var calendar = mw3.getObject(this.objectId);
	calendar.schdId = schdId;
	calendar.selDate = moveDay;
	calendar.callType = callType;
	calendar.viewMode = viewMode;

	calendar.moveScheduleEvent();

};


org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype.addEvent = function(event){

	this.removeEvent(event);
	
	var calendar = $('#scheduleCalendar_' + this.objectId);

	if(event.complete){
		event.color = this.COLOR_EVENT_COMPLETE;
	}
	calendar.fullCalendar( 'renderEvent', event );
};

/*
org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype.addEvent = function(title, instId, startDate, endDate){
	var calendar = $('#scheduleCalendar_' + this.objectId);
	var object = mw3.objects[this.objectId];
	var myEvent = {
			  title: title,
			  id: instId,
			  allDay: object.allDay,
			  callType: 'instance',
			  color: '#0000cd',
			  start: object.startDate,
			  end: object.endDate
			};
	calendar.fullCalendar( 'renderEvent', myEvent );
};
*/

org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype.removeEvent = function(event){
	var calendar = $('#scheduleCalendar_' + this.objectId);
	calendar.fullCalendar( 'removeEvents', event.id );
};

org_uengine_codi_mw3_calendar_ScheduleCalendar.prototype.resize = function(){
	var calendar = $('#scheduleCalendar_' + this.objectId);
	calendar.fullCalendar('render');	
};