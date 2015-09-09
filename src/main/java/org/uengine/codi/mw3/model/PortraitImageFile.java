package org.uengine.codi.mw3.model;

import java.io.*;

import com.sun.imageio.plugins.common.ImageUtil;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.common.ImageUtils;
import org.uengine.kernel.GlobalContext;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.ResourceManager;

public class PortraitImageFile extends MetaworksFile {

    public static final String IMAGE_EXTENTION = "jpg";
    public static final String IMAGE_THUMNAIL_EXTENTION = "thumnail.jpg";

    public PortraitImageFile() {
        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
    }

    String empCode;

    @Hidden
    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }


    @Override
    public Object[] upload() throws FileNotFoundException, IOException, Exception {
        if (getFileTransfer() == null
                || getFileTransfer().getFilename() == null
                || getFileTransfer().getFilename().length() == 0
                || getFileTransfer().getInputStream().available() == 0) {
            throw new Exception("No file attached");
        }

        ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);

        // Image Resource
        IResource imageResource = DefaultResource.createResource(CodiResourceType.PORTRAIT + File.separator
                + getEmpCode() + "." + IMAGE_EXTENTION);

        // Thumnail Image Resoruce
        IResource imageThumnailResource = DefaultResource.createResource(CodiResourceType.PORTRAIT + File.separator
                + getEmpCode() + "." + IMAGE_THUMNAIL_EXTENTION);

        // 원본 이미지 파일 복사
        try (InputStream is = this.getFileTransfer().getInputStream();
             OutputStream os = resourceManager.getStorage().getOutputStream(imageResource)) {
            MetaworksFile.copyStream(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // thumnail 이미지 생성.
        try (InputStream is = resourceManager.getStorage().getInputStream(imageResource);
             OutputStream os = resourceManager.getStorage().getOutputStream(imageThumnailResource)) {
            ImageUtils.createThumbnail(is, os, IMAGE_EXTENTION, 104, 104);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setFileTransfer(null); // ensure to clear the data

        return null;
    }
}
