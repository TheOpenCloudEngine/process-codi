package org.uengine.search.solr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.uengine.kernel.GlobalContext;

public class SolrServerJob implements StatefulJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		boolean isSolrAlive = isSolrServerAlive();
//		if (!isSolrAlive) {
//			System.out.println("서버 죽음");
//			startSolrServer();
//		}else{
//			System.out.println("서버 살음");
//		}
	}
	
	public static void startSolrServer() {
		// TODO os별로 커맨드 나누기...
		
		String solrDirectory = GlobalContext.getPropertyString("solrHome");
		String javaCommned = "java -jar start.jar";
		
		try {
			String[] command = { "cmd"};
			Process p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("cd /d D:");
			stdin.println("cd " + solrDirectory );
			stdin.println(javaCommned);
			stdin.close();
		 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isSolrServerAlive() {
		Process p = null;
		try {
			String[] command = { "cmd", };
			p = Runtime.getRuntime().exec(command);
//			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			BufferedReader reader = new BufferedReader (new InputStreamReader(p.getInputStream()));
			
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("netstat -o -n -a | findstr 0:8983");
			stdin.flush();
			stdin.close();
			
			String line;
			String resultStr = "";
			while ((line = reader.readLine()) != null) {
				resultStr += line;
			}
			System.out.println("================");
			System.out.println(resultStr);
			System.out.println("================");
			if (resultStr.indexOf("LISTENING") == -1) {
				return false;
			} else {
			    return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			if( p != null ){
//				System.out.println("final");
				p.destroy();
			}
		}
 
	}
}
