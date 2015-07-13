package org.uengine.codi.mw3.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.directwebremoting.io.FileTransfer;
import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Download;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.CodiProcessDefinitionFactory;
import org.uengine.codi.mw3.CodiClassLoader;
import org.uengine.kernel.Activity;
import org.uengine.kernel.RoleMapping;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.processmanager.ProcessManagerRemote;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Face(ejsPathMappingByContext={"{when: 'newInstance', face: 'dwr/metaworks/org/uengine/codi/mw3/model/ResourceFile_newInstance.ejs'}", "{when: 'appendProcessMap', face: 'dwr/metaworks/org/uengine/codi/mw3/model/ResourceFile_newInstance.ejs'}"})
@XStreamAlias("resource")
public class ResourceFile implements ContextAware {
	
//	static{
//		CodiDwrServlet.xstream.processAnnotations(ResourceFile.class);
//	}

	public ResourceFile() {
		setMetaworksContext(new MetaworksContext());
		setAlias("");
	}
	
	@XStreamOmitField
	String alias;
		@Id
		public String getAlias() {
			return alias;
		}	
		public void setAlias(String fullPath) {
			this.alias = fullPath;
			
			if(name==null && alias!=null && alias.length() > 0){
				int whereLastSlash = getAlias().lastIndexOf("/");
				
				if(whereLastSlash>-1) {
					int lastIndexOfDot = getAlias().lastIndexOf(".");
					name = getAlias().substring(whereLastSlash+1, lastIndexOfDot != -1 ? lastIndexOfDot : getAlias().length());
				}
			}
			
		}


	@XStreamAsAttribute
	@XStreamAlias("value")
	String uEngineAlias;
	public String getuEngineAlias() {
		return uEngineAlias;
	}
	public void setuEngineAlias(String uEngineAlias) {
		this.uEngineAlias = uEngineAlias;
	}

	@XStreamOmitField
	boolean isFolder;
	
		public boolean isFolder() {
			return isFolder;
		}
	
		public void setFolder(boolean isDirectory) {
			this.isFolder = isDirectory;
		}

	@XStreamOmitField
	String objType;			
		public String getObjType() {
			return objType;
		}
	
		public void setObjType(String type) {
			this.objType = type;
		}

	@XStreamAsAttribute
	String name;
	
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
		

	@XStreamOmitField
	boolean isOpened;	
		public boolean isOpened() {
			return isOpened;
		}
	
		public void setOpened(boolean isOpened) {
			this.isOpened = isOpened;
		}
		
	@XStreamOmitField
	boolean isDeleted;

		public boolean isDeleted() {
			return isDeleted;
		}
	
		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

	@XStreamImplicit
	ArrayList<ResourceFile> childs;
		public ArrayList<ResourceFile> getChilds() {
			return childs;
		}
		public void setChilds(ArrayList<ResourceFile> childFiles) {
			this.childs = childFiles;
		}
		
	@XStreamOmitField
	String filter;
		public String getFilter() {
			return filter;
		}
		public void setFilter(String filter) {
			this.filter = filter;
		}
		
	@ServiceMethod(callByContent=true, except="childs", target="self")
	public void drillDown() throws Exception {
		if(isOpened()){
			setOpened(false);
			
			return;
		}	

		File file = new File(CodiClassLoader.getCodeBaseRoot() + getAlias());
		
//		if(file.getName().startsWith("__") && !file.exists()){
		if(!file.getName().contains(".") && !file.exists()){
			file.mkdirs();
		}
		
		if(!file.isDirectory()){
			return;
		}
		
		setOpened(true);
		
		String[] childFilePaths = file.list();
		childs = new ArrayList<ResourceFile>();
				
		for(int i=0; i<childFilePaths.length; i++){
			File childFile = new File(file.getAbsolutePath() + File.separatorChar + childFilePaths[i]);
			
			ResourceFile rf = new ResourceFile();
			
			rf.setName(childFilePaths[i]);
			rf.setAlias(this.getAlias() + File.separatorChar + rf.getName());
			rf.setuEngineAlias(getuEngineAlias() +rf.getName());
			rf.setFolder(childFile.isDirectory());
			rf.setMetaworksContext(getMetaworksContext());
			
			if(childFile.isDirectory()){
				rf.setObjType("folder");
			}else{
				rf.setObjType(rf.getName().substring(rf.getName().lastIndexOf(".")+1));

				//ignores other types except process if 'newInstance' mode
				if(getMetaworksContext()!=null && (("appendProcessMap".equals(getMetaworksContext().getWhen()) || "newInstance".equals(getMetaworksContext().getWhen())) && !"process".equals(rf.getObjType()) && !"process2".equals(rf.getObjType()) && !"wpd".equals(rf.getObjType()) ))
					continue;
			}
			
			rf.setFilter(getFilter());
			
			if(childFile.isDirectory() || getFilter()==null || (getFilter().indexOf(rf.getObjType()) > -1))
				childs.add(rf);
		}
		
	}
	
