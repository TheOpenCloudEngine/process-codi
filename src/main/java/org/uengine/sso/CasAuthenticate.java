package org.uengine.sso;

import java.net.URLEncoder;
import java.util.HashMap;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.metaworks.dao.TransactionContext;
import org.springframework.web.util.CookieGenerator;
import org.uengine.codi.mw3.StartCodi;
import org.uengine.codi.util.CodiHttpClient;
import org.uengine.kernel.GlobalContext;


public class CasAuthenticate implements BaseAuthenticate {

	//	private final static String CAS_COOKIENAME = "CASTGC";
	public final static String CAS_RESTURL = GlobalContext.getPropertyString("cas.rest.url", "http://localhost:8080/cas/v1/tickets");
	public final static String CAS_OAUTHURL = GlobalContext.getPropertyString("cas.oauth.url", "http://127.0.0.1:8080/cas/oauth2.0");

	@Override
	public String authorize(HashMap<String, String> args) {
		try
		{
			String ssoService = args.get("ssoService");	// 로그인후 호출해줄 URL
			String username = args.get("username");		// 로그인 아이디 
			String password = args.get("password");		// 비밀번호 
			String strTGT   = args.get("tgt");			// CAS 발행되는 TGT

			CodiHttpClient codiHc = new CodiHttpClient();
			if(strTGT == null){
				if(ssoService == null || username == null || password == null)
					throw new Exception("parameter invalid. requirements ssoService, username, password ");

				// tgt 티켓발급받기 위하여 파라미터 세팅 
				HashMap<String, String> params = new HashMap<String,String>();
				params.put("username", username);
				params.put("password", password);
				
				// cas에 rest 방식으로 Ticket Granting Ticket 발급 
				HashMap mapResult = codiHc.sendMessageToEndPoint(CAS_RESTURL, params, "POST");
				if(mapResult != null){
					String strTgt = (String)mapResult.get(CodiHttpClient.RESULT_KEY);
					strTgt = strTgt.substring( strTgt.lastIndexOf("/") +1);
					
					// 쿠키저장
					CookieGenerator cookieGenerator = new CookieGenerator();
					cookieGenerator.setCookieSecure(false);				// 쿠키 보안여부 
					cookieGenerator.setCookieMaxAge(7889231);			// 쿠키 유지시간 
					cookieGenerator.setCookieName("CASTGC");			// 쿠리 저장이름 
					cookieGenerator.addCookie(TransactionContext.getThreadLocalInstance().getResponse(), strTgt);
					return strTgt;
				}
			}



			//			if(tgt == null){
			//				if(ssoService == null || username == null || password == null)
			//					throw new Exception("parameter invalid. requirements ssoService, username, password ");
			//				
			//				// tgt 티켓 발행 
			//				HashMap<String, String> params = new HashMap<String,String>();
			//				params.put("username", username);
			//				params.put("password", password);
			//				HashMap mapResult = codiHc.sendMessageToEndPoint(CAS_RESTURL, params, "POST");
			//				if(mapResult != null){
			//					String strTgt = (String)mapResult.get(CodiHttpClient.RESULT_KEY);
			//					strTgt = strTgt.substring( strTgt.lastIndexOf("/") +1);
			//					System.out.println("Tgt is : " + strTgt);
			//					
			//					// st 티켓 발행 
			//					params = new HashMap<String,String>();
			//					params.put("service", ssoService);
			//					String myURL = CAS_RESTURL + "/"+ strTgt ;
			//					mapResult = codiHc.sendMessageToEndPoint(myURL, params, "POST");
			//					String strSt = (String)mapResult.get(CodiHttpClient.RESULT_KEY);
			//					
			//					// 쿠키저장
			////					CookieGenerator cookieGenerator = new CookieGenerator();
			////					cookieGenerator.setCookieSecure(false);
			////					cookieGenerator.setCookieMaxAge(7889231);
			////					cookieGenerator.setCookieName(CAS_COOKIENAME);
			////					cookieGenerator.addCookie(TransactionContext.getThreadLocalInstance().getResponse(), strTgt);
			//					
			//					return strSt;
			//				}else{
			//					return null;
			//				}
			//				
			//			}else{
			//				String strTgt = tgt;
			//				if(ssoService == null )
			//					throw new Exception("parameter invalid. requirements ssoService ");
			//				
			//				// st 티켓 발행 
			//				HashMap<String,String> params = new HashMap<String,String>();
			//				params.put("service", ssoService);
			//				String myURL = CAS_RESTURL + "/"+ strTgt ;
			//				HashMap mapResult = codiHc.sendMessageToEndPoint(myURL, params, "POST");
			//				String strSt = (String)mapResult.get(CodiHttpClient.RESULT_KEY);
			//				
			//				return strSt;
			//			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		return null;
	}

	public String serviceTicket(String strTgt, String ssoService) {
		try
		{
			CodiHttpClient codiHc = new CodiHttpClient();

			HashMap<String,String> params = new HashMap<String,String>();
			params.put("service", ssoService);
			String myURL = CAS_RESTURL + "/"+ strTgt ;
			HashMap mapResult = codiHc.sendMessageToEndPoint(myURL, params, "POST");
			if(mapResult != null){
				String strSt = (String)mapResult.get(CodiHttpClient.RESULT_KEY);
				return strSt;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		return null;
	}


	@Override
	public String accesstoken(HashMap<String, String> args) {

		try{
			String redirect_uri = args.get("redirect_uri");
			String client_id = args.get("client_id");
			String client_secret = args.get("client_secret");
			String code = args.get("code");
			
			if(redirect_uri == null || client_id == null || client_secret == null || code == null)
				throw new Exception("parameter invalid. requirements redirect_uri, client_id, client_secret, code ");

			CodiHttpClient codiHc = new CodiHttpClient();
			
			// 해당 정보를 URL인코딩해줌 안그러면 오류발생 
			StringBuffer sb = new StringBuffer();
			sb.append("redirect_uri=");
			sb.append(URLEncoder.encode(redirect_uri, "UTF-8"));
			sb.append("&client_id=");
			sb.append(URLEncoder.encode(client_id, "UTF-8"));
			sb.append("&client_secret=");
			sb.append(URLEncoder.encode(client_secret, "UTF-8"));
			sb.append("&code=");
			sb.append(URLEncoder.encode(code, "UTF-8"));

			// oauth 토큰 발행 URL 생성 
			String sendurl = CAS_OAUTHURL +  "/accessToken?" + sb.toString();
			
			// cas oauth API 호출 
			HashMap mapResult = codiHc.sendMessageToEndPoint(sendurl, null, "GET");
			if(mapResult != null){
				// 토큰 정보 추출 작업 
				String result = (String)mapResult.get(CodiHttpClient.RESULT_KEY);
				String[] ret  = result.split("&");
				String[] accessToken = ret[0].toString().split("=");
				return accessToken[1].toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public boolean vaild() {
		String tgt = (String)TransactionContext.getThreadLocalInstance().getRequest().getSession().getAttribute("SSO-TGT");
		boolean result = tgt != null ? true : false;
		return result;
	}
	

	@Override
	public String profile(String access_token) {

		// 사용자 정보 및 토큰정보 확인 URL 생성 
		String sendurl = CAS_OAUTHURL + "/profile?access_token=" + access_token;
		CodiHttpClient codiHc = new CodiHttpClient();
		
		// cas에 oauth API를 이용하여 사용자 정보 및 토큰정보 확인 
		HashMap mapResult = codiHc.sendMessageToEndPoint(sendurl, null, "GET");
		if(mapResult != null){
			return (String)mapResult.get(CodiHttpClient.RESULT_KEY);
		}
		return null;
	}

	@Override
	public boolean destorytoken(String access_token) throws Exception {
		if(access_token != null){
			CodiHttpClient codiHc = new CodiHttpClient();
			
			// 토큰 제거 rest API URL 생성 
			String urls = CAS_RESTURL + "/" + access_token;
			
			// cas에 rest 방식으로 Ticket Granting Ticket 제거 
			HashMap mapResult = codiHc.sendMessageToEndPoint(urls, null, "DELETE");
			if(mapResult == null)
				return false;
			else
				return true;
		}else{
			return false;
		}
	}

}
