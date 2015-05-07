package org.uengine.codi.mw3.model;

import org.uengine.codi.mw3.knowledge.WfPanel;

public class StrategicPerspective extends CollapsePerspective {

	public StrategicPerspective() throws Exception {
		setLabel("Strategic");
	}
	
	
	



	@Override
	protected void loadChildren() throws Exception {
		strategyMap = new WfPanel();
		strategyMap.session = session;
		
		if(session.getEmployee().isApproved()){
			strategyMap.load(session.getCompany().getComCode());	
		}else{
			strategyMap.load(session.getUser().getUserId());
		}		
	}






	WfPanel strategyMap;

		public WfPanel getStrategyMap() {
			return strategyMap;
		}
	
		public void setStrategyMap(WfPanel strategyMap) {
			this.strategyMap = strategyMap;
		}

}
