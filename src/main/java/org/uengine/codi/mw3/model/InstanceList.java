package org.uengine.codi.mw3.model;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.Login;
import org.uengine.kernel.GlobalContext;

import java.util.ArrayList;


@Face(
		ejsPathMappingByContext=
				{
						"{where: 'pinterest', face: 'dwr/metaworks/org/uengine/codi/mw3/model/InstanceList_pinterest.ejs'}",
				}

)
public class InstanceList implements ContextAware{

	public final static int PAGE_CNT = 15;
	public final static int PAGE_CNT_MOBILE = 5;
	public final static int PAGE_CNT_DASHBOARD = 3;

	public InstanceList(){
		this(null);
	}

	public InstanceList(Session session){
		this.session = session;

		this.setNavigation(new Navigation(session));
		this.setPage(1);
	}

	int page;
	@Id
	@Hidden
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	MetaworksContext metaworksContext;
	public MetaworksContext getMetaworksContext() {
		return metaworksContext;
	}
	public void setMetaworksContext(MetaworksContext metaworksContext) {
		this.metaworksContext = metaworksContext;
	}

	IInstance instances;
	public IInstance getInstances() {
		return instances;
	}
	public void setInstances(IInstance instances) {
		this.instances = instances;
	}

	ArrayList<IInstance> arrayInstances;
	public ArrayList<IInstance> getArrayInstances() {
		return arrayInstances;
	}
	public void setArrayInstances(ArrayList<IInstance> arrayInstances) {
		this.arrayInstances = arrayInstances;
	}

	MoreInstanceList moreInstanceList;
	public MoreInstanceList getMoreInstanceList() {
		return moreInstanceList;
	}

	public void setMoreInstanceList(MoreInstanceList moreInstanceList) {
		this.moreInstanceList = moreInstanceList;
	}

	Navigation navigation;
	public Navigation getNavigation() {
		return navigation;
	}
	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}

	@AutowiredToClient
	public Session session;


	String folderId;
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	String folderName;
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	IWorkItem workItem;
	public IWorkItem getWorkItem() {
		return workItem;
	}
	public void setWorkItem(IWorkItem workItem) {
		this.workItem = workItem;
	}
//	@ServiceMethod(callByContent = true, except = { "instances",
//			"moreInstanceList" })
//	public void search() throws Exception {
//		setPage(0);
//		load(session);
//	}

	@ServiceMethod(callByContent = true, except = { "instances" })
	public void more() throws Exception {
		load(this.getNavigation());
	}

	@ServiceMethod(callByContent = true, except = { "workitem" })
	public void moreDocument() throws Exception{
		loadDocument(this.getNavigation());
	}

	@ServiceMethod(callByContent = true, onLoad = true)
	public InstanceList load() throws Exception {
		return load(this.getNavigation());
	}

	public InstanceList load(Navigation navigation) throws Exception {
		int count =  ("phone".equals(navigation.getMedia())?InstanceList.PAGE_CNT_MOBILE:InstanceList.PAGE_CNT);

		String usedSolr = GlobalContext.getPropertyString("solr.server.used");

		// 솔라설정 셋팅을 설정했는가? 검색어를 입력했는가? 그렇다면 솔라를 사용
		if("1".equals(usedSolr) && navigation.getKeyword() != null && !"".equals(navigation.getKeyword())) {
			// codi search to sorl
			ArrayList<IInstance> instanceContents = Instance.loadWithSolr(navigation, getPage() - 1, count);
			this.setArrayInstances(instanceContents);

			setMoreInstanceList(new MoreInstanceList());
			getMoreInstanceList().setNavigation(navigation);
			getMoreInstanceList().setPage(getPage() + 1);

		// 아니면 기존의 코디 디비를 사용
		} else {
			// codi search to db
			IInstance instanceContents = Instance.load(navigation, getPage() - 1, count);


			if(getMetaworksContext()==null) {
				instanceContents.setMetaworksContext(new MetaworksContext());
				instanceContents.getMetaworksContext().setWhere(IInstance.WHERE_INSTANCELIST);
			}else{
				instanceContents.setMetaworksContext(getMetaworksContext());
			}

			this.setInstances(instanceContents);

			// setting moreInstanceList
			if( instanceContents.size() >= count){
				setMoreInstanceList(new MoreInstanceList());
				getMoreInstanceList().setNavigation(navigation);
				getMoreInstanceList().setPage(getPage() + 1);
			}
		}

//		if("sns".equals(preferUX)){
//			if("oce".equals(session.getUx())){
//				instanceContents.setMetaworksContext(new MetaworksContext());
//				if("dashboard".equals(this.getMetaworksContext().getHow()))
//					instanceContents.getMetaworksContext().setWhere("dashboard");
//				instanceContents.getMetaworksContext().setHow("sns");			
//				instanceContents.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
//			}
//		}

		return this;
	}

	public InstanceList loadDocument() throws Exception{
		return loadDocument(this.getNavigation());
	}
	public InstanceList loadDocument(Navigation navigation) throws Exception{
		IInstance instance = null;
		Instance loadInst = new Instance();
		loadInst.session = this.session;
		if("UnlabeledDocument".equals(this.getMetaworksContext().getHow())){
			instance = loadInst.loadDocument();
		}else{
			instance = loadInst.loadDocument(getFolderId());
		}

		instance.getMetaworksContext().setHow("document");
		//setInstances(instance);
//		setWorkItem(workitem);
		setMoreInstanceList(new MoreInstanceList());
		getMoreInstanceList().setNavigation(navigation);
		getMoreInstanceList().setPage(getPage()+1);
		return this;
	}

//	private int findPerspectiveTypeCode(String typeString) {
//		if(typeString == null) {
//			return -1;
//		} else if(typeString.equals("inbox")) {
//			return 0;
//		} else if(typeString.equals("all")) {
//			return 1;
//		} else if(typeString.equals("request")) {
//			return 2;
//		} else if(typeString.equals("stopped")) {
//			return 3;
//		} else if(typeString.equals("organization")) {
//			return 4;
//		} else if(typeString.equals("process")) {
//			return 5;
//		} else if(typeString.equals("strategy")) {
//			return 6;
//		} else if(typeString.equals("taskExt1")) {
//			return 7;
//		} else if(typeString.equals("status")) {
//			return 8;
//		} else if("allICanSee".equals(typeString)){
//			return 9;
//		}
//		return -1;
//	}

	@ServiceMethod(target="instances")
	public void drillDown() throws Exception{

		System.out.println("SDfsdkf sldkfjs ldkfj sdlkfj seldkfj sdlkfj sldkjfsldkfj sdkl");

	}
}
