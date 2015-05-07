package org.uengine.codi.mw3.knowledge;

import java.util.ArrayList;

import org.metaworks.MetaworksContext;
import org.metaworks.dao.DAOUtil;

public class KnowlegeNode extends WfNode {

	@Override
	public ArrayList<WfNode> loadChildren() throws Exception {
		
		ArrayList<WfNode> child = new ArrayList<WfNode>();				

		DAOUtil daoUtil = new DAOUtil();
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT *");
		sb.append("  FROM bpm_knol");
		sb.append(" WHERE parentId=?parentId");
		sb.append("   AND type=?type");
		sb.append("	ORDER BY " + daoUtil.replaceReservedKeyword("no"));
		
		IWfNode findNode = (IWfNode) sql(IWfNode.class,	sb.toString());
		
		findNode.set("parentId", this.getId());
		findNode.set("type", this.getType());
		findNode.select();
		
		if(findNode.size() > 0){
			while (findNode.next()) {
				WfNode node = this.makeNewNode();
				
				node.copyFrom(findNode);
				node.setChildNode(new ArrayList<WfNode>());
				node.setMetaworksContext(new MetaworksContext());
				node.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
				node.getMetaworksContext().setWhere(this.getMetaworksContext().getWhere());

				if(node.getVisType()==null){
					node.getMetaworksContext().setHow("normal");
				}else{
					node.getMetaworksContext().setHow(node.getVisType());
				}
				
				node.setLoadDepth(this.getLoadDepth());
				node.setClose(true);
				node.setFirst(this.isFirst());
				
				if(!this.isFirst())
					node.load();
				
				child.add(node);
			}
		}
			
		return child;
	}

	public void addChildNode(int index, WfNode newNode) throws Exception {
		newNode.setNo(index);
		newNode.setParentId(this.getId());
		
		childNode.add(index, newNode);
		
		DAOUtil daoUtil = new DAOUtil();
		// update
		if(childNode.size()-1 > index){
			StringBuffer sb = new StringBuffer();
			sb.append("update bpm_knol");
			sb.append("   set " + daoUtil.replaceReservedKeyword("no") + "=" + daoUtil.replaceReservedKeyword("no") + "+1");
			sb.append(" where parentId=?parentId");
			sb.append("   and " + daoUtil.replaceReservedKeyword("no") + ">=?no");
			sb.append("   and type=?type");
			
			IWfNode updateNode = sql(sb.toString());
			updateNode.setParentId(this.getId());
			updateNode.setNo(index);
			updateNode.setType(this.getType());
			
			updateNode.update();
		}
		
		// reorder
		for(int i=index; i<childNode.size(); i++){
			IWfNode node = (WfNode)childNode.get(i);
			
			node.setNo(i);
		}
		
	}
	
	public void removeChildNode(int index) throws Exception {
		childNode.remove(index);
		
		if(childNode.size() > 0){
			for(int i=index; i<childNode.size(); i++){
				IWfNode node = (WfNode)childNode.get(i);
				
				node.setNo(i);
			}
			
			// update
			if(childNode.size() > index-1){
				StringBuffer sb = new StringBuffer();
				sb.append("update bpm_knol");
				sb.append("   set no=no-1");
				sb.append(" where parentId=?parentId");
				sb.append("   and no>=?no");
				sb.append("   and type=?type");
				
				IWfNode updateNode = sql(sb.toString());
				updateNode.setParentId(this.getId());
				updateNode.setNo(index);
				updateNode.setType(this.getType());
				
				updateNode.update();
			}			
		}
	}
	
	@Override
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
		
		if(clipboard instanceof KnowlegeNode){
			this.setDragNode((KnowlegeNode)clipboard);
			
			return this.move();
		}else{
			return null;
		}
	}
}