	@ServiceMethod
	public void drillDownDeeply() throws Exception{
		drillDown();
		
		if(childs!=null)
		for(ResourceFile child : childs){
			if(child.isFolder())
				child.drillDownDeeply();
		}
	}

	
	


	@ServiceMethod(callByContent=true, except="childs", inContextMenu=true, needToConfirm=true)
	public Object delete() throws Exception {
		
		String resourceBase = CodiClassLoader.getMyClassLoader().getCodebase() + "/";
		
		File file = new File(resourceBase + getAlias());
		
		file.delete();
		
		setDeleted(true);
		
		return new Remover(this);
		
	}

	@Face(displayName="Export to Activity App Zip")
	@ServiceMethod(callByContent=true, except="childs", inContextMenu=true, target= ServiceMethodContext.TARGET_POPUP)
	public Download exportToJar() throws Exception{
	   final int BUFFER = 2048;
	      
//	   try {
	         BufferedInputStream origin = null;
	         FileOutputStream dest = new 
	           FileOutputStream(this.getName() + ".prom"); //TODO: [before production] temporal folder and concurrency safe handling is needed
	         ZipOutputStream out = new ZipOutputStream(new 
	           BufferedOutputStream(dest));
	         //out.setMethod(ZipOutputStream.DEFLATED);
	         byte data[] = new byte[BUFFER];
	         // get a list of files from current directory
	 		String resourceBase = CodiClassLoader.getMyClassLoader().getCodebase() + "/";
			
			File root = new File(resourceBase + getAlias());

			setOpened(false);
			
			drillDown();
			
			//out.
			
	        for (ResourceFile child : childs) {
	        	
	            File file = new
	  	              File(resourceBase + "/" + child.getAlias());
	            
	            if(file.isDirectory()){
	            	continue; //TODO: need to recursively add the child directories' files as well.
	            }

	            FileInputStream fi = new 
	  	              FileInputStream(file);
	            
	            origin = new 
	              BufferedInputStream(fi, BUFFER);
	            ZipEntry entry = new ZipEntry(child.getAlias());
	            out.putNextEntry(entry);
	            int count;
	            while((count = origin.read(data, 0, 
	              BUFFER)) != -1) {
	               out.write(data, 0, count);
	            }
	            origin.close();
	         }
	         out.close();
	         
	        return 
	        	new Download(
	        		new FileTransfer(
        				 new String(this.getName().getBytes("EUC-KR"),"ISO8859_1") + ".prom", 
        				 "application/octet-stream", 
        				 new FileInputStream(this.getName() + ".prom")
	        		)
	        	);
//	   } catch(Exception e) {
//		   e.printStackTrace();
//	   }
		//return popup;
	}


	@ServiceMethod(inContextMenu=true, callByContent=true, mouseBinding="drag", keyBinding="Ctrl+X")
	public Session cut(){
		session.setClipboard(this);
		return session;
	}
	
	@ServiceMethod(inContextMenu=true, callByContent=true, mouseBinding="drop", keyBinding="Ctrl+V", needToConfirm=true)
	public Object[] paste() throws Exception{
		Object clipboard = session.getClipboard();
		if(clipboard instanceof ResourceFile && isFolder()){
			ResourceFile fileInClipboard = (ResourceFile) clipboard;
			String resourceBase = CodiClassLoader.getMyClassLoader().getCodebase();

			new File(resourceBase + fileInClipboard.getAlias()).renameTo(
					new File(resourceBase + getAlias() + "/" + fileInClipboard.getName())
				);
			
			drillDown();
			
			return new Object[]{fileInClipboard, this};
		}else{
			return new Object[]{this};
		}
		
		
	}
	
	@ServiceMethod(inContextMenu=true, callByContent=true, target="popup", keyBinding="Ctrl+R")
	public Popup rename(){
		FileRenamer fileRenamer = new FileRenamer();
		fileRenamer.setFile(this);
		
		Popup renamer = new Popup(fileRenamer);
		
		return renamer;
	}
	
