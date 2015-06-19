package org.uengine.codi.mw3.model;

import org.directwebremoting.ServerContextFactory;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.uengine.bean.factory.MetaworksSpringBeanFactory;

import javax.servlet.ServletContext;
import java.util.Map;

public class TopMenuPanel {

  @AutowiredFromClient
  public Session session;

  public TopMenuPanel() {
  }

  @ServiceMethod(target = ServiceMethodContext.TARGET_STICK)
  public Popup showChat() throws Exception {
    Popup popup = new Popup();

    return popup;
  }

  @ServiceMethod(target = ServiceMethodContext.TARGET_POPUP)
  public ModalWindow popupFeedback() {
    return new ModalWindow(new ContactUs(), 800, 570, "피드백");
  }

    @ServiceMethod(target=ServiceMethodContext.TARGET_STICK)
    public Popup showApps() throws Exception{
      AllAppList allAppList = MetaworksSpringBeanFactory.getBean(AllAppList.class);
      allAppList.session = session;

      Popup popup = new Popup();
      popup.setPanel(allAppList);

      return popup;
    }
}
