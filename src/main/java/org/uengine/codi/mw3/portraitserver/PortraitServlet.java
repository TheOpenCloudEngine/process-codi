package org.uengine.codi.mw3.portraitserver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.uengine.codi.common.ImageUtils;
import org.uengine.kernel.GlobalContext;

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

		String pathInfo = request.getPathInfo();
		if(pathInfo == null)
			return;
		
		// because pathInfo is started with a "/" character
		boolean exists = false;
		
		String portraitName = pathInfo.substring(1);		
		
		String fileSystemPath = GlobalContext.FILE_SYSTEM_DIR;
		String portraitPath = fileSystemPath + "/portrait";
		
		// 사진 폴더 생성
		File portraitDirectory = new File(portraitPath);		
		if(!portraitDirectory.exists() || !portraitDirectory.isDirectory())
			portraitDirectory.mkdirs();


		int pos = portraitName.lastIndexOf(".");
		if(pos > -1 && ".thumnail".equals(portraitName.substring(pos))){
			String empCode = portraitName.substring(0, pos);
			String thumnailName = portraitPath + File.separatorChar + empCode + ".thumnail.jpg";
			String srcName = portraitPath + File.separatorChar + empCode + ".jpg";
			//String unknownName = portraitPath + File.separatorChar + "unknown_user.gif";
			String unknownName = this.getServletContext().getRealPath("/") +  "images" + File.separatorChar +"portrait" + File.separatorChar +"unknown_user.gif";
			if( empCode.startsWith("dept_")){
				unknownName = this.getServletContext().getRealPath("/") +  "images" + File.separatorChar +"portrait" + File.separatorChar +"group.png";
			}
			// 쎔네일 파일 존재 확인
			File thumnailFile = new File(thumnailName);
			if(thumnailFile.exists() && thumnailFile.isFile()){
				exists = true;
			}else{
				File srcFile = new File(srcName);
				
				if(srcFile.exists() && srcFile.isFile() && srcFile.length() > 0){
					try{
						ImageUtils.createThumbnail(srcFile.getAbsolutePath(),thumnailFile.getAbsolutePath(), "jpg", 104, 104);
						exists = true;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			OutputStream out = null;
			try{
				out = response.getOutputStream();
				
				if(exists){
					BufferedImage bi = ImageIO.read(new File(thumnailName));
					response.setContentType("image/jpg");
					ImageIO.write(bi, "jpg", out);
				}else{
					File unknownFile = new File(unknownName);
					if(unknownFile.exists() && unknownFile.isFile()){
						BufferedImage bi = ImageIO.read(unknownFile);
						if( empCode.startsWith("dept_")){
							response.setContentType("image/png");
							ImageIO.write(bi, "png", out);					
						}else{
							response.setContentType("image/gif");
							ImageIO.write(bi, "gif", out);					
						}
					}				
				}
			}finally{
				if(out != null){
					out.close();
					out = null;
				}
			}
		}else{			
			String srcName = portraitPath + File.separatorChar + pathInfo + ".jpg";
			
			System.out.println("원본 이미지 요청 : " + srcName);
			
			File srcFile = new File(srcName);
			
			if(srcFile.exists() && srcFile.isFile()){
				OutputStream out = null;
				try{
					out = response.getOutputStream();
				
					BufferedImage bi = ImageIO.read(srcFile);
					response.setContentType("image/jpg");
					ImageIO.write(bi, "jpg", out);
				}finally{
					if(out != null){
						out.close();
						out = null;
					}
				}
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
