package org.uengine.codi.mw3.model;

import java.io.Serializable;
import java.util.Date;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.component.SelectBox;

public class RemoteConferenceDate implements ContextAware{
	
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	java.util.Date date;
		public java.util.Date getDate() {
			return date;
		}
		public void setDate(java.util.Date date) {
			this.date = date;
		}
	
	SelectBox hour;
		public SelectBox getHour() {
			return hour;
		}
		public void setHour(SelectBox hour) {
			this.hour = hour;
		}
	
	SelectBox minute;
		public SelectBox getMinute() {
			return minute;
		}
		public void setMinute(SelectBox minute) {
			this.minute = minute;
		}
	
	
	public RemoteConferenceDate(){
		
	}
	
	public void load(){
		metaworksContext = new MetaworksContext();
		metaworksContext.setWhen(MetaworksContext.WHEN_EDIT);
		date = new Date();
		minute = new SelectBox();
		hour = new SelectBox();
		
		this.loadHour();
		this.loadMinute();
	}
	
	public void loadMinute(){
		int maxVal = 60;
		for(int i =0 ; i < maxVal; i++){
			minute.add(String.valueOf(i), String.valueOf(i));
		}
	}
	public void loadHour(){
		int maxVal = 24;
		for(int i =0 ; i < maxVal; i++){
			hour.add(String.valueOf(i), String.valueOf(i));
		}
	}
	
}
