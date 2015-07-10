package org.uengine.codi.mw3;

import com.thoughtworks.xstream.XStream;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.metaworks.*;
import org.metaworks.Forward;
import org.metaworks.annotation.*;
import org.metaworks.annotation.Face;
import org.metaworks.common.MetaworksUtil;
import org.metaworks.dao.TransactionContext;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.admin.TopPanel;
import org.uengine.codi.mw3.collection.SessionIdHashTable;
import org.uengine.codi.mw3.common.MainPanel;
import org.uengine.codi.mw3.email.factory.EmailSenderFactory;
import org.uengine.codi.mw3.email.sender.IEmailSender;
import org.uengine.codi.mw3.email.type.EmailSenderType;
import org.uengine.codi.mw3.email.util.EmailSenderUtil;
import org.uengine.codi.mw3.model.Application;
import org.uengine.codi.mw3.model.*;
import org.uengine.codi.mw3.model.Locale;
import org.uengine.kernel.GlobalContext;
import org.uengine.sso.BaseAuthenticate;
import org.uengine.sso.CasAuthenticate;
import org.uengine.util.UEngineUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class Login implements ContextAware {

    public static final String CACHE_PATH = GlobalContext.getPropertyString("cache");

    protected static Hashtable<String, HashMap<String, String>> SessionIdForCompanyMapping = new SessionIdHashTable<String, HashMap<String, String>>();
    protected static Hashtable<String, HashMap<String, String>> SessionIdForDeptMapping = new SessionIdHashTable<String, HashMap<String, String>>();
    protected static Hashtable<String, String> SessionIdForEmployeeMapping = new SessionIdHashTable<String, String>();

    protected static Hashtable<String, String> userIdDeviceMapping = new SessionIdHashTable<String, String>();
    @AutowiredFromClient
    public Locale localeManager;
    MetaworksContext metaworksContext;
    String status;
    String email;
    String password;
    Boolean rememberMe;
    boolean useSubscribe;
    String lastVisitPage;
    String lastVisitValue;
    String ssoService;
    boolean forOCE;
    String companyName;

    public Login() {
        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
        getMetaworksContext().setHow("login");

        this.setUseSubscribe("1".equals(StartCodi.USE_SIGNUP));

        setForOCE("true".equals(GlobalContext.getPropertyString("forOCE", "false")));

    }

    public static String getSessionIdWithUserId(String userId) {
        return SessionIdForEmployeeMapping.get(userId.toUpperCase());
    }

    public static HashMap<String, String> getSessionIdWithDept(String deptId) {
        deptId = deptId.toUpperCase();
        if (SessionIdForDeptMapping.containsKey(deptId)) {
            HashMap<String, String> mapping = SessionIdForDeptMapping.get(deptId);
            //System.out.println(.);

            Iterator<String> iterator = mapping.keySet().iterator();

            return (HashMap<String, String>) mapping.clone();
        } else {
            return null;
        }
    }

    public static HashMap<String, String> getSessionIdWithCompany(String companyId) {
        companyId = companyId.toUpperCase();
        if (SessionIdForCompanyMapping.containsKey(companyId)) {
            HashMap<String, String> mapping = SessionIdForCompanyMapping.get(companyId);
            //System.out.println(.);

            Iterator<String> iterator = mapping.keySet().iterator();

            return (HashMap<String, String>) mapping.clone();
        } else {
            return null;
        }
    }

    public static String getDeviceWithUserId(String userId) {
        return userIdDeviceMapping.get(userId.toUpperCase());
    }

    /*
        서버를 종료시 세션정보를 캐시 파일로 저장한다.
     */
    public static void storeLoginStaticSessionInfo() throws Exception {
        HashMap<String, Hashtable> sessionMap = new HashMap<>();
        sessionMap.put("SessionIdForCompanyMapping", SessionIdForCompanyMapping);
        sessionMap.put("SessionIdForDeptMapping", SessionIdForDeptMapping);
        sessionMap.put("SessionIdForEmployeeMapping", SessionIdForEmployeeMapping);
        sessionMap.put("userIdDeviceMapping", userIdDeviceMapping);

        XStream stream = new XStream();
        stream.alias("root", HashMap.class);
        String xml = stream.toXML(sessionMap);

        OutputStream fos = null;
        ByteArrayInputStream bai = null;

        try {
            fos = new FileOutputStream(new File(CACHE_PATH + File.separator + Login.class.getName()));
            bai = new ByteArrayInputStream(xml.getBytes(GlobalContext.ENCODING));
            UEngineUtil.copyStream(bai, fos);
        } catch (FileNotFoundException e) {
            throw new Exception("Login Session Serializable File Not Found.");
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Unsupported Encoding Exception.");
        } catch (Exception e) {
            throw new Exception("UEngineUtil Copy Stream Failed.");
        }
    }

    /*
       서버를 시작시 세션정보를 캐시 파일로부터 위의 static변수에 넣는다.
    */
    public static void recoverLoginStaticSessionInfo() throws Exception {
        InputStream is = null;
        ByteArrayOutputStream bao = null;
        File loginSessionFile = null;

        try {
            loginSessionFile = new File(CACHE_PATH + File.separator + Login.class.getName());
            if (!loginSessionFile.exists())
                return;

            is = new FileInputStream(loginSessionFile);
            bao = new ByteArrayOutputStream();
            MetaworksUtil.copyStream(is, bao);
        } catch (FileNotFoundException e) {
            throw new Exception("Login Session Deserializable File Not Found.");
        } catch (Exception e) {
            throw new Exception("MetaworksUtil Copy Stream Failed.");
        }

        String xml = bao.toString(GlobalContext.ENCODING);
        XStream stream = new XStream();
        stream.alias("root", HashMap.class);

        HashMap<String, Hashtable> sessionMap = (HashMap<String, Hashtable>) stream.fromXML(xml);

        SessionIdForCompanyMapping = sessionMap.get("SessionIdForCompanyMapping");
        SessionIdForDeptMapping = sessionMap.get("SessionIdForDeptMapping");
        SessionIdForEmployeeMapping = sessionMap.get("SessionIdForEmployeeMapping");
        userIdDeviceMapping = sessionMap.get("userIdDeviceMapping");

        loginSessionFile.delete();
    }

    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }

    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    @Id
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Face(displayName = "$Email")
    @ValidatorSet({
            @Validator(name = ValidatorContext.VALIDATE_NOTNULL, message = "이메일을 입력하세요."),
            @Validator(name = ValidatorContext.VALIDATE_REGULAREXPRESSION, options = {"/^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$/"}, message = "이메일 형식이 잘못되었습니다")
    })
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Face(options = {"type", "placeholder"}, values = {"password", "Password"})
    @Validator(name = ValidatorContext.VALIDATE_NOTNULL, condition = "metaworksContext.how == 'login'", message = "비밀번호를 입력해 주세요.")
    @Hidden(when = MetaworksContext.WHEN_VIEW)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean isUseSubscribe() {
        return useSubscribe;
    }

    public void setUseSubscribe(boolean useSubscribe) {
        this.useSubscribe = useSubscribe;
    }

    public String getLastVisitPage() {
        return lastVisitPage;
    }

    public void setLastVisitPage(String lastVisitPage) {
        this.lastVisitPage = lastVisitPage;
    }

    public String getLastVisitValue() {
        return lastVisitValue;
    }

    public void setLastVisitValue(String lastVisitValue) {
        this.lastVisitValue = lastVisitValue;
    }

    public String getSsoService() {
        return ssoService;
    }

    public void setSsoService(String ssoService) {
        this.ssoService = ssoService;
    }

    public boolean isForOCE() {
        return forOCE;
    }

    public void setForOCE(boolean forOCE) {
        this.forOCE = forOCE;
    }

    //	private Object createMainPageByLoginType(Session session) {
    //		if (getMetaworksContext().getWhen().equals(MetaworksContext.WHEN_VIEW)) {
    //			if (getMetaworksContext().getWhere().equals(LOGINTYPE_WHERE_CLIENT)) {
    //				// TODO session and wih page on client
    ////					ClientMainPage mainPage = new ClientMainPage();
    ////					mainPage.setSession(session);
    ////					mainPage = mainPage.init();
    ////					return mainPage;
    //				return this;
    //			} else if (getMetaworksContext().getWhere().equals(
    //					LOGINTYPE_WHERE_USER) || getMetaworksContext().getWhere().equals(
    //							LOGINTYPE_WHERE_ADMIN)) {
    //				MainPage mainPage = new MainPage(session);
    //				return mainPage;
    //			} else {
    //				return this;
    //			}
    //		} else {
    //			return this;
    //		}
    //	}
    //
    //	private void createContextInfoByUserType(Session session) {
    //		if (session.getEmployee().getIsAdmin()) {
    //			getMetaworksContext().setWhere(LOGINTYPE_WHERE_ADMIN);
    //		} else {
    //			if (session.getEmployee().getPartCode() != null
    //					&& !session.getEmployee().getPartCode().equals(
    //							IDept.CUSTOMER_DEPT_PARTCODE)) {
    //				getMetaworksContext().setWhere(LOGINTYPE_WHERE_USER);
    //			} else if (session.getEmployee().getPartCode() != null
    //					&& session.getEmployee().getPartCode().equals(
    //							IDept.CUSTOMER_DEPT_PARTCODE)) {
    //				getMetaworksContext().setWhere(LOGINTYPE_WHERE_CLIENT);
    //			} else {
    //				getMetaworksContext().setWhere(LOGINTYPE_WHERE_OTHERS);
    //			}
    //		}
    //
    //	}

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    //	private Object createMainPageByLoginType(Session session) {
    //		if (getMetaworksContext().getWhen().equals(MetaworksContext.WHEN_VIEW)) {
    //			if (getMetaworksContext().getWhere().equals(LOGINTYPE_WHERE_CLIENT)) {
    //				// TODO session and wih page on client
    ////					ClientMainPage mainPage = new ClientMainPage();
    ////					mainPage.setSession(session);
    ////					mainPage = mainPage.init();
    ////					return mainPage;
    //				return this;
    //			} else if (getMetaworksContext().getWhere().equals(
    //					LOGINTYPE_WHERE_USER) || getMetaworksContext().getWhere().equals(
    //							LOGINTYPE_WHERE_ADMIN)) {
    //				MainPage mainPage = new MainPage(session);
    //				return mainPage;
    //			} else {
    //				return this;
    //			}
    //		} else {
    //			return this;
    //		}
    //	}
    //
    //	private void createContextInfoByUserType(Session session) {
    //		if (session.getEmployee().getIsAdmin()) {
    //			getMetaworksContext().setWhere(LOGINTYPE_WHERE_ADMIN);
    //		} else {
    //			if (session.getEmployee().getPartCode() != null
    //					&& !session.getEmployee().getPartCode().equals(
    //							IDept.CUSTOMER_DEPT_PARTCODE)) {
    //				getMetaworksContext().setWhere(LOGINTYPE_WHERE_USER);
    //			} else if (session.getEmployee().getPartCode() != null
    //					&& session.getEmployee().getPartCode().equals(
    //							IDept.CUSTOMER_DEPT_PARTCODE)) {
    //				getMetaworksContext().setWhere(LOGINTYPE_WHERE_CLIENT);
    //			} else {
    //				getMetaworksContext().setWhere(LOGINTYPE_WHERE_OTHERS);
    //			}
    //		}
    //
    //	}

    public Session loginService() throws Exception {

        Session session = new Session();

        Employee emp = new Employee();
        emp.setEmail(getEmail());
        IEmployee findEmp = emp.findByEmail();

        if (findEmp == null)
            throw new Exception("<font color=blue>Wrong User or Password! forgot?</font>");

        if ("1".equals(StartCodi.USE_CAS)) {
            if (!loginSso(session))
                throw new Exception("<font color=blue>Wrong User or Password! forgot?</font>");
        } else {
            if (!getPassword().equals(findEmp.getPassword()))
                throw new Exception("<font color=blue>Wrong User or Password! forgot?</font>");
        }

        session.setEmployee(findEmp);

        return session;
    }

    public boolean loginSso(Session session) {

        String serviceURL = null;
        boolean bCodi = false;
        HttpSession httpsession = TransactionContext.getThreadLocalInstance().getRequest().getSession();

        if (getSsoService() != null) {
            serviceURL = getSsoService();
        } else {
            serviceURL = "http://" + TransactionContext.getThreadLocalInstance().getRequest().getLocalAddr().toString()
                    + ":" + TransactionContext.getThreadLocalInstance().getRequest().getLocalPort()
                    + TransactionContext.getThreadLocalInstance().getRequest().getContextPath();
            bCodi = true;
        }

        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("ssoService", serviceURL);
            params.put("username", getEmail());
            params.put("password", getPassword());

            BaseAuthenticate ssoAuth = new CasAuthenticate();
            String strTgt = ssoAuth.authorize(params);

            if (strTgt != null) {
                httpsession.setAttribute("SSO-TGT", strTgt);
                StartCodi.MANAGED_SESSIONS.put(strTgt, session);
                if (!bCodi) {
                    String ticket = ssoAuth.serviceTicket(strTgt, serviceURL);
                    setSsoService(getSsoService() + "?ticket=" + ticket);
                }
                return true;
            } else {
                return false;
            }


//			// tgt 티켓 발행
//			HashMap<String, String> params = new HashMap<String,String>();
//			params.put("username", getEmail());
//			params.put("password", getPassword());
//			HashMap mapResult = codiHc.sendMessageToEndPoint(StartCodi.CAS_REST_URL, params, "POST");
//			if(mapResult != null){
//				String strTgt = (String)mapResult.get(codiHc.RESULT_KEY);
//				strTgt = strTgt.substring( strTgt.lastIndexOf("/") +1);
//				System.out.println("Tgt is : " + strTgt);
//
//				// st 티켓 발행
//				params = new HashMap<String,String>();
//				params.put("service", serviceURL);
//				String myURL = StartCodi.CAS_REST_URL + "/"+ strTgt ;
//				mapResult = codiHc.sendMessageToEndPoint(myURL, params, "POST");
//				String strSt = (String)mapResult.get(codiHc.RESULT_KEY);
//				if(!bCodi){
//					setSsoService(getSsoService()+"?ticket=" + strSt);
//				}else{
//					httpsession.setAttribute("SSO-ST", strSt);
//					StartCodi.MANAGED_SESSIONS.put(strSt, session);
//				}
//
//				// 쿠키저장
//				CookieGenerator cookieGenerator = new CookieGenerator();
//				cookieGenerator.setCookieSecure(false);
//				cookieGenerator.setCookieMaxAge(7889231);
//				cookieGenerator.setCookieName("CASTGC");
//				cookieGenerator.addCookie(TransactionContext.getThreadLocalInstance().getResponse(), strTgt);
//
//				return true;
//			}else{
//				return false;
//			}

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return false;
    }

    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_APPEND)
    public Object popupSubscribe1() throws Exception {

        this.setStatus("subscribe");

        ModalWindow window = new ModalWindow(this, 600, 380, "Sign Up");
        window.setId("subscribe");
        window.setResizable(false);

        return window;
    }

    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_SELF)
    public Object popupSubscribe() throws Exception {
        return new SignUp();
    }

    @ServiceMethod(payload={"userId"}, target=ServiceMethodContext.TARGET_SELF)
    public void goLogin() throws Exception {
        this.getMetaworksContext().setHow("login");
    }

    @ServiceMethod(payload={"email"}, target=ServiceMethodContext.TARGET_SELF)
    public void goSignUp() throws Exception {
        this.setStatus("signup");
        this.getMetaworksContext().setHow("signup");
    }

    @ServiceMethod(callByContent=true, payload={"email"}, validate=true, target=ServiceMethodContext.TARGET_STICK)
    public Object signUpForMain() throws Exception {
        return signUp();
    }

    @ServiceMethod(callByContent=true, payload={"email"}, validate=true, target=ServiceMethodContext.TARGET_SELF)
    public Object signUp() throws Exception {
        SignUp signUp = new SignUp();
        IEmployee employee = signUp.signUp(getEmail(), false);
        /*
        SignUpConfirm confirm = new SignUpConfirm();
		confirm.setEmail(this.getEmail());
		confirm.setUrl(activateURL);
		return confirm;
		 */
        this.getMetaworksContext().setHow("aftersignup");
        this.getMetaworksContext().setWhere("popup");
        return this;
    }

    @ServiceMethod(callByContent=true, payload={"email"}, validate=true, target=ServiceMethodContext.TARGET_STICK)
    public Object firstSignUp() throws Exception {
        SignUp signUp = new SignUp();
        IEmployee employee = signUp.signUp(getEmail(), true);
        /*
        SignUpConfirm confirm = new SignUpConfirm();
		confirm.setEmail(this.getEmail());
		confirm.setUrl(activateURL);
		return confirm;
		 */
        this.getMetaworksContext().setHow("aftersignup");
        this.getMetaworksContext().setWhere("popup");
        return this;
    }

    @ServiceMethod(payload={"userId"}, target=ServiceMethodContext.TARGET_NONE)
    public boolean checkAuthSocial() {
        if (this.getEmail() == null || this.getEmail().length() == 0)
            return false;

        Employee employee = new Employee();
        employee.setEmpCode(this.getEmail());

        IEmployee employeeRef = null;

        try {
            employeeRef = employee.databaseMe();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeeRef != null;
    }

    @ServiceMethod(payload={"userId", "facebookSSO"}, target=ServiceMethodContext.TARGET_NONE)
    public Session makeSession() throws Exception {
        return loginService();
    }

    @Test(scenario = "first", starter = true, instruction = "Welcome! If you have account, sign in please... or sign up for your new account.", next = "autowiredObject.org.uengine.codi.mw3.model.InstanceListPanel.newInstance()")
    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_APPEND, validate = true)
    public Object[] login() throws Exception {

		/*
    Company company = new Company();
		company.setComCode(findEmp.getGlobalCom());
		ICompany findcompany = company.findByCode();
		 */

        //		if(!findcompany.getAlias().equals(TenantContext.getThreadLocalInstance().getTenantId())){
        //			throw new Exception("");
        //		}

        Session session = loginService();

        return login(session);

		/*
    MainPanel mainPanel=null;/*
		PageNavigator pageNavigator = new PageNavigator();
		pageNavigator.setSession(session);

		if("knowledge".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("knowledge.use", "0"))){
			mainPanel = pageNavigator.goKnowledge();
		}else if("pinterest".equals(lastVisitPage)){
			mainPanel = pageNavigator.goPinterest();

		}else if("ide".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("ide.use", "1"))){
			mainPanel = pageNavigator.goIDE();
		}else if("marketplace".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("marketplace.use", "1"))){
			mainPanel = pageNavigator.goMarketplace();
		}else if("selfservice".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("selfservice.use", "1"))){
			mainPanel = pageNavigator.goSelfServicePortal();
		}else{
			String preferUX = session.getEmployee().getPreferUX();
			if("sns".equals(preferUX) || "".equals(preferUX)){
				mainPanel = pageNavigator.goSns();
			}else{
				mainPanel = pageNavigator.goProcess();
			}
		}
		PageNavigator pageNavigator = null;
		String pageNavigatorPropertyName="";
		String className = null;

		if("1".equals(GlobalContext.getPropertyString("oce.use", "1"))){
			pageNavigatorPropertyName = GlobalContext.getPropertyStringArray("oce.pagenavigator.class","org.uengine.codi.mw3.admin.OcePageNavigator");
			session.setUx("oce");
		}
		else{
			pageNavigatorPropertyName = GlobalContext.getPropertyStringArray("codi.pagenavigator.class","org.uengine.codi.mw3.admin.PageNavigator");
		}

		className = GlobalContext.getPropertyString(pageNavigatorPropertyName);

		Class c = Thread.currentThread().getContextClassLoader().loadClass(GlobalContext.getPropertyString(pageNavigatorPropertyName));
		Object object = c.newInstance();

		if(object instanceof PageNavigator){
			pageNavigator = (PageNavigator)object;
		}
		else{
			throw new Exception("pageNavigator가 잘못 지정되었습니다. uengine.properties의 pagenavigatorClassName을 수정해주세요.");
		}

		pageNavigator.session = session;

		if("oce".equals(session.getUx())){
			mainPanel = pageNavigator.goDashBoard();
		}else{
			if("knowledge".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("knowledge.use", "0"))){
				mainPanel = pageNavigator.goKnowledge();
			}else if("pinterest".equals(lastVisitPage)){
				mainPanel = pageNavigator.goPinterest();
			}else if("ide".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("ide.use", "1"))){
				mainPanel = pageNavigator.goIDE();
			}else if("marketplace".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("marketplace.use", "1"))){
				mainPanel = pageNavigator.goMarketplace();
			}else if("selfservice".equals(lastVisitPage) && "1".equals(GlobalContext.getPropertyString("selfservice.use", "1"))){
				mainPanel = pageNavigator.goSelfServicePortal();
			}else{
				String preferUX = session.getEmployee().getPreferUX();
				if("sns".equals(preferUX) || "".equals(preferUX)){
					mainPanel = pageNavigator.goSns();
				}else{
					mainPanel = pageNavigator.goProcess();
				}
			}
		}

		if("1".equals(GlobalContext.getPropertyString("sso.use", "1"))){
			//Request Token
			OAuthBasic oauth = new OAuthBasic();
			oauth.requestToken();

			// Request Access Token
			String accessToken = oauth.retrieveAccessToken("");
			System.out.println("accessToken  = " + accessToken);

			session.setAccessToken(accessToken);

			//user id, access token save to cubrid
			OAuthDB oauthDB = new OAuthDB();
			oauthDB.update(session.getUser().getUserId(), session.getAccessToken());
		}
		 */

		/*
    HttpServletRequest httpServletRequest = TransactionContext.getThreadLocalInstance().getRequest();

		String ipAddress = httpServletRequest.getRemoteAddr();
		String hostAddress = httpServletRequest.getRemoteHost();

        InetAddress local = InetAddress.getLocalHost();

		System.out.println("-------- client 접속 ------------------");
		System.out.println("Client 명 : "+hostAddress);
		System.out.println("Client Ip : "+ipAddress);
		System.out.println();
        System.out.println("서버 pc 명: " + local.getHostName());
        System.out.println("서버    IP: " + local.getHostAddress());
        System.out.println("---------------------------------------");


        CodiLog  log = new CodiLog();
        log.setId(log.createNewId());
        log.setEmpcode(findEmp.getEmpCode());
        log.setComCode(findEmp.getGlobalCom());
        log.setType("login");
        log.setDate(new Date());
        log.setIp(ipAddress);
        log.createDatabaseMe();
		 */

        //new Remover(new ModalWindow(), true),

    }

    public Object[] login(Session session) throws Exception {
        session.fillSession();
        session.fillUserInfoToHttpSession();

        storeIntoServerSession(session);


        HttpServletRequest httpServletRequest = TransactionContext.getThreadLocalInstance().getRequest();

        String ipAddress = httpServletRequest.getRemoteAddr();

        CodiLog log = new CodiLog();
        log.setId(log.createNewId());
        log.setEmpcode(session.getEmployee().getEmpCode());
        log.setComCode(session.getEmployee().getGlobalCom());
        log.setType("login");
        log.setDate(new Date());
        log.setIp(ipAddress);
        log.createDatabaseMe();


        MainPanel mainPanel;

        //PageNavigator pageNavigator = new PageNavigator();
        //pageNavigator.session = session;


		/*
    if("1".equals(StartCodi.USE_OCE)){
			mainPanel =  pageNavigator.goDashBoard();
		}else{
			mainPanel = pageNavigator.goProcess();
		}
		 */

        Application app = null;


        if (app == null)
            app = new SNS(session);

        TopPanel topPanel = new TopPanel(session);

        if (!SNS.isPhone())
            topPanel.setTopCenterPanel(app.loadTopCenterPanel(session));

        mainPanel = new MainPanel(topPanel, app);

        if (SNS.isPhone()) {
            mainPanel.setPerspectivePanel(new PerspectivePanel(session));
        }

        Locale locale = new Locale();
        locale.setLanguage(session.getEmployee().getLocale());
        locale.load();


        System.out.println(getSsoService() + "   <====================================================  ");

        if (getSsoService() != null)
            return new Object[]{new Forward(getSsoService())};

        return new Object[]{new Refresh(session), new Refresh(locale), new Refresh(mainPanel, false, true)};
    }

  
    @ServiceMethod(payload={"email"}, target=ServiceMethodContext.TARGET_SELF)
    public void goForgotPassword() {
        this.setStatus("forgotpassword");
        this.getMetaworksContext().setHow("forgotpassword");
    }

    @ServiceMethod(callByContent=true, payload={"email"}, validate=true)
    public void forgotPassword() throws Exception {

        Employee employee = new Employee();
        employee.setEmail(this.getEmail());
        IEmployee employeeRef = employee.findByEmail();

        // already s	ign up or invite user
        if (employeeRef == null) {
            throw new Exception("$NoExistedUser");
        } else if (!employeeRef.isApproved())
            throw new Exception("$NoExistedUser");

        // not yet sign up user
        String authKey = UUID.randomUUID().toString();

        employee.setEmpCode(employeeRef.getEmpCode());
        employee.databaseMe().setAuthKey(authKey);
        this.setEmail(employeeRef.getEmail());

        // send mail
        EmailSenderUtil.sendEmail("findpw.html?key=" + authKey, EmailSenderType.LOGIN_FORGET_PASSWORD, getEmail());

        this.getMetaworksContext().setHow("afterforgotpassword");
        return;
    }

	/*
    @ServiceMethod(callByContent=true)
	public MainPanel loginSocialCoding() throws Exception {
		IUser loginUser = new User();

		loginUser.setName(getName());
		loginUser.setUserId(getEmail());

		Session session = new Session();
		session.setUser(loginUser);
		session.setDefId(getDefId());

		storeIntoServerSession(session);

		MainPanel mainPanel = new MainPanel(new Main(session));

		return mainPanel;
		//return new MainPanel(new Knowledge(session));
	}
	 */

    public void fireServerSession(Session session) {
        String userId = session.getUser().getUserId().toUpperCase();
        //		String userId = session.getEmployee().getEmpCode().toUpperCase();

        if (SessionIdForEmployeeMapping.containsKey(userId)) {
            String sessionId = SessionIdForEmployeeMapping.get(userId);

            WebContext wctx = WebContextFactory.get();

            if (sessionId.equals(wctx.getScriptSession().getId())) {
                SessionIdForEmployeeMapping.remove(userId);

                if (session.getEmployee() != null) {
                    String partCode = session.getEmployee().getPartCode();
                    String globalCom = session.getEmployee().getGlobalCom();

                    if (partCode != null && partCode.length() > 0) {
                        partCode = partCode.toUpperCase();
                        HashMap<String, String> mapping = null;

                        if (SessionIdForDeptMapping.containsKey(partCode)) {
                            mapping = SessionIdForDeptMapping.get(partCode);
                            mapping.remove(userId);
                            SessionIdForDeptMapping.put(partCode, mapping);
                        }
                    }

                    if (globalCom != null && globalCom.length() > 0) {
                        globalCom = globalCom.toUpperCase();
                        HashMap<String, String> mapping = null;

                        if (SessionIdForCompanyMapping.containsKey(globalCom)) {
                            mapping = SessionIdForCompanyMapping.get(globalCom);
                            mapping.remove(userId);
                            SessionIdForCompanyMapping.put(globalCom, mapping);
                        }
                    }
                }
            }
        }


    }

    public void storeIntoServerSession(Session session) {
        /*
		 * 2013/04/22 cjw
		 *
		 * add tenant process
		 */
        String userId = session.getUser().getUserId().toUpperCase();
        String tenantId = null;

        if (session.getEmployee() != null)
            tenantId = session.getEmployee().getGlobalCom();

        //setting the userId into session attribute;
        HttpSession httpSession = TransactionContext.getThreadLocalInstance().getRequest().getSession();
        httpSession.setAttribute("userId", userId);
        httpSession.setAttribute("tenantId", tenantId);

		/*
		httpSession.setAttribute("tenantId", tenantId);

		String mySourceCodeBase = CodiClassLoader.mySourceCodeBase();

		//if(mySourceCodeBase.endsWith("main/src/"))
		if(mySourceCodeBase.endsWith(tenantId + "/src/"))
			(new File(mySourceCodeBase)).mkdirs();

		if(mySourceCodeBase!=null && new File(mySourceCodeBase).exists()){
			httpSession.setAttribute("sourceCodeBase", mySourceCodeBase);
		}
		 */


        // fire exist session
		/*
		if(SessionIdForEmployeeMapping.containsKey(userId)){
			MetaworksRemoteService.pushTargetScript(Login.getSessionIdWithUserId(getUserId()), "mw3.getAutowiredObject('" + Session.class.getName() + "').__getFaceHelper().fire", new Object[]{"2"});
		}
		 */
        fireServerSession(session);

        // manager sessionId
        WebContext wctx = WebContextFactory.get();
        String sessionId = wctx.getScriptSession().getId();

        SessionIdForEmployeeMapping.put(userId, sessionId); //stores session id to find out with user Id

        if (session.getEmployee() != null && session.getEmployee().isApproved()) {
            String partCode = session.getEmployee().getPartCode();
            String globalCom = session.getEmployee().getGlobalCom();

            if (partCode != null && partCode.length() > 0) {
                partCode = partCode.toUpperCase();
                HashMap<String, String> mapping = null;

                if (SessionIdForDeptMapping.containsKey(partCode))
                    mapping = SessionIdForDeptMapping.get(partCode);
                else
                    mapping = new HashMap<String, String>();

                mapping.put(userId, sessionId);
                SessionIdForDeptMapping.put(partCode, mapping);

            }

            if (globalCom != null && globalCom.length() > 0) {
                globalCom = globalCom.toUpperCase();
                HashMap<String, String> mapping = null;

                if (SessionIdForCompanyMapping.containsKey(globalCom))
                    mapping = SessionIdForCompanyMapping.get(globalCom);
                else
                    mapping = new HashMap<String, String>();

                //System.out.println("LOGIN : " + sessionId);
                mapping.put(userId, sessionId);
                SessionIdForCompanyMapping.put(globalCom, mapping);

            }
        }

        String device = "desktop";
        if (Main.isPad()) {
            device = "pad";
        } else if (Main.isPhone()) {
            device = "phone";
        }

        userIdDeviceMapping.put(userId.toUpperCase(), device); //stores session id to find out with user Id
    }

    protected void goTadpoleLogin(String email, String pw) {
        String ip = GlobalContext.getPropertyString("pole.call.ip", "localhost");
        String port = GlobalContext.getPropertyString("pole.call.port", "80");
        String db = GlobalContext.getPropertyString("pole.call.db", "/root");

        String parameter = "?email=" + email + "&pw=" + pw;

        String sUrl = "http://" + ip + ":" + port + db + "/loginUser" + parameter;

        URL url;          //URL 주소 객체
        URLConnection connection;  //URL접속을 가지는 객체
        InputStream is;        //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader isr;
        BufferedReader br;
        try {
            //URL객체를 생성하고 해당 URL로 접속한다..
            url = new URL(sUrl);
            connection = url.openConnection();

            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is = connection.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            //내용을 읽어서 화면에 출력한다..
            String buf = null;

            while (true) {
                buf = br.readLine();
                if (buf == null) break;
                System.out.println(buf);
            }

        } catch (IOException ioe) {
            System.err.println("IOException " + ioe);
            ioe.printStackTrace();
        }
    }

}