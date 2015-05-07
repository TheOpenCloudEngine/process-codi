package org.uengine.codi.mw3.model;

import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Test;
import org.metaworks.common.MetaworksUtil;
import org.uengine.codi.mw3.admin.WebEditor;

//@Face(displayName="답글")
public class CommentWorkItem extends WorkItem{
	
	public CommentWorkItem(){
		setType(WORKITEM_TYPE_COMMENT);
	}
	
	@Hidden(on=false)
	@Range(size = 80)
	@Face(displayName = "")
	public String getTitle() {
		return super.getTitle();
	}
	

	@Test(scenario="first", starter=true, instruction="$first.Add", next="autowiredObject.org.uengine.codi.mw3.model.InstanceView.monitor()")
	@ServiceMethod(callByContent = true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] add() throws Exception {
		
		boolean isOwnReturn = false;
		Object[] returnObjects = null;
		if( (WHEN_NEW.equals(getMetaworksContext().getWhen()) && this.getInstId() == null) || WHEN_EDIT.equals(getMetaworksContext().getWhen()) ){
			isOwnReturn = true;
		}else{
			isOwnReturn = false;
		}
		
		// 덧글 상태일때 덧글이 길면 메모로 변경해주는 기능
		if(this.getTitle().length() > TITLE_LIMIT_SIZE){
			this.setType(WORKITEM_TYPE_MEMO);
			
			Class type = MetaworksUtil.getDesiredTypeByTypeSelector(this);
			if(type == null)
				throw new Exception("can't convert CommentWorkItem to MemoWorkItem");
			
			WorkItem workItem = (WorkItem)MetaworksUtil.cast(this, type);
			
			
			workItem.processManager = processManager;
			workItem.session = session;
			workItem.setContent(getTitle());
			
			String title = getTitle().substring(0, MEMO_TITLE_LIMIT_SIZE) + "...";
			if( title.indexOf("\n") > 0 ){
				title = title.substring(0, title.indexOf("\n"));
			}
			
			workItem.setTitle(title);
			workItem.setMemo(new WebEditor(workItem.getContent()));
			
			returnObjects = workItem.add();
			if( !isOwnReturn ){
				// 덧글이 새로 올라왔을 경우 특별한 리턴처리를 해준다. ( 화면상에 taskId 가 없기때문에 refresh를 false 로 가져가야함 )
				returnObjects = new Object[]{new Refresh(workItem, false, true)};
			}
		}else{
			returnObjects = super.add();
			if( !isOwnReturn ){
				// 덧글이 새로 올라왔을 경우 특별한 리턴처리를 해준다. ( 화면상에 taskId 가 없기때문에 refresh를 false 로 가져가야함 )
				returnObjects = new Object[]{new Refresh(this, false, true)};
			}
		}
		return returnObjects;
	}
	
	@Test(scenario="first", starter=true, instruction="$first.NewActivity", next="autowiredObject.org.uengine.codi.mw3.model.IProcessMap@IssueManagement.process.initiate()")
	public Popup newActivity() throws Exception {
		// TODO Auto-generated method stub
		return super.newActivity();
	}	
}
