package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

import java.io.*;
import java.util.ArrayList;

public class Tray {

  @AutowiredFromClient
  public Session session;
  @AutowiredFromClient(onDrop = true)
  public IInstance dropInstance;
  @AutowiredFromClient(onDrop = true)
  public InstanceDrag dropInstanceDrag;
  ArrayList<TrayItem> trayItems;
  TrayItem targetItem;

  public Tray() {

  }

  @Face(options = "alignment", values = "horizontal")
  public ArrayList<TrayItem> getTrayItems() {
    return trayItems;
  }

  public void setTrayItems(ArrayList<TrayItem> trayItems) {
    this.trayItems = trayItems;
  }

  @ServiceMethod(callByContent = true)
  public void addTrayItem() throws Exception {
    if (this.getTargetItem() != null && this.getTargetItem().getInstId() != null) {
      if (trayItems == null)
        trayItems = new ArrayList<TrayItem>();

      trayItems.add(getTargetItem());

      save();
    }
  }

  protected void save() throws Exception {
    GlobalContext.serialize(getTrayItems(), new FileOutputStream(GlobalContext.FILE_SYSTEM_DIR + "/Tray_" + session.getUser().getUserId() + ".xml"), TrayItem.class);
  }

  public TrayItem getTargetItem() {
    return targetItem;
  }

  public void setTargetItem(TrayItem targetItem) {
    this.targetItem = targetItem;
  }

  @ServiceMethod(mouseBinding = "drop", callByContent = true)
  public void drop() throws Exception {
    Object clipboard = session.getClipboard();
    if (clipboard instanceof IInstance) {
      Instance instance = (Instance) clipboard;
      TrayItem ti = new TrayItem();
      ti.setInstId(instance.getInstId() + "");
      ti.setTitle(instance.databaseMe().getName());
      setTargetItem(ti);
      addTrayItem();
    } else if (clipboard instanceof InstanceDrag) {
      InstanceDrag instanceInClipboard = (InstanceDrag) clipboard;
      Instance locatorForInstanceInClipboard = new Instance();
      locatorForInstanceInClipboard.setInstId(instanceInClipboard.getInstanceId());

      IInstance instance = locatorForInstanceInClipboard.databaseMe();
      TrayItem ti = new TrayItem();
      ti.setInstId(instance.getInstId() + "");
      ti.setTitle(instance.getName());
      setTargetItem(ti);
      addTrayItem();
    }
  }

  public void load() throws Exception {
    /// read source file
    File sourceCodeFile = new File(GlobalContext.FILE_SYSTEM_DIR + "/Tray_" + session.getUser().getUserId() + ".xml");
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    FileInputStream is;
    if (sourceCodeFile.exists()) {
      is = new FileInputStream(sourceCodeFile);
      UEngineUtil.copyStream(is, bao);
      ArrayList<TrayItem> trayList = (ArrayList<TrayItem>) GlobalContext.deserialize(bao.toString("UTF-8"));
      for (TrayItem trayItem : trayList) {
        trayItem.getMetaworksContext().setWhen("view");
      }
      setTrayItems(trayList);
    }
  }
}

