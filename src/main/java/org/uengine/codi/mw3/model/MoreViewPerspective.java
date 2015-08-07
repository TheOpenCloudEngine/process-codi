package org.uengine.codi.mw3.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.ServiceMethod;

public class MoreViewPerspective extends Perspective {
	boolean more;
		public boolean isMore() {
			return more;
		}
		public void setMore(boolean more) {
			this.more = more;
		}

	boolean enableMore;
		public boolean isEnableMore() {
			return enableMore;
		}
		public void setEnableMore(boolean enableMore) {
			this.enableMore = enableMore;
		}

	public MoreViewPerspective(){
		super();
	}

	@Available(condition="(typeof enableMore != 'undefined' && enableMore) && (typeof more != 'undefined' && more)")
	@ServiceMethod(callByContent=true, except="child")
	public void collapse() throws Exception {
		this.setMore(false);
		this.loadChildren();
	}

	@Available(condition="(typeof enableMore != 'undefined' && enableMore) && (typeof more == 'undefined' || !more)")
	@ServiceMethod(callByContent=true, except="child")
	public void expand() throws Exception {
		this.setMore(true);
		this.loadChildren();
	}
}