	@ServiceMethod(callByContent=true, except="childs")
	public ContentWindow design() throws Exception {
//		try{
		
//			if("form".equals(objType)){
//				FormDesignerContentPanel formDesigner = new FormDesignerContentPanel();
//				formDesigner.load(databaseMe().getDefId().toString());
//	
//				return formDesigner;
//			}else if("process".equals(objType)){
//				processDesigner.load(databaseMe().getDefId().toString());
//				
//				return processDesigner;
				
//			}else
		

//			else if("entity".equals(objType)){
//				entityDesignerWindow.load(databaseMe().getDefId().toString());
//				
//				return entityDesignerWindow;		
//			}
//		}finally{
//			codiPmSVC.remove();
//		}	
		
		// TODO Auto-generated method stub
		return null;
	}	
	

	
	@ServiceMethod(callByContent=true, except="childs")
	public Object[] appendProcessMap() throws Exception {
		String name = this.getName();

		ProcessMap processMap = new ProcessMap();
		org.uengine.kernel.ProcessDefinition procDef = null;
		
		// ���濡���몄�� 耳���� 珥�湲고��
		ProcessManagerBean pmb = (ProcessManagerBean)processManager;
		CodiProcessDefinitionFactory.getInstance(pmb.getTransactionContext()).removeFromCache(this.getuEngineAlias());
				
		if(name.endsWith(".process")){
			name = name.substring(0, name.length() - 8);
			
			/*
			procDef = processManager.getProcessDefinition(getuEngineAlias());
			String fullCommandPhrase = procDef.getDescription().getText();

			if(fullCommandPhrase!=null){
				int commandCotentStarts = fullCommandPhrase.indexOf(':');
				if(-1 < commandCotentStarts){
	
					processMap.setCmPhrase(fullCommandPhrase);
					processMap.setCmTrgr(fullCommandPhrase.substring(0, commandCotentStarts));
				}
			}
			*/
		}else if(name.endsWith(".process2")){
			name = name.substring(0, name.length() - 9);
			
			procDef = processManager.getProcessDefinition(getAlias());
			String fullCommandPhrase = procDef.getDescription();
			
			if(fullCommandPhrase!=null){
				int commandCotentStarts = fullCommandPhrase.indexOf(':');
				if(-1 < commandCotentStarts){
					
					processMap.setCmPhrase(fullCommandPhrase);
					processMap.setCmTrgr(fullCommandPhrase.substring(0, commandCotentStarts));
				}
			}
		}else if(name.endsWith(".wpd")){
			name = name.substring(0, name.length() - 4);
			
			procDef = processManager.getProcessDefinition(getAlias());
			String fullCommandPhrase = procDef.getDescription();
			
			if(fullCommandPhrase!=null){
				int commandCotentStarts = fullCommandPhrase.indexOf(':');
				if(-1 < commandCotentStarts){
					
					processMap.setCmPhrase(fullCommandPhrase);
					processMap.setCmTrgr(fullCommandPhrase.substring(0, commandCotentStarts));
				}
			}
		}
		
			
		processMap.setMapId(session.getCompany().getComCode() + "." + this.getAlias());
		processMap.setDefId(this.getuEngineAlias());
		processMap.setName(name);
		processMap.setComCode(session.getCompany().getComCode());
		processMap.processManager = processManager;
		

		if(!processMap.confirmExist())
			throw new Exception("$AlreadyAddedApp");

		processMap.createMe();
//		processMap.createRoleDef();
		
		
		ProcessMapList processMapList = new ProcessMapList();
		processMapList.load(session);
		
		
		return new Object[]{processMapList, new Remover(new ModalWindow())};
	}

	
	protected ArrayList<IUser> friends;

	
	@ServiceMethod
	public org.uengine.kernel.ProcessDefinition loadProcessDefinition() throws Exception{
		return processManager.getProcessDefinition(getAlias());
	}

	/**
	 * Not used anymore ... see ProcessMap.initiate instead
	 * @return
	 * @throws Exception
	 */
	@ServiceMethod(callByContent=true, except="childs")
	public Object[] initiate() throws Exception{
		InstanceViewContent instanceView = instanceViewContent;// = new InstanceViewContent();
		

		String instId = processManager.initializeProcess(getAlias());
		
		org.uengine.kernel.ProcessDefinition def = processManager.getProcessDefinition(getAlias());
		if(def.getRoles() == null || def.getRoles().length == 0){
			
			//// setting initiator if there's nothing to state the initiator in the definition ////
			RoleMapping rm = RoleMapping.create();
			if(session.user!=null){
				rm.setName("Initiator");
				rm.setEndpoint(session.user.getUserId());
			}

			processManager.putRoleMapping(instId, rm);

		}

		if(friends!=null){
			for(IUser friend : friends){
				RoleMapping receiver = RoleMapping.create();
				receiver.setEndpoint(friend.getUserId());
				receiver.setName("fol_" + friend.getUserId());

				processManager.putRoleMapping(instId, receiver);
				
			}
			

		}
		
		RoleMapping rm = RoleMapping.create();
		if(session.user!=null)
			rm.setEndpoint(session.user.getUserId());
		
		processManager.setLoggedRoleMapping(rm);
		
		processManager.executeProcess(instId);
		processManager.applyChanges();
	
		IInstance instanceRef = new Instance();
		instanceRef.setInstId(new Long(instId));
		
		instanceView.session = session;
		instanceView.load(instanceRef);
		
		InstanceList instanceList = new InstanceList(session);
		instanceList.load();
		
		return new Object[]{instanceView, instanceList};

		
	}
	
	@AutowiredFromClient
	transient public NewInstancePanel newInstancePanel;
	
	@AutowiredFromClient
	transient public Session session;
	


	@Autowired
	transient public InstanceViewContent instanceViewContent;

	@Autowired
	transient public ProcessManagerRemote processManager;

	
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
		@ServiceMethod(callByContent=true, payload={"objType"})
		public Object[] loadProcess() throws Exception {
			if(getObjType().equals("process")) {
				return Perspective.loadInstanceListPanel(session, Perspective.MODE_PROCESS, Perspective.TYPE_NEWSFEED, getAlias());
			}
			return null;
		}
	
}
