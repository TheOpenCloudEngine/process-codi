package org.uengine.codi.mw3.marketplace;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.Database;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.mw3.model.*;

import java.util.Date;

public class AppMapping extends Database<IAppMapping> implements IAppMapping {

    @AutowiredFromClient
    public Session session;

    private final static String APPMAPPING_TYPE = "app";

    public AppMapping() {
        this.setType(APPMAPPING_TYPE);
    }

    int appId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    String comCode;

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    boolean isDeleted;

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    MetaworksFile logoFile;

    public MetaworksFile getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(MetaworksFile logoFile) {
        this.logoFile = logoFile;
    }

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String empCode;

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String appType;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public IAppMapping findMe() throws Exception {

        IAppMapping findApp = (IAppMapping) Database.sql(IAppMapping.class, "select appmapping.appId, appmapping.comcode, appmapping.appname, appmapping.isdeleted, appmapping.url, appmapping.appType, appmapping.planId, appmapping.effectiveDate, appmapping.expirationDate, appmapping.isTrial, bpm_knol.name projectName from appmapping appmapping, app, bpm_knol where appmapping.appId = app.appId and app.projectId = bpm_knol.id and appmapping.appid=?appId and appmapping.comcode=?comCode");

        findApp.setAppId(this.getAppId());
        findApp.setComCode(this.getComCode());
        findApp.select();

        if (findApp.next())
            return findApp;
        else
            return null;
    }

    public IAppMapping findMyApps() throws Exception {
        return findMyApps(0);
    }

    public IAppMapping findMyApps(int limitCount) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select appmapping.appId, appmapping.comcode, appmapping.appname, appmapping.isdeleted, appmapping.url, appmapping.appType, bpm_knol.name projectName, item.* ")
                .append(" from appmapping, bpm_knol,  ")
                .append(" 	(select app.appId, app.logoFileName, app.logoContent, app.projectId, recentItem.empcode, recentItem.updateDate, recentItem.clickedCount from app left join recentItem on app.appid=recentItem.itemId) item")
                .append(" where appmapping.appId = item.appId ")
                .append("	and item.projectId = bpm_knol.id ")
                .append("	and appmapping.comcode=?comCode ")
                .append("	and appmapping.isdeleted=?isdeleted order by item.clickedCount desc " + ((limitCount == 0) ? " " : "limit " + limitCount));

        IAppMapping findApp = (IAppMapping) Database.sql(IAppMapping.class, sql.toString());
        findApp.setComCode(this.getComCode());
        findApp.setIsDeleted(this.getIsDeleted());
        findApp.select();

        return findApp;
    }

    public IAppMapping findMeWithAppName() throws Exception {

        IAppMapping findApp = (IAppMapping) Database.sql(IAppMapping.class, "select appmapping.appId, appmapping.comcode, appmapping.appname, appmapping.isdeleted, appmapping.url, appmapping.appType, appmapping.planId, appmapping.effectiveDate, appmapping.expirationDate, appmapping.isTrial, bpm_knol.name projectName from appmapping appmapping, app, bpm_knol where appmapping.appId = app.appId and app.projectId = bpm_knol.id and appmapping.appname=?appname and appmapping.comcode=?comCode");

        findApp.setAppName(this.getAppName());
        findApp.setComCode(this.getComCode());
        findApp.select();

        if (findApp.next())
            return findApp;
        else
            return null;
    }

    public IAppMapping findMyApp() throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select appmapping.appId, appmapping.comcode, appmapping.appname, appmapping.isdeleted, appmapping.url, appmapping.appType, bpm_knol.name projectName, item.* ")
                .append(" from appmapping, bpm_knol,  ")
                .append(" 	(select app.appId, app.logoFileName, app.logoContent, app.projectId, recentItem.empcode, recentItem.updateDate, recentItem.clickedCount from app left join recentItem on app.appid=recentItem.itemId) item")
                .append(" where appmapping.appId = ?appId ")
                .append("	and appmapping.appId = item.appId ")
                .append("	and item.projectId = bpm_knol.id ");

        IAppMapping findApp = sql(sql.toString());
        findApp.setAppId(this.getAppId());
        findApp.select();

        return findApp;
    }

    public void findProject(String appId) throws Exception {
        String projectName = null;
        this.setProjectName(projectName);
    }
}

