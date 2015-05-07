package org.uengine.codi.mw3.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.jdk.SimpleCompiler;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.example.ide.CompileError;
import org.metaworks.example.ide.SourceCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.util.CodiStringUtil;
import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

@Scope("request")
@Component
public class SourceCodeWorkItem extends WorkItem{
	
	public SourceCodeWorkItem(){
		setType(WORKITEM_TYPE_SRC);
		this.setSourceCode(new SourceCode());
		
	}

	@Hidden(on=false)
	public SourceCode getSourceCode() {
		// TODO Auto-generated method stub
		return super.getSourceCode();
	}
	
	@Override
	public Object[] add() throws Exception {
		
		if(getSourceCode()!=null && getSourceCode().getCode()!=null){
			
			//if there is no title has been entered, use the first line of content:
			if(title==null || title.trim().length()==0) 
				try{
					//TODO: since the contents contains some multiple lines of html tags, so it cannot parses real next line of the text.
					title = getSourceCode().getCode().substring(0, getSourceCode().getCode().indexOf('\n'));
				}
			catch(Exception e){}
			
			if(getSourceCode().getCode().length() > 2990){
				
				String relativeFilePath = UEngineUtil.getCalendarDir() + "/src" + getInstId() + "_" + System.currentTimeMillis() + ".html";
				String absoluteFilePath = CodiStringUtil.lastLastFileSeparatorChar(GlobalContext.FILE_SYSTEM_DIR) + relativeFilePath;
				
				File contentFile = new File(absoluteFilePath);
				contentFile.getParentFile().mkdirs();
				
				PrintWriter fos = new PrintWriter(contentFile);
				fos.write(getSourceCode().getCode());
				fos.close();
				
				setExtFile(relativeFilePath);

				getSourceCode().setCode("...loading...");
			}else{
				setExtFile(null);
			}
			
			// TODO : ormapping 순서 문제로 this.content 값이 들어가 null 이 되는 현상 근본적 해결 필요			
			this.setContent(this.getSourceCode().getCode());

		}
		
		return super.add();
	}

	@Override
	public void loadContents() throws Exception {
		setContentLoaded(true);
		
		if(getExtFile()!=null){
			ByteArrayOutputStream bao = new ByteArrayOutputStream();

			String absoluteFilePath = CodiStringUtil.lastLastFileSeparatorChar(GlobalContext.FILE_SYSTEM_DIR) + getExtFile();

			UEngineUtil.copyStream(new FileInputStream(absoluteFilePath), bao);
			
			getSourceCode().setCode(bao.toString());
		}
	}

	
	@ServiceMethod(callByContent=true)
	public void compile() throws ClassNotFoundException, CompileException, InstantiationException, IllegalAccessException{
		
		if(getSourceCode()==null) return;
		
	    SimpleCompiler compiler = new SimpleCompiler();
	    compiler.setParentClassLoader(this.getClass().getClassLoader());
	    
	    ArrayList<CompileError> compileErrors = new ArrayList<CompileError>();
	    
	    try {
			compiler.cook(getSourceCode().getCode());
		} catch (CompileException e) {
			
			String message = e.getMessage();
			String[] parts = null;
			int lineNumber = 0;
			
			try {
				lineNumber = Integer.parseInt((parts = message.split(":"))[2]);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				parts = null;
				//e1.printStackTrace();
			}
			
			CompileError compileError = new CompileError();
			compileError.setLine(lineNumber);
			compileError.setColumn(1);
			compileError.setMessage(parts != null ? parts[3] : message);
			
			compileErrors.add(compileError);
		}
	    
		CompileError[] complieErrorInArray = new CompileError[compileErrors.size()];
		compileErrors.toArray(complieErrorInArray);
		
		getSourceCode().setCompileErrors(complieErrorInArray);	    

	}
}
