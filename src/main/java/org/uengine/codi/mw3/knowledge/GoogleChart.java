package org.uengine.codi.mw3.knowledge;

public class GoogleChart {

	public final static String CHARTTYPE_PIECHART 			= "pie";
	public final static String CHARTTYPE_LINECHART 			= "line";
	public final static String CHARTTYPE_COLUMNCHART 		= "column";
	
	String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	boolean loaded;
		public boolean isLoaded() {
			return loaded;
		}
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}
	String type;
		public String getType() {
			return type;
		}	
		public void setType(String type) {
			this.type = type;
		}
		
	String title;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

	Object data;
		public Object getData() {
			return data;
		}
		public void setData(Object data) {
			this.data = data;
		}
}
