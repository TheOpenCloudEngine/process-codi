package org.uengine.codi.mw3.model;

import javax.servlet.http.HttpServletRequest;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.TransactionContext;
import org.metaworks.widget.Window;
import org.metaworks.widget.layout.Layout;
import org.uengine.codi.mw3.admin.TopPanel;

public class SNS extends Application{

	public SNS(){
		
	}

	Layout layout;
		public Layout getLayout() {
			return layout;
		}
		public void setLayout(Layout layout) {
			this.layout = layout;
		}

	Session session;

	public static boolean isPad(){

		HttpServletRequest request = TransactionContext.getThreadLocalInstance().getRequest();

	    String userAgent = request.getHeader("User-Agent");
	    String httpAccept = request.getHeader("Accept");

        return false || (userAgent.contains("iPad"));
        
	}
	@ServiceMethod(target=ServiceMethodContext.TARGET_NONE)
	public static boolean isPhone(){
		
		HttpServletRequest request = TransactionContext.getThreadLocalInstance().getRequest();
		
	    String userAgent = request.getHeader("User-Agent");
	    String httpAccept = request.getHeader("Accept");
		
//	    
//        $user_agent = $_SERVER['HTTP_USER_AGENT']; // get the user agent value - this should be cleaned to ensure no nefarious input gets executed
//        $accept     = $_SERVER['HTTP_ACCEPT']; // get the content accept value - this should be cleaned to ensure no nefarious input gets executed
//        return false
//            || (preg_match('/ipad/i',$user_agent))
//            || (preg_match('/ipod/i',$user_agent)||preg_match('/iphone/i',$user_agent))
//            || (preg_match('/android/i',$user_agent))
//            || (preg_match('/opera mini/i',$user_agent))
//            || (preg_match('/blackberry/i',$user_agent))
//            || (preg_match('/(pre\/|palm os|palm|hiptop|avantgo|plucker|xiino|blazer|elaine)/i',$user_agent))
//            || (preg_match('/(iris|3g_t|windows ce|opera mobi|windows ce; smartphone;|windows ce; iemobile)/i',$user_agent))
//            || (preg_match('/(mini 9.5|vx1000|lge |m800|e860|u940|ux840|compal|wireless| mobi|ahong|lg380|lgku|lgu900|lg210|lg47|lg920|lg840|lg370|sam-r|mg50|s55|g83|t66|vx400|mk99|d615|d763|el370|sl900|mp500|samu3|samu4|vx10|xda_|samu5|samu6|samu7|samu9|a615|b832|m881|s920|n210|s700|c-810|_h797|mob-x|sk16d|848b|mowser|s580|r800|471x|v120|rim8|c500foma:|160x|x160|480x|x640|t503|w839|i250|sprint|w398samr810|m5252|c7100|mt126|x225|s5330|s820|htil-g1|fly v71|s302|-x113|novarra|k610i|-three|8325rc|8352rc|sanyo|vx54|c888|nx250|n120|mtk |c5588|s710|t880|c5005|i;458x|p404i|s210|c5100|teleca|s940|c500|s590|foma|samsu|vx8|vx9|a1000|_mms|myx|a700|gu1100|bc831|e300|ems100|me701|me702m-three|sd588|s800|8325rc|ac831|mw200|brew |d88|htc\/|htc_touch|355x|m50|km100|d736|p-9521|telco|sl74|ktouch|m4u\/|me702|8325rc|kddi|phone|lg |sonyericsson|samsung|240x|x320|vx10|nokia|sony cmd|motorola|up.browser|up.link|mmp|symbian|smartphone|midp|wap|vodafone|o2|pocket|kindle|mobile|psp|treo)/i',$user_agent))
//            || ((strpos($accept,'text/vnd.wap.wml')>0)||(strpos($accept,'application/vnd.wap.xhtml+xml')>0))
//            || (isset($_SERVER['HTTP_X_WAP_PROFILE'])||isset($_SERVER['HTTP_PROFILE']))
//            || (in_array(strtolower(substr($user_agent,0,4)),array('1207'=>'1207','3gso'=>'3gso','4thp'=>'4thp','501i'=>'501i','502i'=>'502i','503i'=>'503i','504i'=>'504i','505i'=>'505i','506i'=>'506i','6310'=>'6310','6590'=>'6590','770s'=>'770s','802s'=>'802s','a wa'=>'a wa','acer'=>'acer','acs-'=>'acs-','airn'=>'airn','alav'=>'alav','asus'=>'asus','attw'=>'attw','au-m'=>'au-m','aur '=>'aur ','aus '=>'aus ','abac'=>'abac','acoo'=>'acoo','aiko'=>'aiko','alco'=>'alco','alca'=>'alca','amoi'=>'amoi','anex'=>'anex','anny'=>'anny','anyw'=>'anyw','aptu'=>'aptu','arch'=>'arch','argo'=>'argo','bell'=>'bell','bird'=>'bird','bw-n'=>'bw-n','bw-u'=>'bw-u','beck'=>'beck','benq'=>'benq','bilb'=>'bilb','blac'=>'blac','c55/'=>'c55/','cdm-'=>'cdm-','chtm'=>'chtm','capi'=>'capi','cond'=>'cond','craw'=>'craw','dall'=>'dall','dbte'=>'dbte','dc-s'=>'dc-s','dica'=>'dica','ds-d'=>'ds-d','ds12'=>'ds12','dait'=>'dait','devi'=>'devi','dmob'=>'dmob','doco'=>'doco','dopo'=>'dopo','el49'=>'el49','erk0'=>'erk0','esl8'=>'esl8','ez40'=>'ez40','ez60'=>'ez60','ez70'=>'ez70','ezos'=>'ezos','ezze'=>'ezze','elai'=>'elai','emul'=>'emul','eric'=>'eric','ezwa'=>'ezwa','fake'=>'fake','fly-'=>'fly-','fly_'=>'fly_','g-mo'=>'g-mo','g1 u'=>'g1 u','g560'=>'g560','gf-5'=>'gf-5','grun'=>'grun','gene'=>'gene','go.w'=>'go.w','good'=>'good','grad'=>'grad','hcit'=>'hcit','hd-m'=>'hd-m','hd-p'=>'hd-p','hd-t'=>'hd-t','hei-'=>'hei-','hp i'=>'hp i','hpip'=>'hpip','hs-c'=>'hs-c','htc '=>'htc ','htc-'=>'htc-','htca'=>'htca','htcg'=>'htcg','htcp'=>'htcp','htcs'=>'htcs','htct'=>'htct','htc_'=>'htc_','haie'=>'haie','hita'=>'hita','huaw'=>'huaw','hutc'=>'hutc','i-20'=>'i-20','i-go'=>'i-go','i-ma'=>'i-ma','i230'=>'i230','iac'=>'iac','iac-'=>'iac-','iac/'=>'iac/','ig01'=>'ig01','im1k'=>'im1k','inno'=>'inno','iris'=>'iris','jata'=>'jata','java'=>'java','kddi'=>'kddi','kgt'=>'kgt','kgt/'=>'kgt/','kpt '=>'kpt ','kwc-'=>'kwc-','klon'=>'klon','lexi'=>'lexi','lg g'=>'lg g','lg-a'=>'lg-a','lg-b'=>'lg-b','lg-c'=>'lg-c','lg-d'=>'lg-d','lg-f'=>'lg-f','lg-g'=>'lg-g','lg-k'=>'lg-k','lg-l'=>'lg-l','lg-m'=>'lg-m','lg-o'=>'lg-o','lg-p'=>'lg-p','lg-s'=>'lg-s','lg-t'=>'lg-t','lg-u'=>'lg-u','lg-w'=>'lg-w','lg/k'=>'lg/k','lg/l'=>'lg/l','lg/u'=>'lg/u','lg50'=>'lg50','lg54'=>'lg54','lge-'=>'lge-','lge/'=>'lge/','lynx'=>'lynx','leno'=>'leno','m1-w'=>'m1-w','m3ga'=>'m3ga','m50/'=>'m50/','maui'=>'maui','mc01'=>'mc01','mc21'=>'mc21','mcca'=>'mcca','medi'=>'medi','meri'=>'meri','mio8'=>'mio8','mioa'=>'mioa','mo01'=>'mo01','mo02'=>'mo02','mode'=>'mode','modo'=>'modo','mot '=>'mot ','mot-'=>'mot-','mt50'=>'mt50','mtp1'=>'mtp1','mtv '=>'mtv ','mate'=>'mate','maxo'=>'maxo','merc'=>'merc','mits'=>'mits','mobi'=>'mobi','motv'=>'motv','mozz'=>'mozz','n100'=>'n100','n101'=>'n101','n102'=>'n102','n202'=>'n202','n203'=>'n203','n300'=>'n300','n302'=>'n302','n500'=>'n500','n502'=>'n502','n505'=>'n505','n700'=>'n700','n701'=>'n701','n710'=>'n710','nec-'=>'nec-','nem-'=>'nem-','newg'=>'newg','neon'=>'neon','netf'=>'netf','noki'=>'noki','nzph'=>'nzph','o2 x'=>'o2 x','o2-x'=>'o2-x','opwv'=>'opwv','owg1'=>'owg1','opti'=>'opti','oran'=>'oran','p800'=>'p800','pand'=>'pand','pg-1'=>'pg-1','pg-2'=>'pg-2','pg-3'=>'pg-3','pg-6'=>'pg-6','pg-8'=>'pg-8','pg-c'=>'pg-c','pg13'=>'pg13','phil'=>'phil','pn-2'=>'pn-2','pt-g'=>'pt-g','palm'=>'palm','pana'=>'pana','pire'=>'pire','pock'=>'pock','pose'=>'pose','psio'=>'psio','qa-a'=>'qa-a','qc-2'=>'qc-2','qc-3'=>'qc-3','qc-5'=>'qc-5','qc-7'=>'qc-7','qc07'=>'qc07','qc12'=>'qc12','qc21'=>'qc21','qc32'=>'qc32','qc60'=>'qc60','qci-'=>'qci-','qwap'=>'qwap','qtek'=>'qtek','r380'=>'r380','r600'=>'r600','raks'=>'raks','rim9'=>'rim9','rove'=>'rove','s55/'=>'s55/','sage'=>'sage','sams'=>'sams','sc01'=>'sc01','sch-'=>'sch-','scp-'=>'scp-','sdk/'=>'sdk/','se47'=>'se47','sec-'=>'sec-','sec0'=>'sec0','sec1'=>'sec1','semc'=>'semc','sgh-'=>'sgh-','shar'=>'shar','sie-'=>'sie-','sk-0'=>'sk-0','sl45'=>'sl45','slid'=>'slid','smb3'=>'smb3','smt5'=>'smt5','sp01'=>'sp01','sph-'=>'sph-','spv '=>'spv ','spv-'=>'spv-','sy01'=>'sy01','samm'=>'samm','sany'=>'sany','sava'=>'sava','scoo'=>'scoo','send'=>'send','siem'=>'siem','smar'=>'smar','smit'=>'smit','soft'=>'soft','sony'=>'sony','t-mo'=>'t-mo','t218'=>'t218','t250'=>'t250','t600'=>'t600','t610'=>'t610','t618'=>'t618','tcl-'=>'tcl-','tdg-'=>'tdg-','telm'=>'telm','tim-'=>'tim-','ts70'=>'ts70','tsm-'=>'tsm-','tsm3'=>'tsm3','tsm5'=>'tsm5','tx-9'=>'tx-9','tagt'=>'tagt','talk'=>'talk','teli'=>'teli','topl'=>'topl','hiba'=>'hiba','up.b'=>'up.b','upg1'=>'upg1','utst'=>'utst','v400'=>'v400','v750'=>'v750','veri'=>'veri','vk-v'=>'vk-v','vk40'=>'vk40','vk50'=>'vk50','vk52'=>'vk52','vk53'=>'vk53','vm40'=>'vm40','vx98'=>'vx98','virg'=>'virg','vite'=>'vite','voda'=>'voda','vulc'=>'vulc','w3c '=>'w3c ','w3c-'=>'w3c-','wapj'=>'wapj','wapp'=>'wapp','wapu'=>'wapu','wapm'=>'wapm','wig '=>'wig ','wapi'=>'wapi','wapr'=>'wapr','wapv'=>'wapv','wapy'=>'wapy','wapa'=>'wapa','waps'=>'waps','wapt'=>'wapt','winc'=>'winc','winw'=>'winw','wonu'=>'wonu','x700'=>'x700','xda2'=>'xda2','xdag'=>'xdag','yas-'=>'yas-','your'=>'your','zte-'=>'zte-','zeto'=>'zeto','acs-'=>'acs-','alav'=>'alav','alca'=>'alca','amoi'=>'amoi','aste'=>'aste','audi'=>'audi','avan'=>'avan','benq'=>'benq','bird'=>'bird','blac'=>'blac','blaz'=>'blaz','brew'=>'brew','brvw'=>'brvw','bumb'=>'bumb','ccwa'=>'ccwa','cell'=>'cell','cldc'=>'cldc','cmd-'=>'cmd-','dang'=>'dang','doco'=>'doco','eml2'=>'eml2','eric'=>'eric','fetc'=>'fetc','hipt'=>'hipt','http'=>'http','ibro'=>'ibro','idea'=>'idea','ikom'=>'ikom','inno'=>'inno','ipaq'=>'ipaq','jbro'=>'jbro','jemu'=>'jemu','java'=>'java','jigs'=>'jigs','kddi'=>'kddi','keji'=>'keji','kyoc'=>'kyoc','kyok'=>'kyok','leno'=>'leno','lg-c'=>'lg-c','lg-d'=>'lg-d','lg-g'=>'lg-g','lge-'=>'lge-','libw'=>'libw','m-cr'=>'m-cr','maui'=>'maui','maxo'=>'maxo','midp'=>'midp','mits'=>'mits','mmef'=>'mmef','mobi'=>'mobi','mot-'=>'mot-','moto'=>'moto','mwbp'=>'mwbp','mywa'=>'mywa','nec-'=>'nec-','newt'=>'newt','nok6'=>'nok6','noki'=>'noki','o2im'=>'o2im','opwv'=>'opwv','palm'=>'palm','pana'=>'pana','pant'=>'pant','pdxg'=>'pdxg','phil'=>'phil','play'=>'play','pluc'=>'pluc','port'=>'port','prox'=>'prox','qtek'=>'qtek','qwap'=>'qwap','rozo'=>'rozo','sage'=>'sage','sama'=>'sama','sams'=>'sams','sany'=>'sany','sch-'=>'sch-','sec-'=>'sec-','send'=>'send','seri'=>'seri','sgh-'=>'sgh-','shar'=>'shar','sie-'=>'sie-','siem'=>'siem','smal'=>'smal','smar'=>'smar','sony'=>'sony','sph-'=>'sph-','symb'=>'symb','t-mo'=>'t-mo','teli'=>'teli','tim-'=>'tim-','tosh'=>'tosh','treo'=>'treo','tsm-'=>'tsm-','upg1'=>'upg1','upsi'=>'upsi','vk-v'=>'vk-v','voda'=>'voda','vx52'=>'vx52','vx53'=>'vx53','vx60'=>'vx60','vx61'=>'vx61','vx70'=>'vx70','vx80'=>'vx80','vx81'=>'vx81','vx83'=>'vx83','vx85'=>'vx85','wap-'=>'wap-','wapa'=>'wapa','wapi'=>'wapi','wapp'=>'wapp','wapr'=>'wapr','webc'=>'webc','whit'=>'whit','winw'=>'winw','wmlb'=>'wmlb','xda-'=>'xda-',)))
//        ;
        return false
            || (userAgent.contains("iPod")) || userAgent.contains("iPhone")
            || (userAgent.contains("Android"))
        ;
	}
	
	
	public SNS(Session session) throws Exception {
		this(session, null);
	}
	
