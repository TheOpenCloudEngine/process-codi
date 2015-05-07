package org.uengine.codi.mw3.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.metaworks.annotation.Hidden;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.admin.WebEditor;
import org.uengine.codi.util.CodiStringUtil;
import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

@Component
public class MemoWorkItem extends WorkItem{
	
	public MemoWorkItem(){
		setType(WORKITEM_TYPE_MEMO);
		setMemo(new WebEditor());
	}

	@Hidden(on=false)
	public WebEditor getMemo() {
		// TODO Auto-generated method stub
		return super.getMemo();
	}

	@Override
	public Object[] add() throws Exception {
		
		if(getMemo()!=null && getMemo().getContents()!=null){
			
			//if there is no title has been entered, use the first line of content:
			if(title==null || title.trim().length()==0) 
				try{
					//TODO: since the contents contains some multiple lines of html tags, so it cannot parses real next line of the text.
					title = getMemo().getContents().substring(0, getMemo().getContents().indexOf('\n'));
				}
			catch(Exception e){}
			
			if(getMemo().getContents().length() > 2990){
				
				String relativeFilePath = UEngineUtil.getCalendarDir() + "/memo" + getInstId() + "_" + System.currentTimeMillis() + ".html";
				String absoluteFilePath = CodiStringUtil.lastLastFileSeparatorChar(GlobalContext.FILE_SYSTEM_DIR) + relativeFilePath;
				
				File contentFile = new File(absoluteFilePath);
				contentFile.getParentFile().mkdirs();
				
				PrintWriter fos = new PrintWriter(contentFile);
				fos.write(getMemo().getContents());
				fos.close();
				
				this.setExtFile(relativeFilePath);

				getMemo().setContents("...loading...");
					
			}
			// TODO : ormapping 순서 문제로 this.content 값이 들어가 null 이 되는 현상 근본적 해결 필요			
			this.setContent(this.getMemo().getContents());

		}
		
		return super.add();
	}

	@Override
	public void loadContents() throws Exception {
		setContentLoaded(true);
		
		if(getExtFile()!=null){
			
			InputStream is = null;
			ByteArrayOutputStream bao = null;
			try{
				bao = new ByteArrayOutputStream();
				String absoluteFilePath = CodiStringUtil.lastLastFileSeparatorChar(GlobalContext.FILE_SYSTEM_DIR) + getExtFile();
				
				is = new FileInputStream(absoluteFilePath);
	
				UEngineUtil.copyStream(is, bao);
				
				getMemo().setContents(bao.toString());
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				setContentLoaded(true);
				
				if(is != null){
					try {
						is.close();
						is = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
				if(bao != null){
					try {
						bao.close();
						bao = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}
}
