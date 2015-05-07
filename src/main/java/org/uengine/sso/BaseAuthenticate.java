package org.uengine.sso;

import java.util.HashMap;

public interface BaseAuthenticate {
	public String authorize(HashMap<String, String> params);		// 인증 함수 
	public String serviceTicket(String strTgt, String ssoService);	// 토큰 발행을 위한 코드 발급 함수 
	public String accesstoken(HashMap<String, String> args);		// 토큰 발행 함수 
	public boolean vaild();											// 검증 함수  
	public String profile(String access_token);						// 사용자 정보 및 토큰 만료여부 확인하는 함수 
	public boolean destorytoken(String access_token) throws Exception;	// 토큰 제거 함수 
}
