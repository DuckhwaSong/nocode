package com.allthink.nocode.library;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.allthink.nocode.model.NocodeDao;
import com.google.gson.Gson;

public class NocodeLib {
	@Autowired
	private static NocodeDao nocodeDao;
	
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

	// 리퀘스트 정보 전달
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

	
	// 파일을 스트링으로 전달 - serviceCall 보조함수
	private static String json2String(String serviceID) {
		String returnStr= "";		
		ClassPathResource resource = new ClassPathResource("myApi/"+serviceID+".json");		
		try {
			Path path = Paths.get(resource.getURI());
			returnStr = Files.lines(path).collect(Collectors.joining(System.lineSeparator()));
		} catch (Exception e) {
			System.out.println(e);
		}
		return returnStr;
	}
	
	// json 문자열을 jsonArray로 변환 - serviceCall 보조함수
	private static List<Map<String,Object>> json2Array(String jsonString) {
		List<Map<String,Object>> jsonArray = new ArrayList<Map<String, Object>>();    
		Gson gson = new Gson();
		try {
			jsonArray = gson.fromJson(jsonString, jsonArray.getClass());		    
		} catch (Exception e) {
			System.out.println(e);
		}		
		return jsonArray;		
	}
	
	// query를 분석하여 ps 형태로 전달 - serviceCall 보조함수
	private static Map<String, Object> query2Map(Map<String, Object> requestData, String queryOrigin){	
		// 쿼리 문자열을 가공하여 ? 표시 변경
		String queryReplace=queryOrigin.replaceAll("\\{:[^\\}]*\\}","?");
		
		// 리스트에 패턴을 담는다
		List<String> preparedStatement = new ArrayList<String>();
		Matcher m = Pattern.compile("\\{:[^\\}]*\\}").matcher(queryOrigin);
		String tmpKey="";
		String tmpValue="";
		while (m.find()) { 
			tmpKey=m.group().toString().replaceAll("\\{:","").replaceAll("\\}","");
			tmpValue=parser(requestData,tmpKey);
			preparedStatement.add(tmpValue);
			
		}
		System.out.println("queryExec1 : " + NocodeDao.queryExec(queryReplace,preparedStatement.toArray()));

		Map<String, Object> returnData = new HashMap<>();

		return returnData;
	}
	// 변수 세팅을 위한 재귀함수  - serviceCall 보조함수
	private static String parser(Map<String, Object> datas, String tmpKey){
		String[] tmpKeys=tmpKey.split("\\.");
		
		if(tmpKeys.length == 1 ) return  datas.get(tmpKeys[0]).toString();
		else if(tmpKeys.length > 1 && datas.get(tmpKeys[0]).getClass().getName()=="java.util.HashMap") {
			Map<String, Object> reData = (Map<String, Object>) datas.get(tmpKeys[0]);
			String reKey=tmpKey.replaceAll(tmpKeys[0]+"\\.", "");
			return parser( reData , reKey);			
		}
		return "";
	}
	
	/*private List<Map<String,Object>> queryExec(String sql) {	
		List<Map<String,Object>> list = nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (0)");	


		
		return list;
	  }*/

	
	// 서비스콜 메인 메서드
	public static Map<String, Object> serviceCall(Map<String, Object> requestData) throws Exception{
		
		//Map<String, Object> returnData = new HashMap<>();
		
		String jsonString = json2String(requestData.get("serviceID").toString());
		List<Map<String,Object>> jsonArray = json2Array(jsonString);
		
		System.out.println("----------[		146		]----------");
	    for(int i = 0; i < jsonArray.size(); i++){		//arraylist 사이즈 만큼 for문을 실행합니다.
	        System.out.println("jsonArray 순서 " + i + "번째 :" + jsonArray.get(i));
	        jsonArray.get(i).forEach((key, value) -> {		// forEach
	            System.out.println(key + " : " + value);

	            System.out.println("query : " + query2Map(requestData,value.toString()));
	        });
	    }
	    System.out.println("------------------------------");

	    
		return requestData;
	}
	

}
