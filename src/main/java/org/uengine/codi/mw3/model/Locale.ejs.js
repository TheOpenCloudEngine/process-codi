var org_uengine_codi_mw3_model_Locale = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;

	var object = mw3.objects[this.objectId];
	
	if(object && object.language && object.resourceBundle){
		jQuery.timeago.settings.strings = {
				  suffixAgo: mw3.localize("$timeago.suffixAgo"),
				  suffixFromNow: mw3.localize("$timeago.suffixFromNow"),
				  seconds: mw3.localize("$timeago.seconds"),
				  minute: mw3.localize("$timeago.minute"),
				  minutes: mw3.localize("$timeago.minutes"),
				  hour: mw3.localize("$timeago.hour"),
				  hours: mw3.localize("$timeago.hours"),
				  day: mw3.localize("$timeago.day"),
				  days: mw3.localize("$timeago.days"),
				  month: mw3.localize("$timeago.month"),
				  months: mw3.localize("$timeago.months"),
				  year: mw3.localize("$timeago.year"),
				  years: mw3.localize("$timeago.years"),
				  wordSeparator: " ",
				  number: []
				};	
	}
};