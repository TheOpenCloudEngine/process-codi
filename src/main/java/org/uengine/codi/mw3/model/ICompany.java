package org.uengine.codi.mw3.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.metaworks.dao.IDAO;
import org.metaworks.website.MetaworksFile;

@Table(name="COMTABLE")
@Face(displayName="$Company", ejsPath="dwr/metaworks/genericfaces/FormFace.ejs", ejsPathMappingByContext={"{when: 'logo', face: 'dwr/metaworks/org/uengine/codi/mw3/model/ICompany.ejs'}"}, options={"fieldOrder"}, values={"logo,repMail,repMlPwd"})
public interface ICompany extends IDAO {
	@Id
	@Hidden
	public String getComCode();
	public void setComCode(String comCode);

	@Face(displayName="$SALESEARLYSALES_BASECOMPANY_NAME")
	@Hidden
	public String getAlias();
	public void setAlias(String alias);
	
	@Name
	@Hidden
	public String getComName();
	public void setComName(String comName);

	@Hidden
	public String getDescription();
	public void setDescription(String description);

	@Hidden
	public String getIsDeleted();	
	public void setIsDeleted(String deleted);

	@Hidden
	public String getBillAccnt();
	public void setBillAccnt(String killbillAccount);

	@Hidden
	public String getBillSbscr();
	public void setBillSbscr(String killbillSubscription);

	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_REGULAREXPRESSION, condition="repMail", options={"/^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$/"}, message="이메일 형식이 잘못되었습니다")
	})
	
	@Face(options={"width"}, values={"300"})
	public String getRepMail();
	public void setRepMail(String repMail);
	
	@Hidden
	public String getRepMlHst();
	public void setRepMlHst(String repMlHst);
	
	@Face(options={"width"}, values={"300"})
	public String getRepMlPwd();
	public void setRepMlPwd(String repMlPwd);
	
	@Face(displayName="회사로고")
	@ORMapping(objectFields={"uploadedPath"}, databaseFields={"logoPath"})
	public MetaworksFile getLogo();
	public void setLogo(MetaworksFile logo);

	//@ServiceMethod
	public ICompany load() throws Exception;
	
	@Face(displayName="설정")
	@ServiceMethod(callByContent=true, validate=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] save() throws Exception;
	
	public ICompany findByAlias() throws Exception;

}
