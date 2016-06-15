package org.uengine.codi.mw3.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.metaworks.Forward;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToOpener;
import org.metaworks.annotation.*;
import org.metaworks.component.SelectBox;
import org.metaworks.dao.DAOFactory;
import org.metaworks.dao.KeyGeneratorDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.mindrot.jbcrypt.BCrypt;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.cloud.saasfier.TenantURL;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.StartCodi;
import org.uengine.codi.mw3.admin.TopPanel;
import org.uengine.codi.mw3.common.MainPanel;
import org.uengine.codi.mw3.knowledge.TopicMapping;
import org.uengine.codi.mw3.knowledge.WfNode;

@Face(
        ejsPathMappingByContext={
        "{how: 'signUp', face: 'dwr/metaworks/org/uengine/codi/mw3/model/EmployeeSignUp.ejs'}"
}, options={"fieldOrder"}, values={"empName,password"})
public class Employee extends EmployeeWithCRUD {

    @Override
    @Name
    @ValidatorSet({
            @Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="Please enter your name"),
            @Validator(name=ValidatorContext.VALIDATE_MAX , options={"200"}, message="Name must be under 200 characters.")
    })
    public String getEmpName(){
        return super.getEmpName();
    }

    @Override
    @Hidden
    public String getFacebookId(){
        return super.getFacebookId();
    }

    @Override
    @Hidden(when = "view")
    @ValidatorSet({
            @Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="Please enter a password"),
    })
    public String getPassword(){
        return super.getPassword();
    }

    @Override
    @NonLoadable
    @NonSavable
    @Validator(name=ValidatorContext.VALIDATE_CONDITION, condition="metaworksContext.where == 'inDetailWindow'", options="(password != confirmPassword)", message="패스워드와 동일하게 다시 입력해 주세요.")
    public String getConfirmPassword(){
        return super.getConfirmPassword();
    }

    @Override
    @Validator(name=ValidatorContext.VALIDATE_MAX , options={"20"}, message="Title must be under 20 characters.")
    public String getJikName(){
        return super.getJikName();
    }

    @Override
    @ORMapping(databaseFields={"partCode", "partName"}, objectFields={"partCode", "partName"})
    @NonSavable
    public IDept getDept(){
        return super.getDept();
    }

    @Override
    @NonSavable
    public String getPartName(){
        return super.getPartName();
    }

    @Override
    @ValidatorSet({
            @Validator(name=ValidatorContext.VALIDATE_NOTNULL,condition="metaworksContext.when == 'new2'", message="Please enter your company name")
    })
    public String getGlobalCom(){
        return super.getGlobalCom();
    }

    @Override
    @Hidden
    public String getIsDeleted(){
        return super.getIsDeleted();
    }

    @Override
    @Validator(name=ValidatorContext.VALIDATE_MAX , options={"20"}, message="Phone number must be under 20 digits.")
    public String getMobileNo(){
        return super.getMobileNo();
    }

    @Override
    @ValidatorSet({
            @Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="Please enter your e-mail address"),
            @Validator(name=ValidatorContext.VALIDATE_REGULAREXPRESSION, options={"/^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$/"}, message="Invalid Email address"),
            @Validator(name=ValidatorContext.VALIDATE_MAX , options={"50"}, message="Email address must be under 50 characters.")
    })
    public String getEmail(){
        return super.getEmail();
    }

    @Override
    @Range(
            values={"ko", "en"},
            options={"Korean", "English"}
    )
    public String getLocale(){
        return super.getLocale();
    }



    String role;
    @Range(
            values={"pa", "pm", "dev", "po"},
            options={"I'm a practice author", "I'm a project manager", "I'm a developer", "I'm a product owner"}
    )
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

    @Override
    @Range(
            values={"tw", "sns", "outlook", "asana", "wave"},
            options={"I love twitter", "I love facebook", "I love Outlook", "I love Asana", "I love Google Wave"}
    )
    public String getPreferUX(){
        return super.getPreferUX();
    }

    @Override
    @Range(
            values={"auto", "phone", "pad", "desktop"},
            options={"Auto Detection", "Phone (1-column)", "Pad (2-column)", "Desktop version"}
    )
    public String getPreferMob(){
        return super.getPreferMob();
    }

    @ServiceMethod(where = "navigator", payload = {"empCode", "empName", "jikName"})
    public Object[] loadOrganization() throws Exception {
        return Perspective.loadInstanceListPanel(session, "organization", getEmpCode(), "사원 : " + this.getEmpName() + "(" + this.getJikName() + ")");
    }

    @ServiceMethod(target = "append", where = "picker")
    public Object pickup() throws Exception {
        // TODO add Remover to return values
        IEmployee selectedEmployee = findMe();
        selectedEmployee.getMetaworksContext().setWhere("pickerCaller");
        return new Object[]{new ToOpener(selectedEmployee), new Remover(new Popup())};
    }

    @ServiceMethod(target = "popup")
    public Popup openPicker() {
        EmployeePicker employeePicker = new EmployeePicker(session.getCompany().getComCode());
        employeePicker.getMetaworksContext().setWhere("picker");
        return new Popup(employeePicker);
    }

    @ServiceMethod(callByContent = true, when = MetaworksContext.WHEN_NEW)
    public Object[] subscribeStep1() throws Exception {
        Locale locale = new Locale();
        locale.setLanguage(this.getLocale());
        locale.load();

        int posAt = this.getEmpCode().indexOf("@");
        if (posAt > -1) {
            String domain = this.getEmpCode().substring(posAt + 1);
            if (domain.indexOf("'") > -1) throw new Exception("email address is invalid");

            IEmployee exisingUserWhoInTheSameDomain = sql("select * from emptable where email like '%" + domain + "'");
            exisingUserWhoInTheSameDomain.select();

            if (exisingUserWhoInTheSameDomain.next()) {
                String globalCom = exisingUserWhoInTheSameDomain.getGlobalCom();
                this.setGlobalCom(globalCom);
            } else {
                this.setGlobalCom(domain);
            }
        }
        getMetaworksContext().setWhen("new2");
        return new Object[]{locale, this};
    }

    @ServiceMethod(callByContent = true, when = MetaworksContext.WHEN_NEW, validate = true)
    public Object subscribeStep2() throws Exception {
        if (!this.checkValidEmail()) {
            throw new Exception("Is that email is already subscribed");
        }

        // BCrypt 로 생성된 password 를 DB 에서 가져온 후,
        String password = this.getPassword();


        if (!BCrypt.checkpw(getPassword(), password)) {
            throw new Exception("Re-entered password doesn't match");
        }

        getMetaworksContext().setWhen("new3");

        return this;
    }

    public boolean checkValidEmail() throws Exception {
        int posAt = this.getEmail().indexOf("@");

        if (this.getEmail().length() != 0 && posAt > -1) {
            String[] splitEmail = this.getEmail().split("@", -1);

            if (splitEmail[0].length() != 0 && splitEmail[1].length() != 0) {
                return true;
            }
        }
        return false;
    }

    @ServiceMethod(callByContent = true, when = MetaworksContext.WHEN_NEW, validate = true)
    public Object subscribeStep3() throws Exception {
        Locale locale = new Locale();
        this.setLocale(localeManager.getLanguage());
        locale.setLanguage(localeManager.getLanguage());
        locale.load();

        getMetaworksContext().setHow("detail");
        getMetaworksContext().setWhen("edit");
        getMetaworksContext().setWhere("admin");

        this.setImageFile(new PortraitImageFile());
        this.getImageFile().getMetaworksContext().setWhen(WHEN_EDIT);

        return new Object[]{locale, this};
    }

    @ServiceMethod(callByContent = true, when = MetaworksContext.WHEN_NEW, validate = true, target = ServiceMethodContext.TARGET_POPUP)
    public Object[] saveEmployeeInfo() throws Exception {
        this.saveMe();

        if (session != null && session.getEmployee().getEmpCode().equals(getEmpCode())) {
            session.setEmployee(findMe());

            SNS sns = MetaworksRemoteService.getComponent(SNS.class);
            sns.load(session);
            TopPanel topPanel = new TopPanel(session);
            topPanel.setTopCenterPanel(sns.loadTopCenterPanel(session));

            MainPanel mainPanel = new MainPanel(topPanel, sns);

            return new Object[]{new Remover(new ModalWindow(), true), new Refresh(mainPanel)};
        }

        if (this.getIsAdmin() == false && !this.isApproved()) {
            Session session = new Session();
            session.setEmployee(this);
            session.fillSession();
            session.setGuidedTour(true);

            CommentWorkItem newComment = new CommentWorkItem();
            newComment.processManager = processManager;
            newComment.session = session;

            newComment.setTitle(localeManager.getResourceBundle().getProperty("RequestJoinApprovedMessage"));
            newComment.save();

            processManager.applyChanges();
        }

        this.notiToCompany();

        ModalWindow modalWindow = new ModalWindow();
        modalWindow.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
        modalWindow.setWidth(300);
        modalWindow.setHeight(150);

        modalWindow.setTitle("$JoinCompleteTitle");
        modalWindow.setPanel(localeManager.getString("$JoinCompleteMessage"));
        modalWindow.getButtons().put("$Confirm", null);
        modalWindow.getCallback().put("$Confirm", "forward");

        return new Object[]{modalWindow, new Refresh(this)};
    }

    public void notiToCompany() throws Exception {
        Notification noti = new Notification();
        INotiSetting notiSetting = new NotiSetting();
        Instance instance = new Instance();
        noti.session = session;
        instance = this.createWorkItem("join");

        Employee employee = new Employee();
        employee.setEmpCode(session.getEmployee().getEmpCode());
        employee.copyFrom(employee.databaseMe());
        IEmployee findResult = employee.findByGlobalCom(employee.getGlobalCom());
        INotiSetting findNotiSetting;

        while (findResult.next()) {
            findNotiSetting = notiSetting.findByUserId(findResult.getEmpCode());
            if (findNotiSetting.next()) {
                if (findNotiSetting.isModiUser()) {
                    noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
                    noti.setUserId(findResult.getEmpCode());
                    noti.setActorId(session.getEmployee().getEmpName());
                    noti.setConfirm(false);
                    noti.setInstId(instance.getInstId());
                    noti.setInputDate(Calendar.getInstance().getTime());
                    noti.setActAbstract(session.getUser().getName() + "님이 가입하셨습니다.");

                    //워크아이템에서 노티를 추가할때와 동일한 로직을 수행하도록 변경

                    noti.add(instance);

                    String followerSessionId = Login.getSessionIdWithUserId(employee.getEmpCode());

                    try {
                        //NEW WAY IS GOOD
                        Browser.withSession(followerSessionId, new Runnable() {

                            @Override
                            public void run() {
                                //refresh notification badge
                                ScriptSessions.addFunctionCall("mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh", new Object[]{});
                            }

                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Instance createWorkItem(String type) throws Exception {
        SystemWorkItem comment = new SystemWorkItem();
        comment.processManager = processManager;
        comment.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);

        if ("join".equals(type)) {
            comment.setSystemMessage(this.getEmpName() + " has been subscribed");
        } else if ("addFriend".equals(type)) {
            comment.setSystemMessage(session.getUser().getName() + " added " + this.getEmpName() + " as a friend.");
        }
        comment.session = session;

        comment.add();

        Instance instance = new Instance();
        instance.setInstId(comment.getInstId());
        instance.copyFrom(instance.databaseMe());

        return instance;
    }

    @ServiceMethod(callByContent = true)
    public Object[] closeEmployeeInfo() throws Exception {
        return new Object[]{new Remover(new ModalWindow(), true)};
    }

    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_NONE)
    public void saveMe() throws Exception {
        if (this.getGlobalCom() == null) {
            String comAlias = Employee.extractTenantName(this.getEmail());
            boolean isAdmin = false;

            Company company = new Company();
            company.setAlias(comAlias);

            ICompany findCompany = company.findByAlias();
            if (findCompany == null) {
                isAdmin = true;

                // not yet sign up tenant
                try {
                    company.setComCode(company.createNewId());
                    company.setComName(comAlias);

                    findCompany = company.createDatabaseMe();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new MetaworksException(e.getMessage());
                }

                String tenantId = findCompany.getComCode();

                this.setGlobalCom(tenantId);

                new TenantContext(tenantId); // From now, tenant will be recognized.

                TenantLifecycle tenantLifecycle = MetaworksRemoteService.getComponent(TenantLifecycle.class);
                if(tenantLifecycle!=null)
                    tenantLifecycle.onNewTenantSubscribe(tenantId);

                // Addition of Basic Topics and Processes
               // ---------
//                addBasicTopics();
//                addBasicProcess();
            } else {
                this.setGlobalCom(findCompany.getComCode());
            }
        }

        this.setApproved(true);
        this.setMailNoti(this.isMailNoti());

        // 프로필 수정이 안되서 일단 강제로 영어로 적용되게끔
//        this.setLocale("en");

        // TODO: 부서 딕셔너리 처리 필
        if (getImageFile() != null && getImageFile().getFileTransfer() != null && getImageFile().getFileTransfer().getFilename() != null && !"".equals(getImageFile().getFileTransfer().getFilename())) {
            if (getImageFile().getFileTransfer().getMimeType() != null && !getImageFile().getFileTransfer().getMimeType().startsWith("image")) {
                throw new MetaworksException("$OnlyImageFileCanUpload");
            } else {
                getImageFile().setEmpCode(this.getEmpCode());
                getImageFile().upload();
            }
        }

        // Random salt 생성 및 BCrypt 암호화
        // workload 에 따라 성능에 영향을 미치므로 normal 계수인 6을 대입
        int workload = 6;
        String salt = BCrypt.gensalt(workload);
        this.setPassword(BCrypt.hashpw(this.getPassword(), salt));

        if (WHEN_NEW.equals(this.getMetaworksContext().getWhen())) {
            this.setEmpCode(this.createNewId());
            createDatabaseMe();
        } else
            syncToDatabaseMe();

        flushDatabaseMe();

        NotiSetting notiSetting = new NotiSetting();
        notiSetting.setId(this.createNewNotiSettingId());
        notiSetting.setUserId(this.getEmpCode());
        notiSetting.setWriteInstance(true);

        notiSetting.createDatabaseMe();

        if (session != null) {
            session.setEmployee(this);
            session.fillUserInfoToHttpSession();
        }
    }

    public static String extractTenantName(String email) {

        if(email==null) return null;

        String str = email.substring(email.indexOf("@") + 1);

        return str.substring(0, str.indexOf("."));


    }

    public String createNewId() throws Exception {
        Map options = new HashMap();
        options.put("useTableNameHeader", "false");
        options.put("onlySequenceTable", "true");

        KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("emptable", options);
        kg.select();
        kg.next();

        Number number = kg.getKeyNumber();

        return number.toString();
    }

    public int createNewNotiSettingId() throws Exception {
        Map options = new HashMap();
        options.put("useTableNameHeader", "false");
        options.put("onlySequenceTable", "true");

        KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("notisetting", options);
        kg.select();
        kg.next();

        Number number = kg.getKeyNumber();

        return number.intValue();
    }

    public void addBasicTopics() throws Exception {
        String topics[] = {"영업", "도서", "연락처"};

        for (int i = 0; i < topics.length; i++) {
            WfNode wfNode = new WfNode();

            wfNode.setName(topics[i]);
            wfNode.setType("topic");
            wfNode.setSecuopt("0");
            wfNode.setParentId(this.getGlobalCom());
            wfNode.setAuthorId(this.getEmpCode());
            wfNode.setCompanyId(this.getGlobalCom());
            wfNode.createMe();

            TopicMapping tm = new TopicMapping();
            tm.setTopicId(wfNode.getId());
            tm.setUserId(this.getEmpCode());
            tm.setUserName(this.getEmpName());
            tm.getMetaworksContext().setWhen(this.getMetaworksContext().getWhen());

            tm.saveMe();
        }
    }

    public void addBasicProcess() throws Exception {
//        String[] defId = {
//                "buisinesstrip/process/buisinesstrip.process",
//                "contact/process/contactregistration.process",
//                "holiday/process/applholiday.process",
//                "purchasing/process/purchasingreq.process",
//                "sales/process/sales.process",
//                "troubleticket/process/troubleticket.process",
//        };
//
//        String[] name = {
//                "출장",
//                "연락처",
//                "휴가",
//                "구매",
//                "영업",
//                "클레임",
//        };
//
//        String basePath = "default/process/";
//        String[] upLoadedPath = {
//                basePath + "buisinesstrip.png",
//                basePath + "contact.png",
//                basePath + "holiday.png",
//                basePath + "purchasing.png",
//                basePath + "sales.png",
//                basePath + "troubleticket.png",
//        };
//
//        for (int i = 0; i < defId.length; i++) {
//            IProcessMap processMap = new ProcessMap();
//            processMap.setMapId(this.getGlobalCom() + "." + defId[i]);
//            processMap.setDefId(defId[i]);
//            processMap.setName(name[i]);
//            processMap.setComCode(this.getGlobalCom());
//            processMap.setNo(i);
//            processMap.setIconFile(new MetaworksFile());
//            processMap.getIconFile().setUploadedPath(upLoadedPath[i]);
//            processMap.createMe();
//        }




    }


    @ServiceMethod(callByContent = true, target = "popup")
    public void addTopicUser() throws Exception {
        System.out.println(getEmpName());
    }

    @ServiceMethod(callByContent = true)
    public void checkEmpCode() throws Exception {
        IEmployee employee = this.findMe();

        if (employee.getEmpCode() != null)
            throw new Exception("이미 존재하는 empCode 입니다.");
    }

    @ServiceMethod(payload = {"email"}, target = ServiceMethodContext.TARGET_NONE)
    public String checkId() throws Exception {
        String valid = "invalid";

        if (checkValidEmail()) {
            this.setEmpCode(this.getEmail());
            IEmployee employee = this.findMe();

            if (employee.getEmpCode() == null)
                valid = "valid";
            else
                valid = "duplicate";
        }
        return valid;
    }

    @ServiceMethod(needToConfirm = true, inContextMenu = true)
    @Face(displayName = "$Unsubscribe")
    @Available(where = {"inDetailWindow", "inDetailPopup"})
    public Object[] unsubscribe() throws Exception {
        if (session.getEmployee().getEmpCode().equals(this.getEmpCode()) || session.employee.getIsAdmin()) {

            Employee employee = new Employee();
            employee.setEmpCode(this.empCode);
            employee.databaseMe().setIsDeleted("1");

            if (session.getEmployee().getEmpCode().equals(this.getEmpCode()))
                return new Object[]{session.logout(), new Remover(new ModalWindow())};
            else
                return new Object[]{new Remover(employee, true)};
        } else
            throw new Exception("관리자나 본인이 아니면 탈퇴할 수 없습니다");
    }

    @Available(how = {"tree"}) // 상황에 맞춰서 넣어 줘야 한다.
    @ServiceMethod(callByContent = true, mouseBinding = "drag-enableDefault")
    public Session drag() throws Exception {
        session.setClipboard(this);
        return session;
    }

    @Hidden(where = {"step1"})
    @ServiceMethod(callByContent = true)
    public void prevStep() {
        this.getImageFile().setFileTransfer(null);

        int step = Integer.parseInt(this.getMetaworksContext().getWhere().substring(4));
        this.getMetaworksContext().setWhere("step" + String.valueOf(step - 1));
    }

    @Hidden(where = {"step2"})
    @ServiceMethod(callByContent = true, validate = true)
    public void nextStep() {
        this.getImageFile().setFileTransfer(null);

        int step = Integer.parseInt(this.getMetaworksContext().getWhere().substring(4));

        this.getMetaworksContext().setWhere("step" + String.valueOf(step + 1));
    }

    @Available(where = {"step2", "forgotpassword"})
    @ServiceMethod(callByContent = true, validate = true)
    public Forward finish() throws Exception {
        session = new Session();

        this.saveMe();

        return new Forward(TenantURL.getURL());
    }

    public IEmployee findCompanyAdmin() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ");
        sb.append("emptable ");
        sb.append("where isAdmin=?isAdmin ");
        sb.append("and globalCom=?globalCom ");

        IEmployee dao = sql(sb.toString());
        dao.setIsAdmin(true);
        dao.setGlobalCom(this.getGlobalCom());
        dao.select();

        return dao;
    }

    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_NONE)
    public Object facebookSSO() throws Exception {
        IEmployee findEmp = this.findForLogin();

        if (findEmp == null) {
            this.getMetaworksContext().setWhen(WHEN_NEW);
            this.saveMe();
        }

        Session session = new Session();
        session.setEmployee(findEmp);
        session.fillUserInfoToHttpSession();

        return new StartCodi(session);
    }


    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_SELF)
    public Object forgotPassword() throws MetaworksException {
        System.out.println("authKey= " + getAuthKey());

        Employee employee = new Employee();
        employee.setAuthKey(this.getAuthKey());
        IEmployee findEmployee = null;

        try {
            findEmployee = employee.findByKey();
        } catch (Exception e) {
            e.printStackTrace();

            throw new MetaworksException(e.getMessage());
        }

        if (findEmployee == null)
            throw new MetaworksException("wrong access");
        if (!findEmployee.getAuthKey().equals(this.getAuthKey()))
            throw new MetaworksException("not match activation code");

        findEmployee.getMetaworksContext().setWhere("forgotpassword");
        findEmployee.getMetaworksContext().setHow("signUp");
        findEmployee.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
        findEmployee.setPassword(null);
        return findEmployee;
    }


    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_SELF)
    public Object[] applyForAddContact() throws Exception {
        IEmployee findEmployee = null;

        try {
            findEmployee = findByKey();
        } catch (Exception e) {
            e.printStackTrace();

            throw new MetaworksException(e.getMessage());
        }

        if (findEmployee == null)
            throw new MetaworksException("wrong access");


        //초대 받는사람 - newContact
        //초대 한 사람  - recommender
        if (findEmployee.getInviteUser() != null) {

            Contact newContact = new Contact();
            newContact.setUserId(findEmployee.getEmpCode());
            Employee recommender = new Employee();
            recommender.setEmpCode(findEmployee.getInviteUser());
            recommender.copyFrom(recommender.findMe());

            IUser newUser_ = new User();
            newUser_.setName(recommender.getEmpName());
            newUser_.setUserId(recommender.getEmpCode());
            newUser_.setNetwork("local");
            newContact.setFriendId(newUser_.getUserId());
            newContact.setFriend(newUser_);
            newContact.createDatabaseMe();

            newContact = new Contact();
            newContact.setUserId(recommender.getEmpCode());

            IUser me_ = new User();
            me_.setName(findEmployee.getEmpName());
            me_.setUserId(findEmployee.getEmpCode());
            me_.setNetwork("local");
            newContact.setFriendId(me_.getUserId());
            newContact.setFriend(me_);
            newContact.createDatabaseMe();

            findEmployee.setInviteUser(null);
            Employee saveEmp = new Employee();
            saveEmp.copyFrom(findEmployee);
            saveEmp.syncToDatabaseMe();

            Login login = new Login();
            login.setEmail(saveEmp.getEmail());
            login.setPassword(saveEmp.getPassword());
            return login.login();
        }
        return null;
    }

    @ServiceMethod(callByContent=true, validate=true, target= ServiceMethodContext.TARGET_SELF)
    public Object activate() throws MetaworksException{
        IEmployee findEmployee = null;

        try {
            findEmployee = findByKey();
        } catch (Exception e) {
            e.printStackTrace();

            throw new MetaworksException(e.getMessage());
        }

        if(findEmployee == null)
            throw new MetaworksException("Illegal access");

        if(findEmployee.isApproved()){
            Login login = new Login();
            login.setEmail(findEmployee.getEmail());

            return login;
        }

        if(!findEmployee.getAuthKey().equals(this.getAuthKey()))
            throw new MetaworksException("not match activation code");

        if(findEmployee.getInviteUser() != null)
            this.setInviteUser(findEmployee.getInviteUser());

        Employee signUpEmployee = new Employee();

        try {
            signUpEmployee.copyFrom(findEmployee);
        } catch (Exception e) {
            throw new MetaworksException("Employee copy Failed!");
        }

        signUpEmployee.getMetaworksContext().setWhere("step1");
        signUpEmployee.getMetaworksContext().setHow("signUp");
        signUpEmployee.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
        signUpEmployee.setImageFile(new PortraitImageFile());
        signUpEmployee.getImageFile().getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        //defaultUX, defaultMob 값 설정
        String defaultUX = "wave";
        String defaultMob = "auto";

        signUpEmployee.setPreferUX(defaultUX);
        signUpEmployee.setPreferMob(defaultMob);
        signUpEmployee.setPassword(null);
        return signUpEmployee;
    }

    public static IEmployee getSystemUser() throws Exception{
        Employee find = new Employee();

        find.setEmpCode("0");

        return find.findMe();
    }

    @ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
    public Object[] forward() throws Exception {
        Session session = new Session();
        session.setEmployee(this);
        session.fillSession();
        session.setGuidedTour(true);

        Login login = new Login();
        login.storeIntoServerSession(session);

        ModalWindow removeWindow = new ModalWindow();
        removeWindow.setId("subscribe");

        return new Object[]{new Remover(removeWindow, true), new ToOpener(new MainPanel(new Main(session)))};
        //return new Object[]{new Remover(removeWindow, true), new Remover(new ModalWindow()), new ToOpener(new MainPanel(new Main(session)))};
    }

    public IEmployee editEmployeeInfo() throws Exception {
        IEmployee employeeProxy = findMe();
        Employee employee = new Employee();
        employee.copyFrom(employeeProxy);
        // password empty
        employee.setPassword("");

        employee.getMetaworksContext().setHow("detail");
        employee.getMetaworksContext().setWhen(WHEN_EDIT);
        employee.getMetaworksContext().setWhere("inDetailWindow");

        employee.setImageFile(new PortraitImageFile());
        employee.getImageFile().getMetaworksContext().setWhen(WHEN_EDIT);

        return employee;
    }

    public INotiSetting editNotiSetting() throws Exception {
        NotiSetting notiSetting = new NotiSetting();
        notiSetting.getMetaworksContext().setWhen(WHEN_EDIT);
        INotiSetting result = notiSetting.findByUserId(this.getEmpCode());
        if( result.next() ){
            notiSetting.copyFrom(result);
            SelectBox selectBox = new SelectBox();
            selectBox.add("OnDate", "1");
            selectBox.add("DayBefore", "2");
            selectBox.setSelected(notiSetting.getDefaultNotiTime());
            notiSetting.setSelectTime(selectBox);
        }else{
            notiSetting.setUserId(this.getEmpCode());
        }
        return notiSetting;
    }

    public void notiToFriend() throws Exception{
        Notification noti = new Notification();
        INotiSetting notiSetting = new NotiSetting();
        Instance instance = new Instance();
        noti.session = session;
        instance = this.createWorkItem("addFriend");

        INotiSetting findNotiSetting = notiSetting.findByUserId(this.getEmpCode());
        if(findNotiSetting.next()){
            if(findNotiSetting.isModiUser()){
                noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
                noti.setUserId(this.getEmpCode());
                noti.setActorId(session.getEmployee().getEmpName());
                noti.setConfirm(false);
                noti.setInstId(instance.getInstId());
                noti.setInputDate(Calendar.getInstance().getTime());
                noti.setActAbstract(session.getUser().getName() + " added " + this.getEmpName() + " as a friend.");

                noti.add(instance);

                String followerSessionId = Login.getSessionIdWithUserId(this.getEmpCode());

                try{
                    //NEW WAY IS GOOD
                    Browser.withSession(followerSessionId, new Runnable(){

                        @Override
                        public void run() {
                            //refresh notification badge
                            ScriptSessions.addFunctionCall("mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh", new Object[]{});
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkRegistered() throws Exception {
        boolean alreadyRegistered = false;
        try{
            this.setEmpCode(this.getEmail());

            this.databaseMe();

            alreadyRegistered = true;
        }catch(Exception e){}

        if(alreadyRegistered)
            throw new Exception("$AlreadyExistingUser");
    }
}
