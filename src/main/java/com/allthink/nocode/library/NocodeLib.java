package com.allthink.nocode.library;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;

import com.google.gson.Gson;

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
	public static Map<String, Object> serviceCall(HttpServletRequest request) throws Exception{
		ClassPathResource resource = new ClassPathResource("myApi/main.json");
		Gson gson = new Gson();
		
		String content = null;
		try {
			Path path = Paths.get(resource.getURI());
		    content = Files.lines(path).collect(Collectors.joining(System.lineSeparator()));
		    //content.forEach(System.out::println);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println(resource);
		
	    System.out.println("----------");
	    System.out.println(content);
	    System.out.println("----------");
	    
	    /*content = "{ \"k1\":\"v1\",\"k2\":\"v2\"}";
	    System.out.println("----------");
	    System.out.println(content);
	    System.out.println("----------");*/
	    
	    
		Map<String, Object> jsonData = new HashMap<>();
		List<Map<String,Object>> jsonArray = new ArrayList<Map<String, Object>>();
		
		try {
			//jsonData = (Map<String,Object>) gson.fromJson(content, jsonData.getClass());
			jsonArray = gson.fromJson(content, jsonArray.getClass());
			System.out.println("----------[		123		]----------");
		    System.out.println(jsonArray.get(0).get("var1"));
		    //jsonArray.forEach(System.out::println);
		    System.out.println("------------------------------");
		    
		} catch (Exception e) {
			System.out.println(e);
		}
		
	    System.out.println("----------[		124		]----------");
	    System.out.println(jsonData);
	    System.out.println("------------------------------");
		
		return jsonData;
	}
}
