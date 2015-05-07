package org.uengine.codi.mw3.model;

import java.rmi.RemoteException;

import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ToOpener;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.Database;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.springframework.stereotype.Component;
import org.uengine.kernel.GlobalContext;

@Component
public class Contact extends Database<IContact> implements IContact{
	
	public final static String DEFAULT_TOPIC_COUNT = GlobalContext.getPropertyString("contact.more.count", "9");

	@AutowiredFromClient
	public Session session;

	String friendId;
		public String getFriendId() {
			return friendId;
		}
		public void setFriendId(String friendId) {
			this.friendId = friendId;
		}

	IUser friend;
		public IUser getFriend() {
			return friend;
		}
	
		public void setFriend(IUser friend) {
			this.friend = friend;
		}
			
	String userId;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}		

	boolean checked;
		public boolean isChecked() {
			return checked;
		}
		public void setChecked(boolean checked) {
			this.checked = checked;
		}
		
	boolean followed;
		public boolean isFollowed() {
			return followed;
		}
		public void setFollowed(boolean followed) {
			this.followed = followed;
		}
	public void put() throws Exception {
		if(!this.getUserId().equals(getFriend().getUserId())){
			
			IContact contact = sql("select * from contact where userid = ?userId and friendId = ?friendId");
			contact.set("friendId", this.getFriend().getUserId());
			contact.setUserId(getUserId());
			
			contact.select();
			
			if(contact.size() == 0){
				createDatabaseMe();
				flushDatabaseMe();
				
				User.updateRecentItem(session, this.getFriend());
			}
		}
	}
	
	public void delegate() throws Exception{
		IContact contact = sql("delete from contact where userid = ?userId and friendId = ?friendId");
		contact.set("friendId", getFriend().getUserId());
		contact.setUserId(getUserId());
		contact.update();
	}
	public void editFriendName() throws Exception{
		IContact contact = sql("update contact set friendName=?friendName where userid = ?userId and friendId = ?friendId");
		contact.set("friendId", getFriend().getUserId());
		contact.set("friendName", getFriend().getName());
		contact.setUserId(getUserId());
		contact.update();
	}
	@Override
	public Object[] addFollower() throws Exception {
		User user = new User();
		user.setUserId(this.getFriendId());
		user.session = session;
		user.addFollower();
		this.setFollowed(true);
		this.getMetaworksContext().setWhere(IUser.WHERE_ADDFOLLOWER);
		
		return new Object[]{new Refresh(this, false, true)};
	}
	
	public Object[] removeFollower() throws Exception {
		User user = new User();
		user.setUserId(this.getFriendId());
		user.session = session;
		user.removeFollower();
		this.setFollowed(false);
		this.getMetaworksContext().setWhere(IUser.WHERE_ADDFOLLOWER);
		
		return new Object[]{new Refresh(this, false, true)};
	}
	
	public static int calcFriendCount(IUser user) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(c.userId) count")
		  .append("  from contact c")
		  .append("  	left join emptable e")
		  .append("    		on c.friendid = e.empcode")
		  .append(" where userId=?userId")
		  .append("   and e.isDeleted=?isDeleted");
		
 		IContact dao = (IContact)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
 													   			 sb.toString(), 
 													   			 IContact.class);
 		
 		dao.setUserId(user.getUserId());		
 		dao.set("isDeleted", "0");
 		dao.select();
 		
 		if(dao.next())
 			return dao.getInt("count");
 		else
 			return 0;
	}
	
	public static IContact findContactsWithFriendId(IUser self, String friendId) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from contact ");
		sb.append(" where  userId=?userId ");
		sb.append("  and friendId = ?friendId ");
		
		IContact dao = null;
		
		try {
	 		dao = (IContact)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
		   			 sb.toString(), 
		   			 IContact.class);

			dao.setUserId(self.getUserId());
			dao.setFriendId(friendId);
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	public static IContact findContacts(IUser user) throws Exception {
		return Contact.findContacts(user, true);
	}
	
	public static IContact findContacts(IUser user, boolean isMore) throws Exception {
		return Contact.findContacts(user, isMore, null);
	}
	public static IContact findContacts(IUser user, boolean isMore, String keyword) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct c.userId, c.friendId, c.friendName, item.updatedate")
		  .append("  from contact c ")
		  .append("  	left join emptable e")
		  .append("    		on c.friendid = e.empcode")
		  .append("  	left join recentItem item ")
		  .append("    		on item.itemId = e.empcode and item.empcode = c.userId and item.itemType=?itemType")
		  .append(" where c.userId=?userId")
		  .append("   and e.isDeleted=?isDeleted");
		
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND friendname LIKE ?friendname");
		
		sb.append(" order by updatedate desc ");
		
		if(!isMore) {
			sb.append("   limit " + GlobalContext.getPropertyString("contact.more.count", DEFAULT_TOPIC_COUNT));
		}

 		IContact dao = (IContact)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
 													   			 sb.toString(), 
 													   			 IContact.class);
 		
 		dao.setUserId(user.getUserId());		
 		dao.set("itemType", RecentItem.TYPE_FRIEND);
 		dao.set("friendName", "%" + keyword + "%");
 		dao.set("isDeleted", "0");
 		dao.select();
 		
		return dao;
	}
	
	public static String createContactSql(String countQuery) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.userId, c.friendId, ifnull(e.empname, c.friendName) friendName, IFNULL(c.mood, CONCAT(p.partname,CONCAT(IF(!isnull(p.partname) and !isnull(e.jikname), ', ', ''), e.jikname))) mood");
		sb.append(countQuery);
		sb.append("  from contact c ");
		sb.append("  left join emptable e");
		sb.append("    on c.friendid = e.empcode");
		sb.append("  LEFT JOIN parttable p");
		sb.append("    ON e.partcode=p.partcode");
		sb.append(" where c.userId=?userId");
		sb.append("   and e.isDeleted=?isDeleted");
		
		return sb.toString();
	}
	
	public static IContact findContactsForTopic(String topicId, IUser user, String keyword) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		StringBuffer countQuery = new StringBuffer();
		countQuery.append(" , (select count('x') from bpm_topicmapping t   ");
		countQuery.append("    where topicid=?topicId and t.userid=c.friendId ");
		countQuery.append("   ) as cnt ");
		
		sb.append("select contact.* , if(cnt > 0 , 1, 0 ) as followed from ( ");
		
		sb.append(Contact.createContactSql(countQuery.toString()));
		
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND friendname LIKE ?friendname");

		sb.append(" order by friendName desc ");
		
		sb.append("  ) contact ");
		
 		IContact dao = (IContact)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
 													   			 sb.toString(), 
 													   			 IContact.class);
 		
 		dao.setUserId(user.getUserId());
 		dao.set("topicId", topicId);
		dao.set("friendName", "%" + keyword + "%");
 		dao.set("itemType", RecentItem.TYPE_FRIEND);
 		dao.set("isDeleted", "0");
 		dao.select();
 		
		return dao;
	}

	public static IContact findContactsForInstance(String instanceId, IUser user, String keyword) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("select contact.* , if(cnt > 0 , 1, 0 ) as followed from ( ");
		
		StringBuffer countQuery = new StringBuffer();
		countQuery.append(" , (select count('x') from bpm_rolemapping rm   ");
		countQuery.append("    where rm.endpoint=c.friendId ");
		countQuery.append("    and rm.rootinstid=?instanceId ");
		countQuery.append("   ) as cnt ");
		
		sb.append(Contact.createContactSql(countQuery.toString()));
		
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND friendname LIKE ?friendname");

		sb.append(" order by empname ");
		
		sb.append("  ) contact ");
		
 		IContact dao = (IContact)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
 													   			 sb.toString(), 
 													   			 IContact.class);
 		
 		dao.setUserId(user.getUserId());
 		dao.set("instanceId", instanceId);	
		dao.set("friendname", "%" + keyword + "%");
 		dao.set("itemType", RecentItem.TYPE_FRIEND);
 		dao.set("isDeleted", "0");
 		dao.select();
 		
		return dao;
	}

	public static IContact findContactsForDept(String comCode, String partCode, IUser user, String keyword) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		StringBuffer countQuery = new StringBuffer();
		countQuery.append(" , (select count('x') from emptable eref   ");
		countQuery.append("    where partcode = ?partcode ");
		countQuery.append("   and empcode = c.friendid");
		countQuery.append("   ) as cnt ");
		
		sb.append("select contact.* , if(cnt > 0 , 1, 0 ) as followed from ( ");
		sb.append(Contact.createContactSql(countQuery.toString()));
