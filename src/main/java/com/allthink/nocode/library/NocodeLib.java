package com.allthink.nocode.library;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class NocodeLib {
	/*public static Map<String, Object> mapper(HttpServletRequest request, Enumeration<String> params) throws Exception{
		Map<String, Object> paramData = new HashMap<>();
		
		while (params.hasMoreElements()) {
		    String name = (String) params.nextElement();
		    String value = request.getParameter(name);
		    //logger.debug(name + "=" + value);
		    paramData.put(name, value);
		}
		return paramData;
	}*/

	public static Map<String, Object> httpInfo(HttpServletRequest request) throws Exception{
		// 서블릿 정보 확인 : https://www.devkuma.com/docs/jsp-servlet/httpservletrequest-%EB%A9%94%EC%86%8C%EB%93%9C/
		
		Map<String, Object> paramData = new HashMap<>();
		Map<String, Object> headerData = new HashMap<>();
		//Map<String, Object> bodyData = new HashMap<>();
		Map<String, Object> httpData = new HashMap<>();
		Map<String, Object> returnData = new HashMap<>();
		
		
		// get param 데이터
		//Map<String, Object> paramData = NocodeLib.mapper(request,request.getParameterNames());
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
		    String name = (String) params.nextElement();
		    String value = request.getParameter(name);
		    //logger.debug(name + "=" + value);
		    paramData.put(name, value);
		}
		
		// 모든 Header 값 표시
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
		    String name = (String) headers.nextElement();
		    String value = request.getHeader(name);
		    //logger.debug(name + "=" + value);
		    headerData.put(name, value);
		}
		
		// Request Body - restAPI 처리시 필요할수도 있음
		/*DataInputStream dis = new DataInputStream(request.getInputStream());
		String str = null;
		while ((str = dis.readLine()) != null) {
		    logger.debug(new String(str.getBytes("ISO-8859-1"), "utf-8") + "/n");
		    // euc-kr로 전송된 한글은 깨진다.		    
		}*/
		
		httpData.put("URL", request.getRequestURL());
		httpData.put("Scheme", request.getScheme());
		httpData.put("ServerName", request.getServerName());
		httpData.put("URI", request.getRequestURI());
		httpData.put("Port", request.getServerPort());
		httpData.put("Method", request.getMethod());
		httpData.put("RemoteAddr", request.getRemoteAddr());
		httpData.put("RequestedSessionId", request.getRequestedSessionId());
		
		httpData.put("uri-etc", request.getServletPath());

		returnData.put("httpInfo", httpData);
		returnData.put("params", paramData);
		returnData.put("headerData", headerData);
		
		// 디버깅
		//System.out.println(returnData);
				
		// sql직접실행
		
		return returnData;
				
	}
}
