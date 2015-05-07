package org.uengine.codi.mw3.widget;

public class Tab {

	private String tabTitle;
	private Object tabContent;
	public String getTabTitle() {
		return tabTitle;
	}
	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}
	public Object getTabContent() {
		return tabContent;
	}
	public void setTabContent(Object tabContent) {
		this.tabContent = tabContent;
	}
	
	public Tab(String title) {
		this.tabTitle = title;
		this.tabContent = new Object();
	}
	
	public Tab(String title,Object obj) {
		this.tabTitle = title;
		this.tabContent = obj;
	}
	
}