//		sb.append("   and e.partcode != ?partcode");
		sb.append("   and e.globalcom = ?globalcom");
		
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND friendname LIKE ?friendname");

		sb.append(" order by empname ");
		sb.append("  ) contact ");
		
 		IContact dao = (IContact)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
 													   			 sb.toString(), 
 													   			 IContact.class);
 		
 		dao.setUserId(user.getUserId());
 		dao.set("partcode", partCode);	
		dao.set("friendname", "%" + keyword + "%");
 		dao.set("itemType", RecentItem.TYPE_FRIEND);
 		dao.set("isDeleted", "0");
 		dao.set("globalcom", comCode);
 		dao.select();
 		
		return dao;
	}
	
	public static IContact findContactsForRole(String comCode, String roleCode, IUser user, String keyword) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		StringBuffer countQuery = new StringBuffer();
		countQuery.append(" , (select count('x') from roleusertable rst   ");
		countQuery.append("    where roleCode=?roleCode and rst.empcode=c.friendId ");
		countQuery.append("   ) as cnt ");
		
		sb.append("select contact.* , if(cnt > 0 , 1, 0 ) as followed from ( ");
		
		sb.append(Contact.createContactSql(countQuery.toString()));
		sb.append("   and e.globalcom = ?globalcom");
		
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND friendname LIKE ?friendname");

		sb.append(" order by empname ");
		
		sb.append("  ) contact ");
		
 		IContact dao = (IContact)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
 													   			 sb.toString(), 
 													   			 IContact.class);
 		
 		dao.setUserId(user.getUserId());
 		dao.set("roleCode", roleCode);	
		dao.set("friendname", "%" + keyword + "%");
 		dao.set("itemType", RecentItem.TYPE_FRIEND);
 		dao.set("isDeleted", "0");
 		dao.set("globalcom", comCode);
 		dao.select();
 		
		return dao;
	}
	
	public void check(){
		this.setChecked(!this.isChecked());
	}
}

