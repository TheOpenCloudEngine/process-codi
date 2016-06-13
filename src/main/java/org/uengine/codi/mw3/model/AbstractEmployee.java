package org.uengine.codi.mw3.model;

import org.metaworks.annotation.*;
import org.metaworks.dao.Database;
import org.metaworks.website.MetaworksFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.processmanager.ProcessManagerRemote;

/**
 * Created by ho.lim on 2015-04-23.
 */
public abstract class AbstractEmployee extends Database<IEmployee> implements IEmployee{

    @Autowired
    public ProcessManagerRemote processManager;

    @AutowiredFromClient
    public Locale localeManager;

    @AutowiredFromClient
    public Session session;

    String authKey;
    boolean validEmail;
    String empCode;
    String empName;
    String facebookId;
    IDept dept;
    String partCode;
    String mood;
    String preferMob;
    String preferUX;
    String password;
    String confirmPassword;
    boolean isAdmin;
    String jikName;
    transient String partName;
    String globalCom;
    String isDeleted;
    String mobileNo;
    String email;
    String locale;
    PortraitImageFile imageFile;
    boolean approved;
    boolean guest;
    boolean mailNoti;
    String inviteUser;
    MetaworksFile companyLogo;

    @Override
    public String getAuthKey() {
        return authKey;
    }

    @Override
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public boolean isValidEmail() {
        return validEmail;
    }

    public void setValidEmail(boolean validEmail) {
        this.validEmail = validEmail;
    }

    @Override
    public String getEmpCode() {
        return empCode;
    }

    @Override
    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    @Override
    public String getEmpName() {
        return empName;
    }

    @Override
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    @Override
    public String getFacebookId() {
        return facebookId;
    }

    @Override
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public IDept getDept() {
        return dept;
    }

    @Override
    public void setDept(IDept dept) {
        this.dept = dept;
    }

    @Override
    public String getPartCode() {
        return partCode;
    }

    @Override
    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    @Override
    public String getMood() {
        return mood;
    }

    @Override
    public void setMood(String mood) {
        this.mood = mood;
    }

    @Override
    public String getPreferMob() {
        return preferMob;
    }

    @Override
    public void setPreferMob(String preferMob) {
        this.preferMob = preferMob;
    }

    @Override
    public String getPreferUX() {
        return preferUX;
    }

    @Override
    public void setPreferUX(String preferUX) {
        this.preferUX = preferUX;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Face(displayName="$Password", options="type", values="password")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public boolean getIsAdmin() {
        return isAdmin;
    }

    @Override
    public void setIsAdmin(boolean value) {
        this.isAdmin = value;
    }

    @Override
    public String getJikName() {
        return jikName;
    }

    @Override
    public void setJikName(String jikName) {
        this.jikName = jikName;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @Override
    public String getGlobalCom() {
        return globalCom;
    }

    @Override
    public void setGlobalCom(String globalCom) {
        this.globalCom = globalCom;
    }

    @Override
    public String getIsDeleted() {
        return this.isDeleted;
    }

    @Override
    public void setIsDeleted(String deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public String getMobileNo() {
        return mobileNo;
    }

    @Override
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    public PortraitImageFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(PortraitImageFile imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public boolean isApproved() {
        return approved;
    }

    @Override
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean isGuest() {
        return guest;
    }

    @Override
    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    @Override
    public boolean isMailNoti() {
        return mailNoti;
    }

    @Override
    public void setMailNoti(boolean mailNoti) {
        this.mailNoti = mailNoti;
    }

    @Override
    public String getInviteUser() {
        return inviteUser;
    }

    @Override
    public void setInviteUser(String inviteUser) {
        this.inviteUser = inviteUser;
    }

    public MetaworksFile getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(MetaworksFile companyLogo) {
        this.companyLogo = companyLogo;
    }
}
