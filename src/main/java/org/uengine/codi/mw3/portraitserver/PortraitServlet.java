package org.uengine.codi.mw3.portraitserver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.codi.common.ImageUtils;
import org.uengine.codi.mw3.model.CodiResourceType;
import org.uengine.codi.mw3.model.PortraitImageFile;
import org.uengine.kernel.GlobalContext;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.ResourceManager;

/**
 * Servlet implementation class UserImagesServlet
 */
public class PortraitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PortraitServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		TenantContext tenantContext = TenantContext.getThreadLocalInstance();
		if(tenantContext.getTenantId() == null){
			tenantContext = new TenantContext((String)request.getSession().getAttribute("tenantId"));
		}

		String pathInfo = request.getPathInfo();
		if(pathInfo == null)
			return;

		String portraitName = pathInfo.substring(1);

		ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);

		IResource imageResource = null;
		try {
			imageResource = DefaultResource.createResource(CodiResourceType.PORTRAIT + File.separator
					+ portraitName + "." + PortraitImageFile.IMAGE_EXTENTION);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try(InputStream is = resourceManager.getStorage().getInputStream(imageResource)){
			BufferedImage bi = ImageIO.read(is);
			response.setContentType("image/jpg");
			ImageIO.write(bi, PortraitImageFile.IMAGE_EXTENTION, response.getOutputStream());
		}catch(Exception e){
			writeDefaultImage(response, portraitName);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void writeDefaultImage(HttpServletResponse response, String portraitName) throws IOException {
		String imageName = null;

		if(portraitName.startsWith("dept_")){
			imageName = "group.png";
		}else{
			imageName = "unknown_user.gif";
		}

		File file = new File(this.getServletContext().getRealPath("/") +  "images" + File.separatorChar +"portrait"
				+ File.separatorChar + imageName);

		if(file.exists() && file.isFile()){
			BufferedImage bi = ImageIO.read(file);
			String imageExt = imageName.substring(imageName.lastIndexOf(".")+1);
			response.setContentType("image/" + imageExt);
			ImageIO.write(bi, imageExt, response.getOutputStream());
		}
	}
}