	public SNS(Session session, String instId) throws Exception {
		this(session, instId, null);
	}
			
	public SNS(Session session, String instId, String topicId) throws Exception {
		
		String preferUX = session.getEmployee().getPreferUX();
		String preferMob = session.getEmployee().getPreferMob();

		boolean pad = isPad();
		boolean phone = isPhone();
		boolean isMobile = pad || phone;
		

		if(isMobile && (preferMob==null || "auto".equals(preferMob)) ){
			preferMob = (pad ? "pad" : "phone");
			
		}
		
		TopPanel topPanel = new TopPanel(session);
		if("phone".equals(preferMob) || "tw".equals(preferUX)){
			session.setUx("phone");
			
			Layout outerLayout = new Layout();
			outerLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, west__spacing_open:5, west__size:150, north__size:52");
			
			Window instanceListWindow = createInstanceListWindow(session);
			
			Layout eastLayout = new Layout();
			eastLayout.setCenter(instanceListWindow);	
			eastLayout.setName("east");
			eastLayout.setUseHideBar(false);

			outerLayout.setCenter(eastLayout);
			setLayout(outerLayout);
			
			
			
		}else if("pad".equals(preferMob) || "fb".equals(preferUX)  || "asana".equals(preferUX)){
			session.setUx("pad");
			
			Layout outerLayout = new Layout();
			outerLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, west__spacing_open:5, north__size:52, west__size:" + ("asana".equals(preferUX) ? "700" : "400"));
			outerLayout.setNorth(topPanel);			
			outerLayout.setWest(createInstanceListWindow(session));
			outerLayout.setCenter(createNewInstancePanel(session));	
			outerLayout.setName("center");
			
			setLayout(outerLayout);

		}else{

		
			Layout westLayout = new Layout();
			
//			ContactWindow contactWindow = new ContactWindow(session.getUser());
			
			PerspectiveWindow perspectiveWindow = new PerspectiveWindow(session);
			
			ContentWindow contentWindow = new ContentWindow(); 
			
			if(topicId != null){
				session.setLastPerspecteType("project");
				session.setLastSelectedItem(topicId);
					
			}if(instId == null)
				contentWindow = createNewInstancePanel(session);
			else{
				Instance instance = new Instance();
				instance.instanceViewContent = new InstanceViewContent();
				instance.session = session;
						
				instance.setInstId(new Long(instId));

				contentWindow = (InstanceViewContent)instance.detail();
			}
			
			westLayout.setCenter(perspectiveWindow);
			westLayout.setUseHideBar(true);
			westLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, south__spacing_open:5, south__size:'50%'");
			westLayout.setName("west");
			
			//Since there's are too many input boxes there, it is removed.
//			if(session.getEmployee().isApproved()){
//				contactWindow.getContactPanel().setSearchBox(null);
//				contactWindow.getContactPanel().setUser(null);
//				westLayout.setSouth(contactWindow);
//			}
			
			Layout eastLayout = new Layout();
			eastLayout.setWest(createInstanceListWindow(session));
			eastLayout.setCenter(contentWindow);
			eastLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, south__spacing_open:5, west__spacing_open:5, west__size:'40%'");
			eastLayout.setName("east");
			eastLayout.setUseHideBar(false);
			
			Layout outerLayout = new Layout();
			outerLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, west__spacing_open:1, north__size:52, west__size: 168");
			
			outerLayout.setWest(westLayout);
			outerLayout.setCenter(eastLayout);		
			outerLayout.setName("center");
			outerLayout.setUseHideBar(false);
			
			setLayout(outerLayout);
		
			//향후 변경해야 할것 으로 보입니다.
			
			/*
			if("oce".equals(session.getUx()))
				setPageNavigator(new OcePageNavigator());
			else if("sns".equals(session.getUx()))
				setPageNavigator(new OcePageNavigator());
			else if("codi_8080".equals(GlobalContext.getPropertyString("metadataKey", "codi")))
				setPageNavigator(null);
			else
				setPageNavigator(new PageNavigator());
			*/
			
		}

	}

