package org.uengine.codi.mw3.previewserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.metaworks.Refresh;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.spring.SpringConnectionFactory;
import org.metaworks.website.MetaworksFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.uengine.codi.mw3.model.FileWorkItem;
import org.uengine.codi.mw3.model.Preview;
import org.uengine.codi.util.CodiStatusUtil;
import org.uengine.kernel.GlobalContext;

/**
 * Servlet implementation class PreviewServlet
 */
public class PreviewServlet extends HttpServlet {

	@Autowired
	SpringConnectionFactory connectionFactory;

	public static boolean useSpring = false;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PreviewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		if(pathInfo == null)
			return;

		// because pathInfo is started with a "/" character
		String previewName = pathInfo.substring(1);

		int pos = previewName.lastIndexOf(".");
		String previewType = previewName.substring(pos+1);

		pos = previewName.indexOf(".");
		String stringTaskId = previewName.substring(0, pos);
		Long taskId = Long.parseLong(stringTaskId);

		String fileSystemPath = GlobalContext.FILE_SYSTEM_DIR;
		String previewPath = fileSystemPath + "/preview";
		
		String pathSequence = String.valueOf(taskId / 1000);
		
		String taskPath = previewPath + File.separatorChar + pathSequence;

		File taskDirectory = new File(taskPath);
		if(!taskDirectory.exists() || !taskDirectory.isDirectory())
			taskDirectory.mkdirs();

		boolean converted = false;
		boolean pass = false;

		CodiStatusUtil statusUtil = new CodiStatusUtil(taskDirectory.getAbsolutePath(), taskId.toString() + "_" + previewType);
		FileWorkItem workItem = new FileWorkItem();
		workItem.setTaskId(taskId);

//		if("image".equals(previewType)){	// 처음에 이미지가 호출이 되고 이미지가 호출되는 순간 PDF 가 변환이 되기때문에 pdf 는 isProcessing 체크를 하지 않는다.
		if("image".equals(previewType) || "pdf".equals(previewType)){
			// 변환진행 여부
			if(statusUtil.isProcessing()){		// 변환중
				// 변환완료
				if(statusUtil.isComplete()){
					pass = true;
				}else{					
					System.out.println("반환중");
				}
			}else{								// 미변환
				System.out.println("변환처리");

				TransactionContext tx = new TransactionContext();
				tx.setManagedTransaction(false);
				tx.setAutoCloseConnection(true);

				if(connectionFactory!=null)
					tx.setConnectionFactory(connectionFactory);

				
				try {
					converted = workItem.createPreviewFile(taskDirectory.getAbsolutePath(), previewType);
					tx.commit();
				} catch(Throwable e){
					try {
						tx.rollback();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}finally{
					if( !converted ){
						// 변환 실패시...
						Preview preview = new Preview();
						preview.setTaskId(workItem.getTaskId());
						preview.setGrpTaskId(workItem.getGrpTaskId());
						preview.setMimeType(previewType);
						preview.setConvertStatus(workItem.getExt1());
						preview.setErrorStatus(true);
						
						MetaworksRemoteService.pushClientObjects(new Object[]{new Refresh(preview,true)});
					}
					try {
						tx.releaseResources();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(converted)
					statusUtil.complete();
			}
		}else{
			pass = true;
		}

		// 반환처리
		if(converted || pass){
//			System.out.println("반환처리 : " + stringTaskId + " , previewType = " + previewType);
			String convertedFilePath = null;

			if("pdf".equals(previewType))
				convertedFilePath = taskDirectory.getAbsolutePath() + File.separatorChar + workItem.makeConvertedFilename();
			else if("image".equals(previewType))
				convertedFilePath = taskDirectory.getAbsolutePath();
			else
				convertedFilePath = taskDirectory.getAbsolutePath() + File.separatorChar + pathInfo;

			File convertedFile = new File(convertedFilePath);

			if(convertedFile.exists() && convertedFile.isFile() && convertedFile.length() > 0){
				FileInputStream is = null;
				ServletOutputStream os = null;

				try{
					String mimeType = getServletContext().getMimeType(convertedFile.getAbsolutePath());

					is = new FileInputStream(convertedFile);

					response.setContentType(mimeType);
					response.addHeader("Content-Length", String.valueOf(convertedFile.length()));

					os = response.getOutputStream();
					MetaworksFile.copyStream(is, os);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					is.close();
				}

			}
		}


		/*
			File srcFile = new File(srcName);

			if(srcFile.exists() && srcFile.isFile() && srcFile.length() > 0){
				FileInputStream is = null;
				ServletOutputStream os = null;

				try{
					String mimeType = getServletContext().getMimeType(srcName);

					is = new FileInputStream(srcFile);
					response.setContentType(mimeType);

					os = response.getOutputStream();
					UEngineUtil.copyStream(is, os);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					os.close();
					is.close();
				}

			}else{
				try{
					WorkItem workItem =new WorkItem();
					workItem.setTaskId(taskId);

					String realContent = workItem.databaseMe().getContent();
					String realFilePath = fileSystemPath + "/" + realContent;

					boolean success = false;
					FileWorkItem fileWorkItem = new FileWorkItem();
					String realFileMimeType = getServletContext().getMimeType(realFilePath);
					success = fileWorkItem.createPreviewFile(taskId, realContent, realFileMimeType);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

	        try{
	        	tx.commit();
	        }catch(Exception exAtCommit){

	        	//TODO: [before production] replacing the reply (by reply.setThrowable(exAtCommit)) to shout out the right error messages to the client.
	        	Replies replies = (Replies) tx.getSharedContext("replies");
//	        	replies.getReply(0).
	        	replies.addReply(new Reply(replies.getCalls().getCall(0).getCallId(), null, exAtCommit));

	        	throw exAtCommit;
	        }	
		 */


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