	private ContentWindow createNewInstancePanel(Session session)
			throws Exception {
		ContentWindow contentWindow = new ContentWindow();
		contentWindow.setTitle("$New");
		
		NewInstancePanel instancePanel = new NewInstancePanel();
		instancePanel.session = session;
		instancePanel.load(session);
		contentWindow.setPanel(instancePanel);
		
//		contentWindow.setPanel(instancePanel);
		
//		Grid grid = new Grid();
//		grid.setColumnNames(new String[]{"Name", "Age", "Address"});
		//grid.set
		
		
		
//		contentWindow.setPanel(new IFrame("http://www.naver.com"));
		return contentWindow;
	}

	static public ListWindow createInstanceListWindow(Session session) throws Exception {
		
//		PersonalPerspective personalPerspective = new PersonalPerspective();
//		personalPerspective.session = session;
//		
//		ListWindow listWindow = new ListWindow();
//		listWindow.setPanel(personalPerspective.loadAllICanSee());
		
		InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_PERSONAL, Perspective.TYPE_NEWSFEED, session.getUser().getUserId());

		ListPanel listPanel = new ListPanel();
		listPanel.setPerspectiveInfo(new PerspectiveInfo(session, Perspective.TYPE_NEWSFEED));
		listPanel.setInstanceListPanel(instanceListPanel);
		
		ListWindow listWindow = new ListWindow();
		listWindow.setPanel(listPanel);
		
		return listWindow;
	}
}

